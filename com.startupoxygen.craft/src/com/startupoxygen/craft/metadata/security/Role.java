package com.startupoxygen.craft.metadata.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Role {
	private String name;
	private List<Grant> grants;

	public Role() {
		this.grants = new ArrayList<Grant>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public void addGrant(Grant argGrant) {
		this.grants.add(argGrant);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
