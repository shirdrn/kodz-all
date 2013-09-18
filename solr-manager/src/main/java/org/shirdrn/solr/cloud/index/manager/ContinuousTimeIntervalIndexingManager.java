package org.shirdrn.solr.cloud.index.manager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.solr.cloud.common.AbstractIndexer;
import org.shirdrn.solr.cloud.common.AbstractIndexingManager;
import org.shirdrn.solr.cloud.common.ArgsAssembler;
import org.shirdrn.solr.cloud.executors.DefaultThreadFactory;
import org.shirdrn.solr.cloud.executors.ScheduleAgainPolicy;
import org.shirdrn.solr.cloud.utils.FileUtils;
import org.shirdrn.solr.cloud.utils.TimeUtils;

public class ContinuousTimeIntervalIndexingManager extends AbstractIndexingManager {

	private static final Log LOG = LogFactory.getLog(ContinuousTimeIntervalIndexingManager.class);
	protected int threadPoolSize;
	protected File metadataDir;
	protected int beforeHours;
	private ExecutorService pool;
	private final AtomicInteger counter = new AtomicInteger(0);
	private final Object lock = new Object();
	
	public ContinuousTimeIntervalIndexingManager() {
		super();
		// initialize thread pool
        if(mappedCount != 0) {
        	int nThreads = Math.min(threadPoolSize, mappedCount);
        	if(nThreads > 0) {
        		threadPoolSize = nThreads;
        		LOG.info("Thread pool size: threadPoolSize=" + threadPoolSize);
        		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(threadPoolSize);
        		pool = new ThreadPoolExecutor(
        				1, threadPoolSize, 120, TimeUnit.SECONDS, workQueue, 
        				new DefaultThreadFactory("POOL"), new ScheduleAgainPolicy(threadPoolSize));
        	}
        } else {
        	LOG.warn("0 mapping items are configured!!!");
        	System.exit(0);
        }
	}
	
	@Override
	public void parseProperties() {
		super.parseProperties();
		this.threadPoolSize = getInt("solr.thread.pool.size", 1);
		String md = get("solr.index.metadata.dir", "index.metadata");
		this.metadataDir = new File(md);
		if (!metadataDir.exists()) {
			metadataDir.mkdirs();
		}
		LOG.info("Metadata directory: " + metadataDir.getAbsolutePath());
		beforeHours = getInt("solr.index.init.hour.before", 24);
		this.timestampFormat = get("solr.index.timestamp.format", "yyyy-MM-dd HH:mm:ss");
	}
	
	@Override
	public void buildIndexes(String[] args) throws Throwable {
		Iterator<Entry<String, MappedConf>>  iter = getIterator();
		while(iter.hasNext()) {
			final Entry<String, MappedConf> entry = iter.next();
			LOG.info("Indexing for: collection=" + entry.getKey() + ",table=" + entry.getValue().getTableName());
			pool.execute(this.makeWorker(entry));
		}
		
		// wait thread to finish task
		synchronized(lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// dump statistics information
		dumpStatAll();
		// shut down thread pool service
		pool.shutdown();
	
	}

	protected Worker makeWorker(final Entry<String, MappedConf> entry) {
		return new Worker(entry);
	}
	
	protected static final Log WLOG = LogFactory.getLog(Worker.class);
	public class Worker implements Runnable {
		protected Entry<String, MappedConf> entry;
		protected String previousIndexTime;
		protected String lastIndexTime;
		protected File thisFile;
		
		public Worker(Entry<String, MappedConf> entry) {
			super();
			this.entry = entry;
			thisFile = new File(metadataDir, entry.getValue().getTableName());
		}
		
		@Override
		public void run() {
			WLOG.info("Enter worker thread body.");
			counter.incrementAndGet();
			ArgsAssembler<? extends AbstractIndexer> assembler = null;
			AbstractIndexer indexer = null;
			StatInfo stat = getStat(entry.getKey());
			try {
				String conditions = checkConditions(entry.getValue().getTableName());
				stat.setQueryCondition(conditions);
				WLOG.info("Concatenated query condition: " + conditions);
				// create assembler
				assembler = createAssembler();
				// create indexer
				indexer = assembler.assemble(new String[] {
						zkHost, String.valueOf(connectTimeout), String.valueOf(clientTimeout), String.valueOf(batchCount), 
						entry.getKey(), entry.getValue().getSchemaMappingFile(),
						jdbcUrl, entry.getValue().getTableName(), conditions
				});
				WLOG.info("Indexer instance created: indexer=" + indexer);
				// execute to index
				WLOG.info("Start to index documents...");
				indexer.indexDocs();
				WLOG.info("Done!");
				indexer.close();
				// write last index time to file
				FileUtils.writeToFile(thisFile, lastIndexTime);
				WLOG.info("Write lastIndexTime to file: lastIndexTime=" + lastIndexTime + ", file=" + thisFile.getAbsolutePath());
			} catch (Throwable t) {
				stat.getCauses().add(t);
				WLOG.error(t);
			} finally {
				if(counter.get() == mappedCount) {
					synchronized(lock) {
						lock.notify();
					}
				}
				
				// collect statistics information
				WLOG.info("Collect statistics information...");
				stat.setAssembler(assembler);
				stat.setIndexer(indexer);
				stat.setPreviousIndexTime(previousIndexTime);;
				stat.setLastIndexTime(lastIndexTime);;
				stat.setMappedConf(entry.getValue());
				WLOG.info("Leave worker thread body.");
			}
		}
		
		protected String checkConditions(String tableName) {
			Date date = TimeUtils.getDateBefore(Calendar.HOUR_OF_DAY, beforeHours);
			String previousTime = TimeUtils.format(date, timestampFormat);
			lastIndexTime = TimeUtils.format(new Date(), timestampFormat);
			StringBuffer condition = new StringBuffer();
			if(!thisFile.exists()) {
				try {
					thisFile.createNewFile();
				} catch (IOException e) {
					WLOG.error("Error to create file: " + thisFile.getAbsolutePath());
					throw new RuntimeException(e);
				}
			} else {
				List<String> lines = FileUtils.populateListWithLines(thisFile, "UTF-8");
				previousTime = lines.get(0);
			}
			previousIndexTime = previousTime;
			condition.append(entry.getValue().getTimestampFieldName()).append(">='").append(previousTime).append("'")
			.append(" AND ").append(entry.getValue().getTimestampFieldName()).append("<='").append(lastIndexTime).append("'");
			return condition.toString();
		}
		
	
	}

}
