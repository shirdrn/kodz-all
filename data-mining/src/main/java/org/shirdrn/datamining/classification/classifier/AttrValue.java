package org.shirdrn.datamining.classification.classifier;


public class AttrValue {

	protected Attr attr;
	protected String value;
	
	public AttrValue() {
		super();
	}
	
	public AttrValue(Attr attr, String value) {
		super();
		this.attr = attr;
		this.value = value;
	}

	public Attr getAttr() {
		return attr;
	}
	
	public void setAttr(Attr attr) {
		this.attr = attr;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		AttrValue other = (AttrValue) obj;
		return this.attr.equals(other.attr) 
				&& this.value.equals(other.value);
	}
	
}
