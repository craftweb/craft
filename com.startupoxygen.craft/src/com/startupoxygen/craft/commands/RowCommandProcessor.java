package com.startupoxygen.craft.commands;


public class RowCommandProcessor extends AbstractCommandProcessor {
	private static final String ENTITY = "entity";

	public RowCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = getCommand(argCommandContext);
	}
}
