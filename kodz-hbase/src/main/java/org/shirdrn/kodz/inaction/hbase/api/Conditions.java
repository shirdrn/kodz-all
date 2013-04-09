package org.shirdrn.kodz.inaction.hbase.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class Conditions {

	private Set<Condition> conditions = new HashSet<Condition>();
	List<Filter> filters = new ArrayList<Filter>();
	
	public void addCondition(Condition c) {
		conditions.add(c);
		Filter filter = new SingleColumnValueFilter(
				Bytes.toBytes(c.getFamily()), 
				Bytes.toBytes(c.getQualifier()), 
				c.getOp(), c.getValue());
		filters.add(filter);
	}
	
	public List<Filter> getFilterList() {
		return filters;
	}
	
	public static class Condition {
		
		private String family;
		private String qualifier;
		private CompareOp op;
		private byte[] value;
		
		public Condition(String family, String qualifier, CompareOp op,
				byte[] value) {
			super();
			this.family = family;
			this.qualifier = qualifier;
			this.op = op;
			this.value = value;
		}
		
		@Override
		public boolean equals(Object obj) {
			Condition other = (Condition) obj;
			return this.family.equals(other.family) 
					&& this.qualifier.equals(other.qualifier) 
					&& this.op.equals(other.op)
					&& this.value.equals(other.value);
		}

		public String getFamily() {
			return family;
		}

		public String getQualifier() {
			return qualifier;
		}

		public CompareOp getOp() {
			return op;
		}

		public byte[] getValue() {
			return value;
		}
		
	}
	
}
