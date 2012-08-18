package com.startupoxygen.craft.metadata;

import com.startupoxygen.craft.validation.DataTypeValidator;

public class DataType {
	private String name;
	private boolean primitive;
	private boolean userDefined;
	private Class<? extends DataTypeValidator> validator;

	public DataType() {
		super();
	}

	public DataType(String argName, boolean argPrimitive) {
		this.name = argName;
		this.primitive = argPrimitive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public boolean isPrimitive() {
		return this.primitive;
	}

	public void setPrimitive(boolean argPrimitive) {
		this.primitive = argPrimitive;
	}

	public boolean isUserDefined() {
		return this.userDefined;
	}

	public void setUserDefined(boolean argUserDefined) {
		this.userDefined = argUserDefined;
	}

	public Class<? extends DataTypeValidator> getValidator() {
		return this.validator;
	}

	public void setValidator(Class<? extends DataTypeValidator> argValidator) {
		this.validator = argValidator;
	}

	@Override
	public String toString() {
		return "DataType [name=" + this.name + ", primitive=" + this.primitive
				+ ", userDefined=" + this.userDefined + ", validator="
				+ this.validator + "]";
	}

}
