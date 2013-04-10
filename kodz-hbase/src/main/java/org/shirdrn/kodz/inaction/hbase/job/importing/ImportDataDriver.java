package org.shirdrn.kodz.inaction.hbase.job.importing;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * Table DDL: create 't_sub_domains', 'cf_basic', 'cf_status'
 * <pre>
 * cf_basic:domain cf_basic:len
 * cf_status:status cf_status:live
 * </pre>
 * 
 * @author shirdrn
 */
public class ImportDataDriver {

	public static void main(String[] args) 
			throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {
		if(args.length != 2) {
			System.err.println("Usage: \n" +
					" ImportDataDriver <tableName> <input>");
			System.exit(1);
		}
		String tableName = args[0].trim();
		String input = args[1].trim();
//		String libjars = args[2].trim();
		
		Configuration conf = HBaseConfiguration.create();
		// set table columns
		conf.set("table.cf.family", "cf_basic");
		conf.set("table.cf.qualifier.fqdn", "domain");
		conf.set("table.cf.qualifier.timestamp", "create_at");
				
		Job job = new Job(conf, "Import into HBase table");
		job.setJarByClass(ImportDataDriver.class);
		job.setMapperClass(ImportFileLinesMapper.class);
		job.setOutputFormatClass(TableOutputFormat.class);
		
		job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, tableName);
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Writable.class);
		
		job.setNumReduceTasks(0);
		
		FileInputFormat.addInputPath(job, new Path(input));
		
		// add external jar files to distributed cache
//		Path dependedJars = new Path(libjars);
//		FileSystem fs = dependedJars.getFileSystem(conf);
//		String qualifiedJarPath = dependedJars.makeQualified(fs).toString();
//		DistributedCache.addCacheArchive(new URI(qualifiedJarPath), conf);
//		DistributedCache.createSymlink(conf);
		
		int exitCode = job.waitForCompletion(true) ? 0 : 1;
		System.exit(exitCode);
	}

}
