package org.shirdrn.kodz.inaction.hbase.job.count;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class CountValueSizeReducer extends
		Reducer<IntWritable, LongWritable, IntWritable, LongWritable> {

	@Override
	protected void reduce(IntWritable key, Iterable<LongWritable> values, Context context)
			throws IOException, InterruptedException {
		int count = 0;
		for(LongWritable val : values) {
			count += val.get();
		}
		context.write(key, new LongWritable(count));
	}

}
