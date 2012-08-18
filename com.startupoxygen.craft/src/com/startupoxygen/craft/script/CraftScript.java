package com.startupoxygen.craft.script;

import java.util.ArrayList;
import java.util.List;

import com.startupoxygen.craft.commands.Command;
import com.startupoxygen.craft.metadata.Focus;

public class CraftScript {
	private Focus focus;
	private List<Command> commands;

	public CraftScript() {
		super();
		this.commands = new ArrayList<Command>();
	}

	public Focus getFocus() {
		return this.focus;
	}

	public void setFocus(Focus argFocus) {
		this.focus = argFocus;
	}

	public List<Command> getCommands() {
		return this.commands;
	}

	public void setCommands(List<Command> argCommands) {
		this.commands = argCommands;
	}

	public void addCommand(Command argCommand) {
		this.commands.add(argCommand);
	}

	@Override
	public String toString() {
		return "CraftScript [focus=" + this.focus + ", commands=" + this.commands
				+ "]";
	}

	public boolean hasCommands() {
		return !this.getCommands().isEmpty();
	}

}
