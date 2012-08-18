package com.startupoxygen.craft.commands;


public class ForEachCommandProcessor extends AbstractCommandProcessor {
	private static final String ENTITY = "entity";

	public ForEachCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = getCommand(argCommandContext);
	}
}
