package org.shirdrn.solr.cloud.index;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.solr.cloud.common.AbstractIndexer;
import org.shirdrn.solr.cloud.conf.ClientConf;
import org.shirdrn.solr.cloud.executors.ScheduleAgainPolicy;
import org.shirdrn.solr.cloud.executors.DefaultThreadFactory;

public abstract class MultiThreadedIndexer extends AbstractIndexer {

	private static final Log LOG = LogFactory.getLog(MultiThreadedIndexer.class);
	private final ExecutorService executorService;
	
	public MultiThreadedIndexer(ClientConf clientConf, int nThreads) {
		super(clientConf);
		int workQSize = 2 * nThreads;
		BlockingQueue<Runnable> q = new ArrayBlockingQueue<>(workQSize);
		executorService = new ThreadPoolExecutor(nThreads, nThreads,
				10000L, TimeUnit.MILLISECONDS, q, new DefaultThreadFactory(), 
				new ScheduleAgainPolicy(workQSize));
	}
	
	protected void execute(Runnable r) {
		executorService.execute(r);
	}
	
	@Override
	public void close() throws IOException {
		LOG.info("Close thread executor...");
		executorService.shutdown();
		super.close();
	}

}
