package com.startupoxygen.craft.commands;

import com.startupoxygen.craft.metadata.Focus;

public class AbstractCommandProcessor implements CommandProcessor {

	public void validateCommand() {
		// TODO Auto-generated method stub

	}

	public void validateFocus() {
		// TODO Auto-generated method stub
	}

	public void validateCommandOptions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(CommandContext argCommandContext) {
		validateCommand();
		validateCommandOptions();
		validateFocus();
		execute(argCommandContext);
		changeFocus();
	}

	protected void execute(CommandContext argCommandContext) {
		// TODO Auto-generated method stub

	}

	public void changeFocus() {
		// TODO Auto-generated method stub

	}

	public Command getCommand(CommandContext argCommandContext) {
		return argCommandContext.getCommand();
	}

	public Focus getFocus(CommandContext argCommandContext) {
		return argCommandContext.getFocus();
	}

}
