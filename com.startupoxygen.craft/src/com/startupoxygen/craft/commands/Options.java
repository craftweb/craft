package com.startupoxygen.craft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.startupoxygen.craft.Validations;

public class Options {
	private static final int FIRST = 0;
	private static final int SECOND = 1;
	private List<Option> options;

	public Options() {
		super();
	}

	public Options(List<Option> argOptions) {
		Validations.notNullNoNullElements(argOptions, "argOptions");
		this.options = new ArrayList<Option>();
		this.options.addAll(argOptions);
	}

	public Options(Option... argOptions) {
		Validations.notNullNoNullElements(argOptions, "argOptions");
		this.options = Arrays.asList(argOptions);
	}

	public String getFirstOptionValue() {
		if (this.getOptions().size() >= 1) {
			Option option = this.getOptions().get(FIRST);
			String value = option.getValue();
			return value;
		} else {
			throw new RuntimeException("no such option available.");
		}
	}

	public String getSecondOptionValue() {
		if (this.getOptions().size() >= 2) {
			Option option = this.getOptions().get(SECOND);
			String value = option.getValue();
			return value;
		} else {
			throw new RuntimeException("no such option available.");
		}
	}

	public List<Option> getOptions() {
		return this.options;
	}

	public void setOptions(List<Option> argOptions) {
		this.options = argOptions;
	}

	public Option getOptionByName(String argString) {
		for (Option option : this.options) {
			if (option != null && option.getName().equals(argString)) {
				return option;
			}
		}
		return null;
	}

	public String getOptionValueByName(String argString) {
		for (Option option : this.options) {
			if (option != null && option.getName().equals(argString)) {
				return option.getValue();
			}
		}
		return null;
	}

	public List<String> getOptionValueListByName(String argString) {
		List<String> valueList = new ArrayList<String>();
		for (Option option : this.options) {
			if (option != null && option.getName().equals(argString)) {
				valueList.add(option.getValue());
			}
		}
		return valueList;
	}

	public boolean isOptionPresent(String argString) {
		return getOptionByName(argString) != null;
	}
}
