package com.startupoxygen.craft;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.startupoxygen.craft.metadata.Entity;
import com.startupoxygen.craft.metadata.Project;

public class CraftProjectCache {
	public static final int CRAFT_DB_PORT = 27017;
	public static final String CRAFT_DB_HOST = "localhost";
	public static final String CRAFT = "craft";
	public static final String CRAFT_EXT = ".craft";

	private static final CraftProjectCache instance = new CraftProjectCache();
	private Map<String, Project> projectContextPathVsProjectMap;
	private Map<String, MongoTemplate> projectContextPathVsMongoTemplateMap;
	private static Mongo mongo;

	public static CraftProjectCache getInstance() {
		return instance;
	}

	private CraftProjectCache() {
		this.projectContextPathVsProjectMap = new LinkedHashMap<String, Project>();
		this.projectContextPathVsMongoTemplateMap = new LinkedHashMap<String, MongoTemplate>();
		try {
			mongo = new Mongo(CRAFT_DB_HOST, CRAFT_DB_PORT);
		} catch (Exception e) {
			throw new RuntimeException(
					"exception while initializing mongo template", e);
		}
		String craftProjectsDirectoryName = "F:/devhome/workspace/optaamaze/com.startupoxygen.craft/conf/projects";
		File craftProjectsDirectory = new File(craftProjectsDirectoryName);
		if (craftProjectsDirectory != null && craftProjectsDirectory.exists()
				&& craftProjectsDirectory.isDirectory()) {
			File[] craftFiles = craftProjectsDirectory.listFiles();
			for (File craftFile : craftFiles) {
				if (craftFile.getAbsolutePath().endsWith(CRAFT_EXT)) {
					String craftFileName = craftFile.getAbsolutePath();
					CraftScriptParser mdp = new CraftScriptParser();
					mdp.setFileName(craftFileName);
					mdp.parse();
					Project project = mdp.getProject();
					System.out.println(mdp);
					if (project != null && project.getIgnoredLines() != null
							&& !project.getIgnoredLines().isEmpty()) {
						for (String ignoredLine : project.getIgnoredLines()) {
							System.out.println("IGNORED: " + ignoredLine);
						}
					}
					this.projectContextPathVsProjectMap.put(
							project.getContextPath(), project);
					MongoTemplate mongoTemplate;
					try {
						System.err.println("database=" + project.getDb());
						mongoTemplate = new MongoTemplate(mongo,
								project.getDb());
					} catch (Exception e) {
						throw new RuntimeException(
								"exception while initializing mongo template",
								e);
					}
					this.projectContextPathVsMongoTemplateMap.put(
							project.getContextPath(), mongoTemplate);
				}
			}
		}
	}

	public Project getProjectByContextPath(String argProjectContextPath) {
		return this.projectContextPathVsProjectMap.get(argProjectContextPath);
	}

	public DBCollection getCollection(Project argProject, Entity argEntity) {
		MongoTemplate mongoTemplate = this.projectContextPathVsMongoTemplateMap
				.get(argProject.getContextPath());
		DBCollection collection = mongoTemplate.getCollection(argEntity
				.getName());
		return collection;
	}

	public void insert(Project argProject, Entity argEntity,
			Map<String, String> dataMap) {
		MongoTemplate mongoTemplate = this.projectContextPathVsMongoTemplateMap
				.get(argProject.getContextPath());
		mongoTemplate.insert(dataMap, argEntity.getName());
	}

}
