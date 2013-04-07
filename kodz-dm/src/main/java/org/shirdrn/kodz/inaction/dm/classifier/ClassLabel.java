package org.shirdrn.kodz.inaction.dm.classifier;

public class ClassLabel {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		ClassLabel other = (ClassLabel) obj;
		return this.name.equals(other.name);
	}
	
}
