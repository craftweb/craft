package com.startupoxygen.craft.metadata.security;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	private String condition;

	public static List<Filter> newFilterList(List<String> argConditions) {
		if (argConditions != null) {
			List<Filter> filters = new ArrayList<Filter>();
			for (String condition : argConditions) {
				filters.add(new Filter(condition));
			}
			return filters;
		}
		return null;
	}

	public Filter() {
		super();
	}

	public Filter(String argCondition) {
		super();
		this.condition = argCondition;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String argCondition) {
		this.condition = argCondition;
	}

	@Override
	public String toString() {
		return "Filter [condition=" + this.condition + "]";
	}

}
