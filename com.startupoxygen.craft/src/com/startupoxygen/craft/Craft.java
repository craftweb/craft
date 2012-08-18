package com.startupoxygen.craft;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mongodb.DBCollection;
import com.startupoxygen.craft.metadata.DataType;
import com.startupoxygen.craft.metadata.Entity;
import com.startupoxygen.craft.metadata.Field;
import com.startupoxygen.craft.metadata.Project;
import com.startupoxygen.craft.metadata.Relationship;
import com.startupoxygen.craft.metadata.RelationshipType;

public class Craft {
	private static final String SLASH = "/";
	public static final String PROJECT = "project";
	public static final String ENTITY = "entity";
	public static final String ACTION = "action";
	public static final String DATAMAP = "datamap";

	public static final String CRAFT_ENTITY_EDIT = "craft.entity.edit";
	public static final String CRAFT_ENTITY_VIEW = "craft.entity.view";
	public static final String CRAFT_ENTITY_NEW = "craft.entity.new";
	public static final String CRAFT_ENTITY_LIST = "craft.entity.list";
	public static final String CRAFT_MAIN = "craft.main";
	public static final String PAGE_BEAN = "pageBean";

	public Craft() {
		// Default Constructor
	}

	public Project getProjectByContextPath(String argProjectContextPath) {
		return CraftProjectCache.getInstance().getProjectByContextPath(
				argProjectContextPath);
	}

	public DBCollection getCollection(Project argProject, Entity argEntity) {
		return CraftProjectCache.getInstance().getCollection(argProject,
				argEntity);
	}

	public void insert(Project argProject, Entity argEntity,
			Map<String, String> dataMap) {
		CraftProjectCache.getInstance().insert(argProject, argEntity, dataMap);
	}

	public PageBean getPageBean(HttpServletRequest argRequest) {
		return (PageBean) argRequest.getAttribute(Craft.PAGE_BEAN);
	}

	public String getCraftContextPath(HttpServletRequest argRequest) {
		String craftContextPath = argRequest.getContextPath();
		return craftContextPath;
	}

	public String getProjectContextPath(HttpServletRequest argRequest) {
		PageBean pageBean = getPageBean(argRequest);
		Project project = pageBean.getProject();
		String craftContextPath = argRequest.getContextPath();
		String result = craftContextPath + SLASH + project.getContextPath();
		return result;
	}

	public String getEntityContextPath(HttpServletRequest argRequest) {
		PageBean pageBean = getPageBean(argRequest);
		Entity entity = pageBean.getEntity();
		String result = getProjectContextPath(argRequest) + SLASH
				+ entity.getNormalizedName();
		return result;
	}

	public String getEntityContextPath(HttpServletRequest argRequest,
			Entity argEntity) {
		String result = getProjectContextPath(argRequest) + SLASH
				+ argEntity.getNormalizedName();
		return result;
	}

	public String getHtmlInputType(Field argField) {
		if (argField != null) {
			DataType dataType = argField.getDataType();
			String datTypeName = dataType.getName();
			if (datTypeName != null) {
				String result = "";
				if (argField.isDropdown()) {
					result = result + "dropdown";
					if (argField.isAllowNew()) {
						result = result + "-" + "allownew";
					}
				} else if (argField instanceof Relationship) {
					Relationship relationship = (Relationship) argField;
					RelationshipType relationshipType = relationship
							.getRelationshipType();
					if (RelationshipType.manytoone == relationshipType) {
						return "lookup";
					}
				} else if ("url".equals(datTypeName)) {
					result = "url";
					if (argField.isAutocomplete()) {
						result = result + "-" + "autocomplete";
					}
				} else if ("email".equals(datTypeName)) {
					result = "email";
					if (argField.isAutocomplete()) {
						result = result + "-" + "autocomplete";
					}
				} else if ("date".equals(datTypeName)) {
					result = "date";
				} else if ("time".equals(datTypeName)) {
					result = "time";
				} else if ("number".equals(datTypeName)) {
					result = "number";
					if (argField.isAutocomplete()) {
						result = result + "-" + "autocomplete";
					}
				} else if ("text".equals(datTypeName)) {
					result = "textarea";
				} else {
					result = "text";
					if (argField.isAutocomplete()) {
						result = result + "-" + "autocomplete";
					}
				}
				return result;
			}
		}
		throw new IllegalArgumentException("argField cannot be null or empty");
	}

	public boolean isManyToManyOrOneToMany(Field argField) {
		boolean result = false;
		if (argField instanceof Relationship) {
			Relationship relationship = (Relationship) argField;
			result = RelationshipType.manytomany == relationship
					.getRelationshipType()
					|| RelationshipType.onetomany == relationship
							.getRelationshipType();
		}
		return result;
	}
}
