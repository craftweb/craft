package com.startupoxygen.craft.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class CommandProcessorChain implements CommandProcessor {
	private static final String EQUALS = "=";
	private Map<String, CommandProcessor> commandNameVscommandProcessorsMap;

	public CommandProcessorChain() {
		this.commandNameVscommandProcessorsMap = new LinkedHashMap<String, CommandProcessor>();
		this.initialize();
	}

	public void initialize() {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(this
					.getClass().getResourceAsStream("/commands.properties")));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				String commandName = StringUtils.substringBefore(line, EQUALS);
				commandName = commandName.trim();
				String commandProcessorClassName = StringUtils.substringAfter(
						line, EQUALS);
				commandProcessorClassName = commandProcessorClassName.trim();
				@SuppressWarnings("rawtypes")
				Class commandProcessorClass = Thread.currentThread()
						.getContextClassLoader()
						.loadClass(commandProcessorClassName);
				CommandProcessor commandProcessor = (CommandProcessor) commandProcessorClass
						.newInstance();
				this.register(commandName, commandProcessor);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception while initializing command processor chain", e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ignore) {
					// ignore
				}
			}
		}
	}

	public void register(String argCommandName,
			CommandProcessor argCommandProcessor) {
		this.commandNameVscommandProcessorsMap.put(argCommandName,
				argCommandProcessor);
	}

	@Override
	public void process(CommandContext argCommandContext) {
		Command command = argCommandContext.getCommand();
		CommandProcessor commandProcessor = this.commandNameVscommandProcessorsMap
				.get(command.getName());
		if (commandProcessor != null) {
			commandProcessor.process(argCommandContext);
		} else {
			System.out.println("no command processor registerd for command: "
					+ command);
		}
	}

}
