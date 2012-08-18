package com.startupoxygen.craft;

import java.util.List;

public class FieldSet {
	private String name;
	private boolean defaultFieldSet;
	private List<FieldAndValue> fieldAndValues;

	public List<FieldAndValue> getFieldAndValues() {
		return this.fieldAndValues;
	}

	public void setFieldAndValues(List<FieldAndValue> argFieldAndValues) {
		this.fieldAndValues = argFieldAndValues;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public boolean isDefaultFieldSet() {
		return this.defaultFieldSet;
	}

	public void setDefaultFieldSet(boolean argDefaultFieldSet) {
		this.defaultFieldSet = argDefaultFieldSet;
	}
}
