package com.startupoxygen.craft.commands;

import java.util.List;

public class Command {
	private String name;
	private boolean multiline;
	private boolean comment;
	private Options options;

	public Command() {
		super();
	}

	public Command(String argName, Options argOptions) {
		super();
		this.name = argName;
		this.options = argOptions;
	}

	public Command(String argName) {
		super();
		this.name = argName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public boolean isMultiline() {
		return this.multiline;
	}

	public void setMultiline(boolean argMultiline) {
		this.multiline = argMultiline;
	}

	public boolean isComment() {
		return this.comment;
	}

	public void setComment(boolean argComment) {
		this.comment = argComment;
	}

	public String getFirstOptionValue() {
		return this.options.getFirstOptionValue();
	}

	public String getSecondOptionValue() {
		return this.options.getSecondOptionValue();
	}

	public List<Option> getOptions() {
		return this.options.getOptions();
	}

	public void setOptions(List<Option> argOptions) {
		this.options.setOptions(argOptions);
	}

	public Option getOptionByName(String argString) {
		return this.options.getOptionByName(argString);
	}

	public String getOptionValueByName(String argString) {
		return this.options.getOptionValueByName(argString);
	}

	public List<String> getOptionValueListByName(String argString) {
		return this.options.getOptionValueListByName(argString);
	}

	@Override
	public String toString() {
		return "Command [name=" + this.name + ", multiline=" + this.multiline
				+ ", options=" + this.options + "]";
	}

	public boolean isOptionPresent(String argString) {
		return this.options.isOptionPresent(argString);
	}
}
