package com.startupoxygen.craft.commands;


public class ColCommandProcessor extends AbstractCommandProcessor {
	private static final String ENTITY = "entity";

	public ColCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = getCommand(argCommandContext);
	}
}
