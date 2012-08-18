package com.startupoxygen.craft.commands;

import com.startupoxygen.craft.metadata.DataTypeFactory;
import com.startupoxygen.craft.metadata.Relationship;
import com.startupoxygen.craft.metadata.RelationshipType;

public class RelationshipCommandProcessor extends AbstractCommandProcessor {
	private static final String FIELD = "field";

	public RelationshipCommandProcessor() {
		super();
	}

	@Override
	public void execute(CommandContext argCommandContext) {
		Command command = getCommand(argCommandContext);
		Relationship relationship = new Relationship();
		relationship.setRelationshipType(RelationshipType.valueOf(command.getName()));
		relationship.setDataType(DataTypeFactory.fromName(command
				.getOptionValueByName("type")));
		relationship.setName(command.getOptionValueByName("name"));
		relationship.setRequired(command.isOptionPresent("required"));
		argCommandContext.addField(relationship);
	}
}
