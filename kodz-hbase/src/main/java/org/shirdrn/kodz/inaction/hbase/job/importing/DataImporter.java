package org.shirdrn.kodz.inaction.hbase.job.importing;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * Table DDL: create 't_sub_domains', 'cf_basic', 'cf_status'
 * <pre>
 * cf_basic:domain cf_basic:len
 * cf_status:status cf_status:live
 * </pre>
 * 
 * @author shirdrn
 */
public class DataImporter {

	public static void main(String[] args) 
			throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {
		
		Configuration conf = HBaseConfiguration.create();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		
		assert(otherArgs.length == 2);
		
		if(otherArgs.length < 2) {
			System.err.println("Usage: \n" +
					" ImportDataDriver -libjars <jar1>[,<jar2>...[,<jarN>]] <tableName> <input>");
			System.exit(1);
		}
		String tableName = otherArgs[0].trim();
		String input = otherArgs[1].trim();
		
		// set table columns
		conf.set("table.cf.family", "cf_basic");
		conf.set("table.cf.qualifier.fqdn", "domain");
		conf.set("table.cf.qualifier.timestamp", "create_at");
				
		Job job = new Job(conf, "Import into HBase table");
		job.setJarByClass(DataImporter.class);
		job.setMapperClass(ImportFileLinesMapper.class);
		job.setOutputFormatClass(TableOutputFormat.class);
		
		job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, tableName);
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Put.class);
		
		job.setNumReduceTasks(0);
		
		FileInputFormat.addInputPath(job, new Path(input));
		
		int exitCode = job.waitForCompletion(true) ? 0 : 1;
		System.exit(exitCode);
	}

}
