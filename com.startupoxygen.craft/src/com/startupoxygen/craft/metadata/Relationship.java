package com.startupoxygen.craft.metadata;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Relationship extends Field {
	private RelationshipType relationshipType;

	public Relationship() {
		super();
		super.setRelationship(true);
	}

	public Relationship(String argName, DataType argDataType) {
		super(argName, argDataType);
	}

	public RelationshipType getRelationshipType() {
		return this.relationshipType;
	}

	public void setRelationshipType(RelationshipType argRelationshipType) {
		this.relationshipType = argRelationshipType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getFullyQualifiedName() {
		return super.getEntity().getFullyQualifiedName() + "_" + this.getName();
	}

}
