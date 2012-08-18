package com.startupoxygen.craft.metadata;

import com.startupoxygen.craft.metadata.security.Role;

public class Focus {
	private Project project;
	private Entity entity;
	private Field field;
	private Role role;

	public Focus() {
		super();
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project argProject) {
		this.project = argProject;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity argEntity) {
		this.entity = argEntity;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field argField) {
		this.field = argField;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role argRole) {
		this.role = argRole;
	}

	@Override
	public String toString() {
		return "Focus [project=" + this.project + ", entity=" + this.entity
				+ ", field=" + this.field + ", role=" + this.role + "]";
	}

}
