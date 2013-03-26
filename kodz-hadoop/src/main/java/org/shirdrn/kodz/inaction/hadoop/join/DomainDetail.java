package org.shirdrn.kodz.inaction.hadoop.join;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class DomainDetail implements Writable {

	private Text domain;
	private Text ipAddress;
	private IntWritable organizationId;
	private Text organization;
	private JoinSide joinSide;
	
	public DomainDetail() {
		super();
		this.domain = new Text();
		this.ipAddress = new Text();
		this.organizationId = new IntWritable();
		this.organization = new Text();
		this.joinSide = JoinSide.LEFT;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		domain.readFields(in);
		ipAddress.readFields(in);
		organizationId.readFields(in);
		organization.readFields(in);

	}

	@Override
	public void write(DataOutput out) throws IOException {
		domain.write(out);
		ipAddress.write(out);
		organizationId.write(out);
		organization.write(out);
	}
	
	public Text getDomain() {
		return domain;
	}

	public void setDomain(Text domain) {
		this.domain = domain;
	}

	public Text getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(Text ipAddress) {
		this.ipAddress = ipAddress;
	}

	public IntWritable getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(IntWritable organizationId) {
		this.organizationId = organizationId;
	}

	public Text getOrganization() {
		return organization;
	}

	public void setOrganization(Text organization) {
		this.organization = organization;
	}

	public JoinSide getJoinSide() {
		return joinSide;
	}

	public void setJoinSide(JoinSide joinSide) {
		this.joinSide = joinSide;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(domain != null) {
			builder.append(domain.toString()).append("\t");
		}
		if(ipAddress != null) {
			builder.append(ipAddress.toString()).append("\t");
		}
		builder.append(organizationId.get()).append("\t");
		if(organizationId != null) {
			builder.append(organization.toString());
		}
		return builder.toString();
	}

}
