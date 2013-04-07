package org.shirdrn.kodz.inaction.hadoop.join.datajoin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.contrib.utils.join.TaggedMapOutput;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class TaggedDomainMapOutput extends TaggedMapOutput {

	private Text textLineData;
	
	public TaggedDomainMapOutput() {
		super();
		this.textLineData = new Text();
	}
	
	public TaggedDomainMapOutput(Text textLine) {
		super();
		this.textLineData = textLine;
	}
	
	public TaggedDomainMapOutput(TaggedDomainMapOutput record) {
		this();
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.tag.readFields(in);
		textLineData.readFields(in);
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.tag.write(out);
		textLineData.write(out);
	}

	@Override
	public Writable getData() {
		return textLineData;
	}

}
