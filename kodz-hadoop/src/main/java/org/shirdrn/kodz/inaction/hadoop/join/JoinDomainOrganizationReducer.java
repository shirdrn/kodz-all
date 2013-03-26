package org.shirdrn.kodz.inaction.hadoop.join;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinDomainOrganizationReducer extends
		Reducer<IntWritable, DomainDetail, Text, DomainDetail> {

	@Override
	protected void reduce(IntWritable key, Iterable<DomainDetail> values, Context context)
			throws IOException, InterruptedException {
		Iterator<DomainDetail> iter = values.iterator();
		DomainDetail pre = null;
		if(iter.hasNext()) {
			pre = iter.next();
		}
		boolean finishtoIterateThisGroup = false;
		while(iter.hasNext()) {
			DomainDetail next = iter.next();
			if(!finishtoIterateThisGroup 
					&& pre.getOrganizationId().get() == next.getOrganizationId().get()) {
				if(pre.getJoinSide() != next.getJoinSide()) {
					if(pre.getJoinSide() == JoinSide.LEFT) {
						pre.setOrganization(next.getOrganization());
						context.write(pre.getDomain(), pre);
					} else {
						next.setOrganization(pre.getOrganization());
						context.write(next.getDomain(), next);
					}
					finishtoIterateThisGroup = true;
				}
				// see next
				if(iter.hasNext()) {
					DomainDetail temp = iter.next();
					if(temp.getOrganizationId().get() != pre.getOrganizationId().get()) {
						pre = temp;
						finishtoIterateThisGroup = false;
					}
				}
			} else {
				if(next.getOrganizationId().get() != pre.getOrganizationId().get()) {
					pre = next;
					finishtoIterateThisGroup = false;
				}
			}
		}
	}

}
