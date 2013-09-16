package org.shirdrn.kodz.inaction.hbase.job.count;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class CountValueSize {

	public static void main(String[] args) 
			throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = HBaseConfiguration.create();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length != 2) {
			System.err.println("CountValueSize <tableName> <output>");
			System.exit(1);
		}
		String table = otherArgs[0].trim();
		String output = otherArgs[1].trim();
		
		Job job = new Job(conf, "count value length");
		job.setJarByClass(CountValueSize.class);
		Scan scan = new Scan();
		TableMapReduceUtil.initTableMapperJob(
				table, scan, CountValueSizeMapper.class, IntWritable.class, LongWritable.class, job);
		job.setCombinerClass(CountValueSizeReducer.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(LongWritable.class);
		job.setReducerClass(CountValueSizeReducer.class);
		job.setNumReduceTasks(1);
		
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		int exitCode = job.waitForCompletion(true) ? 0 : 1;
		System.exit(exitCode);
	}

}
