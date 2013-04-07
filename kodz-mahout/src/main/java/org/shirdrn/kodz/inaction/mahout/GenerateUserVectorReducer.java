package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class GenerateUserVectorReducer extends
		Reducer<VarLongWritable, VarLongWritable, VarLongWritable, VectorWritable> {

	@Override
	protected void reduce(VarLongWritable key, Iterable<VarLongWritable> values, Context context)
			throws IOException, InterruptedException {
		// INPUT=> <userId, list(itemId)>
		VarLongWritable userId = key;
		Vector userVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 100);
		for(VarLongWritable itemId : values) {
			userVector.set((int) itemId.get(), 1.0);
		}
		// OUTPUT=> <userId, [pref(itemId(1)), pref(itemId(2)), ..., pref(itemId(n))]>
		// pref(): userId's preference to a specified itemId
		context.write(userId, new VectorWritable(userVector));
	}

}
