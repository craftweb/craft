package com.startupoxygen.craft;

import java.util.ArrayList;
import java.util.List;

import com.startupoxygen.craft.metadata.Entity;
import com.startupoxygen.craft.metadata.Project;

public class PageBean {
	private Project project;
	private Entity entity;
	private List<FieldSet> fieldSets;
	private List<Association> associations;

	public PageBean() {
		this.fieldSets = new ArrayList<FieldSet>();
		this.associations = new ArrayList<Association>();
	}

	public void addFieldSet(FieldSet argFieldSet) {
		this.getFieldSets().add(argFieldSet);
	}

	public void addAssociation(Association argAssociation) {
		this.getAssociations().add(argAssociation);
	}

	public List<FieldSet> getFieldSets() {
		return this.fieldSets;
	}

	public void setFieldSets(List<FieldSet> argFieldSets) {
		this.fieldSets = argFieldSets;
	}

	public List<Association> getAssociations() {
		return this.associations;
	}

	public void setAssociations(List<Association> argAssociations) {
		this.associations = argAssociations;
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

}
