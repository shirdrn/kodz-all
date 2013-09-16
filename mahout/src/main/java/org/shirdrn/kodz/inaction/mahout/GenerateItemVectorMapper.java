package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;

public class GenerateItemVectorMapper extends
		Mapper<IntWritable, VectorWritable, IntWritable, IntWritable> {

	@Override
	protected void map(IntWritable key, VectorWritable value, Context context)
			throws IOException, InterruptedException {
		// INPUT=> <userId, [itemId(1), ItemId(2), ...,itemId(n)]>
		VectorWritable userVector = value;
		Iterator<Element> iter = userVector.get().iterateNonZero();
		while(iter.hasNext()) {
			Element leftElement = iter.next();
			int leftItemId = leftElement.index();
			// iterate all
			Iterator<Element> it = userVector.get().iterateNonZero();
			while(it.hasNext()) {
				Element rightElement = iter.next();
				int rightItemId = rightElement.index();
				// OUTPUT=> <itemId(i), itemId(j)>
				context.write(new IntWritable(leftItemId), new IntWritable(rightItemId));
			}
			
		}
	}

}
