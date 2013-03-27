//package org.shirdrn.kodz.inaction.hadoop.join;
//
//import java.io.IOException;
//
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Mapper;
//
//public class JoinDomainOrganizationMapper extends
//		Mapper<LongWritable, Text, IntWritable, DomainDetail> {
//
//	@Override
//	protected void map(LongWritable key, Text value, Context context)
//			throws IOException, InterruptedException {
//		// Left : cdbaby.com	cdbaby.com	70.102.112.164	2	3
//		// Right: 45050	Zynga Game Network
//		String line = value.toString();
//		String[] fields = line.split("\t");
//		DomainDetail detail = new DomainDetail();
//		if(fields.length == 5) {
//			detail.setDomain(new Text(fields[0]));
//			detail.setIpAddress(new Text(fields[2]));
//			detail.setOrganizationId(new IntWritable(Integer.parseInt(fields[3])));
//			detail.setJoinSide(JoinSide.LEFT);
//		} else if(fields.length == 2) {
//			detail.setOrganizationId(new IntWritable(Integer.parseInt(fields[0])));
//			detail.setOrganization(new Text(fields[1]));
//			detail.setJoinSide(JoinSide.RIGHT);
//		}
//		if(fields.length == 5 || fields.length == 2) {
//			context.write(detail.getOrganizationId(), detail);
//		}
//	}
//
//}
