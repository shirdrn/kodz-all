package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VectorWritable;

public class ItemColumnVectorWrapperMapper extends
		Mapper<IntWritable, VectorWritable, IntWritable, VectorOrPrefWritable> {

	@Override
	protected void map(IntWritable key, VectorWritable value, Context context)
			throws IOException, InterruptedException {
		// INPUT=> <ItemId, [itemId(1):pref, itemId(2):pref, ...,itemId(n):pref]>
		VectorOrPrefWritable wrapped = new VectorOrPrefWritable(value.get());
		// INPUT=> <ItemId, Vector[itemId(1):pref, itemId(2):pref, ...,itemId(n):pref]>
		context.write(key, wrapped);
	}

}
