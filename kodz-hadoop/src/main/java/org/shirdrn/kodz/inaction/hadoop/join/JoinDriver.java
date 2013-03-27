//package org.shirdrn.kodz.inaction.hadoop.join;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.conf.Configured;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
//import org.apache.hadoop.util.Tool;
//import org.apache.hadoop.util.ToolRunner;
//
//public class JoinDriver extends Configured implements Tool {
//
//	@Override
//	public int run(String[] args) throws Exception {
//		
//		Configuration conf = new Configuration();
//		Job job = new Job(conf, "left-right join");
//		
//		job.setJarByClass(JoinDriver.class);
//		job.setMapperClass(JoinDomainOrganizationMapper.class);
//		job.setReducerClass(JoinDomainOrganizationReducer.class);
//		
//		job.setMapOutputKeyClass(IntWritable.class);
//		job.setMapOutputValueClass(DomainDetail.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(DomainDetail.class);
//		job.setOutputFormatClass(SequenceFileOutputFormat.class);
//	    
//		job.setNumReduceTasks(5);
//		
//		Path input = new Path(args[0].trim());
//		Path output = new Path(args[1].trim());
//		FileInputFormat.addInputPath(job, input);
//	    FileOutputFormat.setOutputPath(job, output);
//		
//		return job.waitForCompletion(true) ? 0 : 1;
//	}
//	
//	public static void main(String[] args) throws Exception {
//		int exitCode = ToolRunner.run(new JoinDriver(), args);
//		System.exit(exitCode);
//	}
//
//}
