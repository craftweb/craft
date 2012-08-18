package com.startupoxygen.craft.metadata;

public class DataTypeFactory {
	public static DataType fromName(String argDataTypeName) {
		if (DataTypes.STRING.equals(argDataTypeName)) {
			return new DataType(DataTypes.STRING, true);
		} else {
			return new DataType(argDataTypeName, false);
		}
	}
}
