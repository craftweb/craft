package com.startupoxygen.craft;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.startupoxygen.craft.Craft;
import com.startupoxygen.craft.PageBean;
import com.startupoxygen.craft.metadata.Entity;
import com.startupoxygen.craft.metadata.Field;
import com.startupoxygen.craft.metadata.Project;
import com.startupoxygen.craft.metadata.Relationship;

@Controller
@RequestMapping(value = "/")
public class CraftController {

	@RequestMapping(value = "{argProjectContextPath}/testlayout", method = RequestMethod.GET)
	public String displayLayout(Model argModel,
			@PathVariable String argProjectContextPath,
			HttpServletRequest argRequest, HttpServletResponse argResponse) {
		Project project = new Craft()
				.getProjectByContextPath(argProjectContextPath);
		argModel.addAttribute(Craft.PROJECT, project);
		return Craft.CRAFT_MAIN;
	}

	private PageBean createPageBean(String argProjectContextPath,
			String argEntityName) {
		PageBean pageBean = new PageBean();
		Project project = new Craft().getProjectByContextPath(
				argProjectContextPath);
		pageBean.setProject(project);
		Entity entity = project.getEntityByName(argEntityName);
		pageBean.setEntity(entity);
		return pageBean;
	}

	@RequestMapping(value = "{argProjectContextPath}/{argEntityName}", method = RequestMethod.GET)
	public String listEntity(Model argModel,
			@PathVariable String argProjectContextPath,
			@PathVariable String argEntityName, HttpServletRequest argRequest,
			HttpServletResponse argResponse) {
		PageBean pageBean = createPageBean(argProjectContextPath, argEntityName);
		argModel.addAttribute(Craft.PAGE_BEAN, pageBean);

		DBCollection db = new Craft().getCollection(
				pageBean.getProject(), pageBean.getEntity());
		DBCursor dbCursor = db.find();
		Iterator<DBObject> dbObjectIterator = dbCursor.iterator();
		Set<String> headers = new LinkedHashSet<String>();
		while (dbObjectIterator.hasNext()) {
			DBObject dbObject = dbObjectIterator.next();
			Map<String, Object> map = dbObject.toMap();
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (key != null && !key.equals("_class")) {
					Field field = pageBean.getEntity().getFieldByName(key);
					if (field != null && !(field instanceof Relationship)) {
						String fieldDisplayName = field.getDisplayName();
						headers.add(fieldDisplayName);
					}
				}
			}
		}
		argModel.addAttribute("headers", headers);
		argModel.addAttribute("summary", dbCursor);
		argModel.addAttribute("collectionName", Craft.DATAMAP);
		return Craft.CRAFT_ENTITY_LIST;
	}

	@RequestMapping(value = "{argProjectContextPath}/{argEntityName}/new", method = RequestMethod.GET)
	public String newEntity(Model argModel,
			@PathVariable String argProjectContextPath,
			@PathVariable String argEntityName, HttpServletRequest argRequest,
			HttpServletResponse argResponse) {
		PageBean pageBean = createPageBean(argProjectContextPath, argEntityName);
		argModel.addAttribute(Craft.PAGE_BEAN, pageBean);
		return Craft.CRAFT_ENTITY_NEW;
	}

	@RequestMapping(value = "{argProjectContextPath}/{argEntityName}/save", method = RequestMethod.POST)
	public String saveEntity(Model argModel,
			@PathVariable String argProjectContextPath,
			@PathVariable String argEntityName, HttpServletRequest argRequest,
			HttpServletResponse argResponse) {
		PageBean pageBean = createPageBean(argProjectContextPath, argEntityName);
		argModel.addAttribute(Craft.PAGE_BEAN, pageBean);
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		Collection<Field> fields = pageBean.getEntity().getFields();
		for (Field field : fields) {
			String name = field.getName();
			String[] values = argRequest.getParameterMap().get(name);
			String value = null;
			if (values != null && values.length >= 1) {
				value = values[0];
			}
			dataMap.put(name, value);
		}
		new Craft().insert(pageBean.getProject(), pageBean.getEntity(),
				dataMap);
		argModel.addAttribute(Craft.DATAMAP, dataMap);
		return Craft.CRAFT_ENTITY_VIEW;
	}

	@RequestMapping(value = "{argProjectContextPath}/{argEntityName}/view", method = RequestMethod.POST)
	public String viewEntity(Model argModel,
			@PathVariable String argProjectContextPath,
			@PathVariable String argEntityName, HttpServletRequest argRequest,
			HttpServletResponse argResponse) {
		PageBean pageBean = createPageBean(argProjectContextPath, argEntityName);
		argModel.addAttribute(Craft.PAGE_BEAN, pageBean);
		Map<String, String> resultedMap = new LinkedHashMap<String, String>();
		Collection<Field> fields = pageBean.getEntity().getFields();
		for (Field field : fields) {
			String name = field.getName();
			String[] values = argRequest.getParameterMap().get(name);
			String value = null;
			if (values != null && values.length >= 1) {
				value = values[0];
			}
			resultedMap.put(name, value);
		}
		new Craft().insert(pageBean.getProject(), pageBean.getEntity(),
				resultedMap);
		argModel.addAttribute(Craft.DATAMAP, resultedMap);
		return Craft.CRAFT_ENTITY_VIEW;
	}

	@RequestMapping(value = "{argProjectContextPath}/{argEntityName}/edit", method = RequestMethod.GET)
	public String editEntity(Model argModel,
			@PathVariable String argProjectContextPath,
			@PathVariable String argEntityName, HttpServletRequest argRequest,
			HttpServletResponse argResponse) {
		return Craft.CRAFT_ENTITY_EDIT;
	}

	@RequestMapping(value = "{argProjectContextPath}/{argEntityName}/delete", method = RequestMethod.POST)
	public String deleteEntity(Model argModel,
			@PathVariable String argProjectContextPath,
			@PathVariable String argEntityName, HttpServletRequest argRequest,
			HttpServletResponse argResponse) {
		return Craft.CRAFT_ENTITY_LIST;
	}
}
