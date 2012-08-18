package com.startupoxygen.craft.commands;

import com.startupoxygen.craft.metadata.Entity;
import com.startupoxygen.craft.metadata.Field;
import com.startupoxygen.craft.metadata.Focus;
import com.startupoxygen.craft.metadata.Project;
import com.startupoxygen.craft.metadata.security.Grant;
import com.startupoxygen.craft.metadata.security.Role;

public class CommandContext {
	private Focus focus;
	private Command command;
	private Project project;

	public CommandContext() {
		super();
		this.focus = new Focus();
	}

	public Focus getFocus() {
		return this.focus;
	}

	public void setFocus(Focus argFocus) {
		this.focus = argFocus;
	}

	public Command getCommand() {
		return this.command;
	}

	public void setCommand(Command argCommand) {
		this.command = argCommand;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project argProject) {
		this.project = argProject;
		this.getFocus().setProject(this.project);
	}

	public void addEntity(Entity argEntity) {
		this.project.addEntity(argEntity);
		this.getFocus().setEntity(argEntity);
	}

	public void addField(Field argField) {
		Entity entity = this.getFocus().getEntity();
		if (entity != null) {
			entity.addField(argField);
		} else {
			throw new RuntimeException(
					"No entity found in context. Field can be added only when an entity is in context. Currnet focus: "
							+ this.getFocus());
		}
	}

	public void addRole(Role argRole) {
		this.project.addRole(argRole);
		this.getFocus().setRole(argRole);
	}

	public void addGrant(Grant argGrant) {
		Role role = this.getFocus().getRole();
		if (role != null) {
			role.addGrant(argGrant);
		} else {
			throw new RuntimeException(
					"No entity found in context. Field can be added only when an entity is in context. Currnet focus: "
							+ this.getFocus());
		}
	}

	public Entity getEntityByName(String argEntityName) {
		Entity entity = this.getProject().getEntityByName(argEntityName);
		if (entity != null) {
			return entity;
		} else {
			throw new RuntimeException("could not find entity: "
					+ argEntityName);
		}
	}
}
