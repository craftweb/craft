package com.startupoxygen.craft.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.WordUtils;

import com.startupoxygen.craft.metadata.layouts.Layout;

public class Entity {
	private String name;
	private String normalizedName;
	private String displayName;
	private String computedDisplayName;
	private boolean abstractEntity;
	private String extendsEntity;
	private Project project;
	private boolean main;
	private Map<String, Field> fieldNameVsFieldMap;
	private Map<String, Layout> layoutNameVsLayoutMap;

	public Entity() {
		super();
		this.fieldNameVsFieldMap = new LinkedHashMap<String, Field>();
		this.layoutNameVsLayoutMap = new LinkedHashMap<String, Layout>();
	}

	public boolean isMain() {
		return this.main;
	}

	public void setMain(boolean argMain) {
		this.main = argMain;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project argProject) {
		this.project = argProject;
	}

	public Entity(String argName) {
		super();
		this.name = argName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
		this.computedDisplayName = StringUtils.join(StringUtils
				.splitByCharacterTypeCamelCase(WordUtils.capitalize(argName)),
				" ");
		this.normalizedName = StringUtils.lowerCase(argName);
	}

	public Collection<Field> getFields() {
		return Collections.unmodifiableCollection(this.fieldNameVsFieldMap
				.values());
	}

	public void addField(Field argField) {
		this.fieldNameVsFieldMap.put(argField.getName(), argField);
		argField.setEntity(this);
	}

	public Field getFieldByName(String argFieldName) {
		return this.fieldNameVsFieldMap.get(argFieldName);
	}

	public Collection<Layout> getLayouts() {
		return Collections.unmodifiableCollection(this.layoutNameVsLayoutMap
				.values());
	}

	public void addLayout(Layout argLayout) {
		this.layoutNameVsLayoutMap.put(argLayout.getName(), argLayout);
	}

	public Layout getLayoutByName(String argLayoutName) {
		return this.layoutNameVsLayoutMap.get(argLayoutName);
	}

	public boolean isAbstractEntity() {
		return this.abstractEntity;
	}

	public void setAbstractEntity(boolean argAbstractEntity) {
		this.abstractEntity = argAbstractEntity;
	}

	public String getExtendsEntity() {
		return this.extendsEntity;
	}

	public void setExtendsEntity(String argExtendsEntity) {
		this.extendsEntity = argExtendsEntity;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getFullyQualifiedName() {
		return this.project.getFullyQualifiedName() + "_" + this.getName();
	}

	public Map<String, Field> getFullyQualifiedNameVsFieldMap(String argPrefix,
			boolean argRelationship) {
		if (this.getFields() != null && !this.getFields().isEmpty()) {
			Map<String, Field> fqnVsFieldMap = new LinkedHashMap<String, Field>();
			for (Field field : this.getFields()) {
				String fqn = argPrefix;
				if (!argRelationship) {
					fqn = fqn + "_" + this.getName();
				}
				fqn = fqn + "_" + field.getName();
				if (!field.isRelationship()) {
					fqnVsFieldMap.put(fqn, field);
				} else {
					Relationship relationship = (Relationship) field;
					if (relationship.getRelationshipType() == RelationshipType.onetoone) {
						Entity relatedEntity = this.getProject()
								.getEntityByName(
										relationship.getDataType().getName());
						Map<String, Field> relatedFqnVsFieldMap = relatedEntity
								.getFullyQualifiedNameVsFieldMap(fqn, true);
						fqnVsFieldMap.putAll(relatedFqnVsFieldMap);
					}
				}
			}
			return fqnVsFieldMap;
		}
		return null;
	}

	public String getDisplayName() {
		return displayName != null ? displayName : this
				.getComputedDisplayName();
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getComputedDisplayName() {
		return computedDisplayName;
	}

	public void setComputedDisplayName(String computedDisplayName) {
		this.computedDisplayName = computedDisplayName;
	}

	public String getNormalizedName() {
		return normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

}