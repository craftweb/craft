package com.startupoxygen.craft.commands;

import com.startupoxygen.craft.metadata.Project;

public class ProjectCommandProcessor extends AbstractCommandProcessor {
	private static final String PROJECT = "project";

	public ProjectCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = super.getCommand(argCommandContext);
		Project project = new Project();
		project.setName(command.getFirstOptionValue());
		project.setDescription(command.getOptionValueByName("description"));
		project.setContextPath(command.getOptionValueByName("contextPath"));
		project.setDb(command.getOptionValueByName("db"));
		project.setTopLevelPackage(command
				.getOptionValueByName("topLevelPackage"));
		project.setVersion(command.getOptionValueByName("version"));
		argCommandContext.setProject(project);
	}
}
