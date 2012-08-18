package com.startupoxygen.craft.metadata.security;

import java.util.List;

import com.startupoxygen.craft.metadata.Entity;

public class Grant {
	private List<String> permissions;
	private Entity entity;
	private List<Filter> filters;

	public Grant() {
		super();
	}

	public List<String> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(List<String> argPermissions) {
		this.permissions = argPermissions;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity argEntity) {
		this.entity = argEntity;
	}

	public List<Filter> getFilters() {
		return this.filters;
	}

	public void setFilters(List<Filter> argFilters) {
		this.filters = argFilters;
	}

	@Override
	public String toString() {
		return "Grant [permissions=" + this.permissions + ", entity="
				+ this.entity + ", filters=" + this.filters + "]";
	}

}
