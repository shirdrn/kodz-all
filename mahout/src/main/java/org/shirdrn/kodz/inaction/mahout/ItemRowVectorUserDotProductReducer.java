package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class ItemRowVectorUserDotProductReducer extends
		Reducer<VarLongWritable, VectorWritable, VarLongWritable, VectorWritable> {

	@Override
	protected void reduce(VarLongWritable key, Iterable<VectorWritable> values, Context context)
			throws IOException, InterruptedException {
		// 
		VarLongWritable userId = key;
		Vector thisUserResultVector = null;
		for(VectorWritable vector : values) {
			if(thisUserResultVector == null) {
				thisUserResultVector = vector.get();
			} else {
				thisUserResultVector = thisUserResultVector.plus(vector.get());
			}
		}
		context.write(userId, new VectorWritable(thisUserResultVector));
	}

	

}
