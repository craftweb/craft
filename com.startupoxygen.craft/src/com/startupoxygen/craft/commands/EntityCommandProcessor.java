package com.startupoxygen.craft.commands;

import com.startupoxygen.craft.metadata.Entity;

public class EntityCommandProcessor extends AbstractCommandProcessor {
	private static final String ENTITY = "entity";

	public EntityCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = getCommand(argCommandContext);
		Entity entity = new Entity();
		entity.setProject(argCommandContext.getProject());
		entity.setName(command.getOptionValueByName("name"));
		entity.setMain(command.isOptionPresent("main"));
		argCommandContext.addEntity(entity);
	}
}
