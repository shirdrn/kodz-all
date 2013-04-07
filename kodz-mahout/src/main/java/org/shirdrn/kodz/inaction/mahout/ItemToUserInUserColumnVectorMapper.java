package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;

public class ItemToUserInUserColumnVectorMapper extends
		Mapper<VarLongWritable, VectorWritable, IntWritable, VectorOrPrefWritable> {

	@Override
	protected void map(VarLongWritable key, VectorWritable value, Context context)
			throws IOException, InterruptedException {
		// INPUT=> <userId, [itemId(1):pref(1), itemId(2):pref(2), ..., itemId(n):pref(n)]>
		long userId = key.get();
		Vector userVector = value.get();
		Iterator<Element> iter = userVector.iterateNonZero();
		IntWritable writableItemId = new IntWritable();
		while(iter.hasNext()) {
			Element element = iter.next();
			int itemId = element.index();
			writableItemId.set(itemId);
			double pref = element.get();
			// OUTPUT=> <itemId, userId(i):pref(i)>
			context.write(writableItemId, new VectorOrPrefWritable(userId, (float) pref));
		}
	}

}
