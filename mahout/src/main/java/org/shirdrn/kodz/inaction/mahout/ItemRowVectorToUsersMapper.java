package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorAndPrefsWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

/**
 * 
 * For each itemID, the value includes the following content:
 * <pre>
 * &lt;itemId, list(userId(i):pref(i))&gt;
 * &lt;ItemId, Vector[itemId(j):pref(j)]&gt;
 * </pre>
 * which encapsulates into {@link VectorAndPrefsWritable} instance.
 * </br>
 * The output format like:
 * <pre>
 * &lt;userId(i), userPref(i) * Vector[itemId(j):pref(j)]&gt;
 * </pre>
 * 
 * @author Shirdrn
 */
public class ItemRowVectorToUsersMapper extends
		Mapper<IntWritable, VectorAndPrefsWritable, VarLongWritable, VectorWritable> {

	@Override
	protected void map(IntWritable key, VectorAndPrefsWritable value, Context context)
			throws IOException, InterruptedException {
		// INPUT=>
		// <itemId, list(userId(i):pref(i))>
		// <ItemId, Vector[itemId(j):pref(j)]>
		Vector row = value.getVector();
		List<Long> userIds = value.getUserIDs();
		List<Float> userPrefs = value.getValues();
		for(int i=0; i<userIds.size(); i++) {
			long userId = userIds.get(i);
			float userPref = userPrefs.get(i);
			// each element in row vector is multiplied by userPref
			Vector multipliedVector = row.times(userPref);
			// OUTPUT=>
			// <userId(i), userPref(i) * Vector[itemId(j):pref(j)]>
			context.write(new VarLongWritable(userId), new VectorWritable(multipliedVector));
		}
		
		// [itemId(1) itemId(2) ... itemId(n)]    X     userId(1):pref(1) userId(2):pref(2) ... userId(m):pref(m)
		// =>
		// itemId(1) X 
	}

	

}
