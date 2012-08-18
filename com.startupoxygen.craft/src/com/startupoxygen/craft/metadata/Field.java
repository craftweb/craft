package com.startupoxygen.craft.metadata;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.WordUtils;

import com.startupoxygen.craft.validation.Validator;

public class Field {
	private String name;
	private String displayName;
	private DataType dataType;
	private boolean required;
	private int minSize;
	private int maxSize;
	private String formula;
	private boolean computed;
	private boolean relationship;
	private boolean nameField;
	private boolean summaryField;
	private boolean dropdown;
	private boolean autocomplete;
	private boolean allowNew;
	private Entity entity;
	private List<Validator> validators;
	private String computedDisplayName;

	public Field() {
		super();
	}

	public Field(String argName, DataType argDataType) {
		super();
		this.name = argName;
		this.dataType = argDataType;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity argEntity) {
		this.entity = argEntity;
	}

	public boolean isRelationship() {
		return this.relationship;
	}

	public void setRelationship(boolean argRelationship) {
		this.relationship = argRelationship;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
		this.computedDisplayName = StringUtils.join(StringUtils
				.splitByCharacterTypeCamelCase(WordUtils.capitalize(argName)),
				" ");
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType argDataType) {
		this.dataType = argDataType;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(boolean argRequired) {
		this.required = argRequired;
	}

	public int getMinSize() {
		return this.minSize;
	}

	public void setMinSize(int argSizeMin) {
		this.minSize = argSizeMin;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public void setMaxSize(int argSizeMax) {
		this.maxSize = argSizeMax;
	}

	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String argFormula) {
		this.formula = argFormula;
	}

	public boolean isComputed() {
		return this.computed;
	}

	public void setComputed(boolean argComputed) {
		this.computed = argComputed;
	}

	public List<Validator> getValidators() {
		return this.validators;
	}

	public void setValidators(List<Validator> argValidators) {
		this.validators = argValidators;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getFullyQualifiedName() {
		return this.entity.getFullyQualifiedName() + "_" + this.getName();
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

	public boolean isNameField() {
		return this.nameField;
	}

	public void setNameField(boolean argNameField) {
		this.nameField = argNameField;
	}

	public boolean isSummaryField() {
		return this.summaryField;
	}

	public void setSummaryField(boolean argSummaryField) {
		this.summaryField = argSummaryField;
	}

	public boolean isDropdown() {
		return this.dropdown;
	}

	public void setDropdown(boolean argDropdown) {
		this.dropdown = argDropdown;
	}

	public boolean isAutocomplete() {
		return this.autocomplete;
	}

	public void setAutocomplete(boolean argAutocomplete) {
		this.autocomplete = argAutocomplete;
	}

	public boolean isAllowNew() {
		return this.allowNew;
	}

	public void setAllowNew(boolean argAllowNew) {
		this.allowNew = argAllowNew;
	}

}
