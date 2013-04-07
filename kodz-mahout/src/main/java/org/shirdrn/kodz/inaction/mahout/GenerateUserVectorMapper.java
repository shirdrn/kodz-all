package org.shirdrn.kodz.inaction.mahout;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.math.VarLongWritable;

public class GenerateUserVectorMapper extends
		Mapper<LongWritable, Text, VarLongWritable, VarLongWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// INPUT=> <Offset, line(userId:itemId1	itemId2	itemId3	...itemIdn)>
		String record = value.toString().trim();
		// check file format
		if(!record.isEmpty()) {
			String[] userItemsPair = record.split("[\t:]+"); 
			VarLongWritable userId = new VarLongWritable(Long.parseLong(userItemsPair[0]));
			// reuse the itemId instance
			VarLongWritable itemId = new VarLongWritable();
			for(int i=1; i<userItemsPair.length; i++) {
				itemId.set(Long.parseLong(userItemsPair[i]));
				// OUTPUT=> <userId, itemId>
				context.write(userId, itemId);
			}
		}
	}
	
}
