package org.shirdrn.solr.cloud.index.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.shirdrn.solr.cloud.common.AbstractArgsAssembler;
import org.shirdrn.solr.cloud.common.AbstractIndexer;
import org.shirdrn.solr.cloud.common.DocCreator;
import org.shirdrn.solr.cloud.common.IndexingService;
import org.shirdrn.solr.cloud.conf.ClientConf;
import org.shirdrn.solr.cloud.conf.ZkConf;
import org.shirdrn.solr.cloud.index.LineDocCreator;
import org.shirdrn.solr.cloud.index.SingleThreadIndexer;

public class SingleThreadFileBasedIndexer extends SingleThreadIndexer {

	private static final Log LOG = LogFactory.getLog(SingleThreadFileBasedIndexer.class);
	private IndexingService client;
	private File inputDir;
	
	public SingleThreadFileBasedIndexer(final ClientConf clientConf, final String path) 
			throws MalformedURLException {
		super(clientConf);
		client = SingleThreadClient.newIndexingClient(clientConf, this);
		DocCreator<String> docCreator = new LineDocCreator(builder);
		builder.setDocCreator(docCreator);
		inputDir = new File(path);
		LOG.info("Input files dir: " + inputDir.getAbsolutePath());
	}

	@Override
	protected void process() throws Exception {
		if(inputDir != null) {
			File[] files = inputDir.listFiles();
			for(File file : files) {
				File f = new File(file.getAbsolutePath());
				LOG.info("Processing file: " + f.getAbsolutePath());
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(f));
					String record = null;
					while((record = reader.readLine()) != null) {
						if(record != null && !record.trim().isEmpty()) {
							client.addDoc(record);
						}
					}
				} finally {
					try {
						client.finallyCommit();
						if(reader != null) {
							reader.close();
						}
					} catch (IOException | SolrServerException e) {
						throw e;
					}
				}
			}
		}
	}
	
	public static class Assembler extends AbstractArgsAssembler<AbstractIndexer> {
		
		private static final String TYPE_SINGLE_THREAD_FILE_BASED_INDEXER = "4";
		private static final String NAME_SINGLE_THREAD_FILE_BASED_INDEXER = "Single_Thread_File_Based_Indexer";
		
		public Assembler() {
			super();
			argCount = 7;
			type = TYPE_SINGLE_THREAD_FILE_BASED_INDEXER;
			name = NAME_SINGLE_THREAD_FILE_BASED_INDEXER;
		}
		
		@Override
		public AbstractIndexer assemble(String[] args) throws Exception {
			super.assemble(args);
			
			String zkHost;
			int connectTimeout = 10000;
			int clientTimeout = 30000;
			String collection;
			int batchCount = 1000;
			String input;
			String schemaMappingFile;
			AbstractIndexer indexer = null;
			
			zkHost = args[0];
			try {
				connectTimeout = Integer.parseInt(args[1]);
				clientTimeout = Integer.parseInt(args[2]);
				batchCount = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) { }
			collection = args[4];
			schemaMappingFile = args[5];
			input = args[6];
			
			ZkConf zkConf = new ZkConf();
			zkConf.setZkHost(zkHost);
			zkConf.setZkConnectTimeout(connectTimeout);
			zkConf.setZkClientTimeout(clientTimeout);
			ClientConf clientConf = new ClientConf(zkConf);
			clientConf.setCollectionName(collection);
			clientConf.setSchemaMappingFile(schemaMappingFile);
			clientConf.setBatchCount(batchCount);
			
			try {
				indexer = new SingleThreadFileBasedIndexer(clientConf, input);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
			return indexer;
		}
		
		@Override
		public String getUsageArgList() {
			StringBuffer usage = new StringBuffer();
			usage.append("<0:zkHost> <1:connectTimeout> <2:clientTimeout> <3:batchCount> <4:collection> ")
			.append("<5:schemaMappingFile> <6:inputDir>");
			return usage.toString();
		}

		@Override
		public String[] showCLIExamples() {
			String[] examples = new String[] {
					"zk:2181 10000 30000 1000 mycollection schema-mapping.xml /home/solr/data",
					"zk:2181 10000 30000 1000 mycollection /home/solr/schemas/schema-mapping.xml /home/solr/data"
			};
			return examples;
		}
	}
	
}
