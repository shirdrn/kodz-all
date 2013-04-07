package org.shirdrn.kodz.inaction.hadoop.join.datajoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.contrib.utils.join.DataJoinMapperBase;
import org.apache.hadoop.contrib.utils.join.DataJoinReducerBase;
import org.apache.hadoop.contrib.utils.join.TaggedMapOutput;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.shirdrn.kodz.inaction.hadoop.join.reduceside.ReduceSideJoinDriver;

public class DataJoinDriver extends Configured implements Tool {

	public static class DomainOrgMapper extends DataJoinMapperBase {

		@Override
		protected Text generateInputTag(String inputFile) {
			// irc_basic_info.ds
			// irc_org_info.ds
			String tag = inputFile.substring(4, 5); // b | o
			return new Text(tag);
		}

		@Override
		protected TaggedMapOutput generateTaggedMapOutput(Object value) {
			Text textLineData = (Text) value;
			TaggedDomainMapOutput aRecord = new TaggedDomainMapOutput(textLineData);
			aRecord.setTag(new Text(this.inputTag));
			return aRecord;
		}

		@Override
		protected Text generateGroupKey(TaggedMapOutput aRecord) {
			String line = aRecord.getData().toString();	
			String[] fields = line.split("\t");
			String groupKey = null;
			if(fields.length == 5) {
				// cdbaby.com	cdbaby.com	70.102.112.164	2	3
				groupKey = fields[3];
				
			} else if(fields.length == 2) {
				// 286434	_public_OKURA HARDWARE AND LUMBER
				groupKey = fields[0];
			}
			return new Text(groupKey);
		}
		
	}
	
	public static class DomainOrgReducer extends DataJoinReducerBase {

		@Override
		protected TaggedMapOutput combine(Object[] tags, Object[] values) {
			if(tags.length < 2) {
				return null;
			}
			StringBuilder joinedRecord = new StringBuilder();
			for(int i=0; i<tags.length; i++) {
				if(i > 0) {
					joinedRecord.append("\t");
				}
				TaggedMapOutput value = (TaggedMapOutput) values[i];
				// a record line
				String line = value.getData().toString();
				String[] fields = line.split("\t");
				if(fields.length == 5) {
					joinedRecord.append(fields[0]).append("\t"); // domain
					joinedRecord.append(fields[2]); // ip address
				}
			}
			TaggedMapOutput output = new TaggedDomainMapOutput(new Text(joinedRecord.toString()));
			return output;
		}
		
	}
	
	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		JobConf job = new JobConf(conf, DataJoinDriver.class);
		job.setJarByClass(ReduceSideJoinDriver.class);
		job.setJobName("datajoin join");
		
		Path input = new Path(args[0].trim());
		Path output = new Path(args[1].trim());
		FileInputFormat.setInputPaths(job, input);
		FileOutputFormat.setOutputPath(job, output);
	    
		job.setInputFormat(TextInputFormat.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(TaggedDomainMapOutput.class);
	    
		job.setMapperClass(DomainOrgMapper.class);
		job.setReducerClass(DomainOrgReducer.class);
		job.setNumReduceTasks(10);
		
		// add contrib/hadoop-datajoin-.jar
		Path jarPath = new Path(args[2].trim());
		FileSystem fs = jarPath.getFileSystem(conf);
		DistributedCache.addArchiveToClassPath(jarPath, conf, fs);
		DistributedCache.createSymlink(job);  
		
		JobClient.runJob(job);
		return 0;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DataJoinDriver(), args);
		System.exit(exitCode);
	}

}
