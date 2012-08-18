package com.startupoxygen.craft.commands;

public class Option {
	private String name;
	private String value;

	public Option() {
		super();
	}

	public Option(String argName, String argValue) {
		super();
		this.name = argName;
		this.value = argValue;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String argValue) {
		this.value = argValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result
				+ ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Option other = (Option) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Option [name=" + this.name + ", value=" + this.value + "]";
	}

}
