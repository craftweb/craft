package com.startupoxygen.craft.script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.startupoxygen.craft.commands.Command;
import com.startupoxygen.craft.commands.CommandContext;
import com.startupoxygen.craft.commands.CommandProcessorChain;
import com.startupoxygen.craft.commands.CommandRegistry;
import com.startupoxygen.craft.commands.Option;
import com.startupoxygen.craft.commands.Options;
import com.startupoxygen.craft.metadata.Project;

public class CraftScriptParser {
	private static final String SINGLE_LINE_COMMENT = "//";
	private static final String OPTION_MARKER = "--";
	private static final String SPACE = " ";

	private String fileName;
	private Project project;
	private CraftScript craftScript;

	public CraftScriptParser() {
		this.setProject(new Project());
		craftScript = new CraftScript();
	}

	public void parse() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(this.getFileName()));
			int lineNumber = 0;
			String line = null;
			while ((line = br.readLine()) != null) {
				++lineNumber;
				line = line.trim();

				if (line.isEmpty()) {
					// handle empty line
				} else if (line.startsWith(SINGLE_LINE_COMMENT)) {
					// handle comment line
				} else {
					String firstWord = StringUtils.substringBefore(line, SPACE);
					String command = null;
					command = firstWord;
					if (CommandRegistry.getInstance().isRegisterd(command)) {
						System.out.println("LINE: " + lineNumber + ": " + line);
						List<Option> options = new CraftScriptParserHelper()
								.tokenize(StringUtils.substringAfter(line,
										command).trim());
						System.out.println("COMMAND: " + command + " OPTIONS: "
								+ options);
						this.craftScript.addCommand(new Command(command,
								new Options(options)));
					} else {
						System.err
								.println("ERROR-7007: UNKNOWN COMMAND : LINE: "
										+ lineNumber + ": " + line);
						this.getProject().addIgnoredLine(line);
					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(
					"exception while reading metadata file: "
							+ this.getFileName(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception ignore) {
					// ignore
				}
			}
		}
		executeScript();
	}

	private void executeScript() {
		if (this.craftScript != null && this.craftScript.hasCommands()) {
			CommandProcessorChain commandProcessorChain = new CommandProcessorChain();
			commandProcessorChain.initialize();
			CommandContext commandContext = new CommandContext();
			List<Command> commands = this.craftScript.getCommands();
			for (Command com : commands) {
				commandContext.setCommand(com);
				commandProcessorChain.process(commandContext);
			}
			commandContext.getProject().setIgnoredLines(
					this.project.getIgnoredLines());
			this.project = commandContext.getProject();
		}
	}

	private boolean isCommand(String argFirstWord) {
		// TODO Auto-generated method stub
		return false;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project argProject) {
		this.project = argProject;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String argFileName) {
		this.fileName = argFileName;
	}

	public CraftScript getScript() {
		return this.craftScript;
	}

	public void setScript(CraftScript argCraftScript) {
		this.craftScript = argCraftScript;
	}

	@Override
	public String toString() {
		return "MetaDataParser [fileName=" + this.fileName + ", project="
				+ this.project + ", craftScript=" + this.craftScript + "]";
	}

}
