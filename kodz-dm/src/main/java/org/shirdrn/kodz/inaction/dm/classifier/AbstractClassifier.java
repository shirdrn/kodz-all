package org.shirdrn.kodz.inaction.dm.classifier;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClassifier<T> implements Closeable {

	protected List<Attr> attributes = new ArrayList<Attr>();
	protected DataModel dataModel;
	
	public Attr addAttrName(String attrName) {
		Attr attr = new Attr(attrName);
		if(!attributes.contains(attr)) {
			attributes.add(attr);
		}
		return attr;
	}
	
	public void addAttrValue(Attr attr, String attrValue) {
		if(attributes.contains(attr)) {
			List<AttrValue> values = attr.getValues();
			AttrValue attrVal = new AttrValue();
			attrVal.setAttr(attr);
			attrVal.setValue(attrValue);
			if(!values.contains(attrVal)) {
				values.add(attrVal);
			}
		}
	}
	
	public abstract void setupModel();
	
	public abstract ClassLabel predict(DataRecord<T> record);
	
}
