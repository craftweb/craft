package com.startupoxygen.craft.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.startupoxygen.craft.metadata.security.Role;

public class Project {
	private String name;
	private String description;
	private String contextPath;
	private String db;
	private String topLevelPackage;
	private String version;
	private List<String> ignoredLines;
	private Map<String, Entity> entityNormalizedNameVsEntityMap;
	private Map<String, Role> roleNameVsRoleMap;

	public Project() {
		this.setIgnoredLines(new ArrayList<String>());
		this.entityNormalizedNameVsEntityMap = new LinkedHashMap<String, Entity>();
		this.roleNameVsRoleMap = new LinkedHashMap<String, Role>();
	}

	public void addEntity(Entity argEntity) {
		if (argEntity != null) {
			if (argEntity.getName() != null && !argEntity.getName().isEmpty()) {
				this.entityNormalizedNameVsEntityMap.put(
						StringUtils.lowerCase(argEntity.getName().trim()),
						argEntity);
			}
		}
	}

	public Collection<Entity> getEntities() {
		return Collections
				.unmodifiableCollection(this.entityNormalizedNameVsEntityMap
						.values());
	}

	public void addRole(Role argRole) {
		if (argRole != null) {
			if (argRole.getName() != null && !argRole.getName().isEmpty()) {
				this.roleNameVsRoleMap.put(argRole.getName().trim(), argRole);
			}
		}
	}

	public Collection<Role> getRoles() {
		return Collections.unmodifiableCollection(this.roleNameVsRoleMap
				.values());
	}

	public void addIgnoredLine(String argLine) {
		this.getIgnoredLines().add(argLine);
	}

	public List<String> getIgnoredLines() {
		return ignoredLines;
	}

	public void setIgnoredLines(List<String> argIgnoredLines) {
		this.ignoredLines = argIgnoredLines;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public String getTopLevelPackage() {
		return this.topLevelPackage;
	}

	public void setTopLevelPackage(String argTopLevelPackage) {
		this.topLevelPackage = argTopLevelPackage;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String argVersion) {
		this.version = argVersion;
	}

	public Entity getEntityByName(String argEntityName) {
		Entity entity = this.entityNormalizedNameVsEntityMap.get(StringUtils
				.lowerCase(argEntityName));
		return entity;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getFullyQualifiedName() {
		return this.getTopLevelPackage();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

}
