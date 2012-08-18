package com.startupoxygen.craft;

import java.util.Collection;

import org.apache.commons.lang3.Validate;

public class Validations {
	private static final String NOT_NULL = "%s cannot be null";
	private static final String NOT_EMPTY = "%s cannot be empty";
	private static final String NOT_NULL_AND_NOT_EMPTY = "%s cannot be null or empty";

	public static void notNullNotEmptyNoNullElements(Object[] argObjects,
			String argName) {
		Validate.notNull(argObjects, NOT_NULL, argName);
		Validate.notEmpty(argObjects, NOT_EMPTY, argName);
		Validate.noNullElements(argObjects, NOT_NULL_AND_NOT_EMPTY, argName);
	}

	public static void notNullNoNullElements(Object[] argObjects, String argName) {
		Validate.notNull(argObjects, NOT_NULL, argName);
		Validate.noNullElements(argObjects, NOT_NULL_AND_NOT_EMPTY, argName);
	}

	public static void notNullNoNullElements(
			@SuppressWarnings("rawtypes") Iterable argObjects, String argName) {
		Validate.notNull(argObjects, NOT_NULL, argName);
		Validate.noNullElements(argObjects, NOT_NULL_AND_NOT_EMPTY, argName);
	}

	public static void notNullNotEmptyNoNullElements(
			@SuppressWarnings("rawtypes") Collection argCollection,
			String argName) {
		Validate.notNull(argCollection, NOT_NULL, argName);
		Validate.notEmpty(argCollection, NOT_EMPTY, argName);
		Validate.noNullElements(argCollection, NOT_NULL_AND_NOT_EMPTY, argName);
	}
}
