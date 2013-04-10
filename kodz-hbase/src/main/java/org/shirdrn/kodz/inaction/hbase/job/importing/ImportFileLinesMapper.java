package org.shirdrn.kodz.inaction.hbase.job.importing;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.shirdrn.kodz.inaction.hbase.utils.DatetimeUtils;

public class ImportFileLinesMapper extends
		Mapper<LongWritable, Text, ImmutableBytesWritable, Writable> {

	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private byte[] basicFamily = null;
	private byte[] qualifierFqdn = null;
	private byte[] qualifierTimestamp = null;
	
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		String f = conf.get("table.cf.family", "cf_basic");
		String fqdn = conf.get("table.cf.qualifier.fqdn", "domain");
		String timestamp = conf.get("table.cf.qualifier.timestamp", "create_at");
		basicFamily = Bytes.toBytes(f);
		qualifierFqdn = Bytes.toBytes(fqdn);
		qualifierTimestamp = Bytes.toBytes(timestamp);
	}
	
	byte[] lengthQualifier = Bytes.toBytes("len");
	byte[] statusFamily = Bytes.toBytes("cf_status");
	byte[] statusQualifier = Bytes.toBytes("status");
	byte[] liveQualifier = Bytes.toBytes("live");
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		byte[] rowKey = DigestUtils.md5(line);
		Put put = new Put(rowKey);
		put.add(basicFamily, qualifierFqdn, Bytes.toBytes(line));
		String creatAt = DatetimeUtils.formatDateTime(new Date(), DATE_FORMAT);
		put.add(basicFamily, qualifierTimestamp, Bytes.toBytes(creatAt));
		put.add(basicFamily, lengthQualifier, Bytes.toBytes(line.length()));
		
		// set record status
		put.add(statusFamily, statusQualifier, Bytes.toBytes(1));
		put.add(statusFamily, liveQualifier, Bytes.toBytes(0));
		
		context.write(new ImmutableBytesWritable(rowKey), put);
	}

}
