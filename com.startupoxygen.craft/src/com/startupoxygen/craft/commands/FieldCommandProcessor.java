package com.startupoxygen.craft.commands;

import org.apache.commons.lang3.math.NumberUtils;

import com.startupoxygen.craft.metadata.DataTypeFactory;
import com.startupoxygen.craft.metadata.Field;

public class FieldCommandProcessor extends AbstractCommandProcessor {
	private static final String FIELD = "field";

	public FieldCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = getCommand(argCommandContext);
		Field field = new Field();
		field.setDataType(DataTypeFactory.fromName(command
				.getOptionValueByName("type")));
		field.setName(command.getOptionValueByName("name"));
		field.setComputed(command.isOptionPresent("computed"));
		field.setNameField(command.isOptionPresent("nameField"));
		field.setSummaryField(command.isOptionPresent("summaryField"));
		field.setDropdown(command.isOptionPresent("dropdown"));
		field.setAutocomplete(command.isOptionPresent("autocomplete"));
		field.setAllowNew(command.isOptionPresent("allowNew"));
		field.setFormula(command.getOptionValueByName("computed"));
		field.setMinSize(NumberUtils.toInt(command
				.getOptionValueByName("minsize")));
		field.setMaxSize(NumberUtils.toInt(command
				.getOptionValueByName("maxsize")));
		field.setRequired(command.isOptionPresent("required"));
		argCommandContext.addField(field);
	}
}
