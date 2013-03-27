//package org.shirdrn.kodz.inaction.hadoop.join;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Reducer;
//
//public class JoinDomainOrganizationReducer extends
//		Reducer<IntWritable, DomainDetail, Text, DomainDetail> {
//
//	@Override
//	protected void reduce(IntWritable key, Iterable<DomainDetail> values, Context context)
//			throws IOException, InterruptedException {
//		Iterator<DomainDetail> iter = values.iterator();
//		DomainDetail pre = null;
//		List<DomainDetail> group = new ArrayList<DomainDetail>();
//		while(iter.hasNext()) {
//			DomainDetail next = iter.next();
//			if(pre == null) {
//				pre = next;
//				group.add(pre);
//			}
//			while(iter.hasNext()) {
//				next = iter.next();
//				if(next.getOrganizationId().get() == pre.getOrganizationId().get()) {
//					group.add(next);
//				} else {
//					collect(context, group);
//					// prepare for next group
//					group = new ArrayList<DomainDetail>();
//					pre = next;
//					group.add(pre);
//				}
//			}
//		}
//		
//	}
//
//	private void collect(Context context, List<DomainDetail> group) throws IOException, InterruptedException {
//		if(group.size() >= 2) {
//			DomainDetail first = group.get(0);
//			DomainDetail second = null;
//			for(int i=1; i<group.size(); i++) {
//				if(group.get(i).getJoinSide() != first.getJoinSide()) {
//					second = group.get(i);
//					break;
//				}
//			}
//			if(second != null) {
//				if(first.getJoinSide() == JoinSide.LEFT) {
//					first.setOrganization(second.getOrganization());
//					context.write(first.getDomain(), first);
//				} else {
//					second.setOrganization(first.getOrganization());
//					context.write(second.getDomain(), second);
//				}
//			}
//		}
//			
//	}
//
//}
