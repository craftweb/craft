package com.startupoxygen.craft.metadata.layouts;

import java.util.LinkedHashMap;
import java.util.Map;

public class Layout {
	private String name;
	private boolean newPage;
	private boolean editPage;
	private boolean retrieve;
	private String entity;
	private Map<String, Section> sectionNameVsSectionMap;

	public Layout() {
		super();
		this.sectionNameVsSectionMap = new LinkedHashMap<String, Section>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public boolean isRetrieve() {
		return this.retrieve;
	}

	public void setRetrieve(boolean argRetrieve) {
		this.retrieve = argRetrieve;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String argEntity) {
		this.entity = argEntity;
	}

	public boolean isNewPage() {
		return this.newPage;
	}

	public void setNewPage(boolean argNewPage) {
		this.newPage = argNewPage;
	}

	public boolean isEditPage() {
		return this.editPage;
	}

	public void setEditPage(boolean argEditPage) {
		this.editPage = argEditPage;
	}

}
