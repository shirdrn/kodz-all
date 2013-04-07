package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class GenerateItemVectorReducer extends
		Reducer<IntWritable, IntWritable, IntWritable, VectorWritable> {

	@Override
	protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		// INPUT=> <itemId(i), list(itemId(j))>
		IntWritable thisItemId = key;
		Vector itemVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 100);
		for(IntWritable itemId : values) {
			int index = (int) itemId.get();
			itemVector.set(index, itemVector.get(index) + 1.0);
		}
		// OUTPUT=> <itemId(i), [count(itemId1), count(itemId2), ..., count(itemIdn)]>
		context.write(thisItemId, new VectorWritable(itemVector));
	}

}
