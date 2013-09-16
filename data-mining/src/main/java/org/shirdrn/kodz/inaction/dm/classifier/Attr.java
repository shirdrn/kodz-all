package org.shirdrn.kodz.inaction.dm.classifier;

import java.util.ArrayList;
import java.util.List;


public class Attr {

	private String name;
	private List<AttrValue> values = new ArrayList<AttrValue>(0);
	
	public Attr() {
		super();
	}
	
	public Attr(String name) {
		super();
		this.name = name;
	}
	
	public Attr(String name, List<AttrValue> values) {
		super();
		this.name = name;
		this.values = values;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<AttrValue> getValues() {
		return values;
	}
	
	public void setValues(List<AttrValue> values) {
		this.values = values;
	}

	@Override
	public boolean equals(Object obj) {
		Attr other = (Attr) obj;
		return this.name.equals(other.name);
	}

}
