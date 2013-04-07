package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.cf.taste.hadoop.item.VectorAndPrefsWritable;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VarIntWritable;
import org.apache.mahout.math.Vector;

import com.google.common.collect.Lists;

/**
 * 
 * @author shirdrn
 * 
 * @see {@link ToVectorAndPrefReducer}
 */
public class GenerateVectorAndPrefReducer 
	extends Reducer<VarIntWritable, VectorOrPrefWritable, VarIntWritable, VectorAndPrefsWritable> {

	@Override
	  protected void reduce(VarIntWritable key, Iterable<VectorOrPrefWritable> values, Context context) 
			  throws IOException, InterruptedException {
	    List<Long> userIDs = Lists.newArrayList();
	    List<Float> prefValues = Lists.newArrayList();
	    Vector similarityMatrixColumn = null;
	    for (VectorOrPrefWritable value : values) {
	      if (value.getVector() == null) {
	        // Then this is a user-pref value
	    	// <itemId, userId(i):pref(i)> 
	        userIDs.add(value.getUserID());
	        prefValues.add(value.getValue());
	      } else {
	        // Then this is the column vector
         	// <ItemId, Vector[itemId(1):pref, itemId(2):pref, ...,itemId(n):pref]>
	        if (similarityMatrixColumn != null) {
	          throw new IllegalStateException("Found two similarity-matrix columns for item index " + key.get());
	        }
	        similarityMatrixColumn = value.getVector();
	      }
	    }

	    if (similarityMatrixColumn == null) {
	      return;
	    }

	    VectorAndPrefsWritable vectorAndPrefs = new VectorAndPrefsWritable(similarityMatrixColumn, userIDs, prefValues);
	    context.write(key, vectorAndPrefs);
	  }
}
