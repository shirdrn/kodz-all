package org.shirdrn.kodz.inaction.hbase.job.count;

import java.io.IOException;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

public class CountValueSizeMapper extends TableMapper<IntWritable, LongWritable> {

	@Override
	protected void map(ImmutableBytesWritable key, Result value, Context context)
			throws IOException, InterruptedException {
		// key: row
		// value: columns
		for(KeyValue kv : value.list()) {
			context.write(new IntWritable(kv.getKeyLength()), 
					new LongWritable(kv.getValueLength()));
		}
	}

	

}
