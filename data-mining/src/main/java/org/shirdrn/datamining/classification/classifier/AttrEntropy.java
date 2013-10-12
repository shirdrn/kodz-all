package org.shirdrn.datamining.classification.classifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttrEntropy {

	private Attr attr;
	private Map<AttrValue, LabelledAttrValue> values = new HashMap<AttrValue, LabelledAttrValue>(0);
	
	public class LabelledAttrValue extends AttrValue {
		
		private Map<ClassLabel, Integer> accumulator = new HashMap<ClassLabel, Integer>(0);
		
		public Integer get(ClassLabel classLabel) {
			return accumulator.get(classLabel);
		}
		
		public void incr(ClassLabel classLabel) {
			Integer c = accumulator.get(classLabel);
			c++;
		}

		public Map<ClassLabel, Integer> getAccumulator() {
			return Collections.unmodifiableMap(accumulator);
		}
		
	}
	
}
