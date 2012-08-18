package com.startupoxygen.craft;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.startupoxygen.craft.commands.Option;

public class CraftScriptParserHelper {
	private List<Option> results;
	private StringBuilder currentOption;
	private StringBuilder currentValue;

	public CraftScriptParserHelper() {
		this.results = new ArrayList<Option>();
		this.currentOption = new StringBuilder();
		this.currentValue = new StringBuilder();
	}

	private void store() {
		if (this.currentOption.length() > 0) {
			String option = this.currentOption.toString();
			this.results.add(new Option(option, this.currentValue.toString()));
		} else {
			this.results.add(new Option("", this.currentValue.toString()));
		}
	}

	public List<Option> tokenize(final String remainingBuffer) {
		Validate.notNull(remainingBuffer,
				"Remaining buffer cannot be null, although it can be empty");
		boolean inQuotes = false;
		// Verify correct number of double quotes are present
		int count = 0;
		for (final char c : remainingBuffer.toCharArray()) {
			if ('"' == c) {
				count++;
			}
		}
		Validate.isTrue(count % 2 == 0,
				"Cannot have an unbalanced number of quotation marks");

		if ("".equals(remainingBuffer.trim())) {
			// They've not specified anything, so exit now
			return results;
		}

		final String[] split = remainingBuffer.split(" ");
		for (int i = 0; i < split.length; i++) {
			final String currentToken = split[i];

			if (currentToken.startsWith("\"") && currentToken.endsWith("\"")
					&& currentToken.length() > 1) {
				final String tokenLessDelimiters = currentToken.substring(1,
						currentToken.length() - 1);
				this.currentValue.append(tokenLessDelimiters);

				// Store this token
				store();
				this.currentOption = new StringBuilder();
				this.currentValue = new StringBuilder();
				continue;
			}

			if (inQuotes) {
				// We're only interested in this token series ending
				if (currentToken.endsWith("\"")) {
					final String tokenLessDelimiters = currentToken.substring(
							0, currentToken.length() - 1);
					currentValue.append(" ").append(tokenLessDelimiters);
					inQuotes = false;

					// Store this now-ended token series
					store();
					currentOption = new StringBuilder();
					currentValue = new StringBuilder();
				} else {
					// The current token series has not ended
					currentValue.append(" ").append(currentToken);
				}
				continue;
			}

			if (currentToken.startsWith("\"")) {
				// We're about to start a new delimited token
				final String tokenLessDelimiters = currentToken.substring(1);
				currentValue.append(tokenLessDelimiters);
				inQuotes = true;
				continue;
			}

			if (currentToken.trim().equals("")) {
				// It's simply empty, so ignore it (ROO-23)
				continue;
			}

			if (currentToken.startsWith("--")) {
				// We're about to start a new option marker
				// First strip all of the - or -- or however many there are
				final int lastIndex = currentToken.lastIndexOf("-");
				final String tokenLessDelimiters = currentToken
						.substring(lastIndex + 1);
				this.currentOption.append(tokenLessDelimiters);

				// Store this token if it's the last one, or the next token
				// starts with a "-"
				if (i + 1 == split.length) {
					// We're at the end of the tokens, so store this one and
					// stop processing
					store();
					break;
				}

				if (split[i + 1].startsWith("-")) {
					// A new token is being started next iteration, so store
					// this one now
					store();
					this.currentOption = new StringBuilder();
					this.currentValue = new StringBuilder();
				}

				continue;
			}

			// We must be in a standard token

			// If the standard token has no option name, we allow it to contain
			// unquoted spaces
			if (this.currentOption.length() == 0) {
				if (this.currentValue.length() > 0) {
					// Existing content, so add a space first
					this.currentValue.append(" ");
				}
				this.currentValue.append(currentToken);

				// Store this token if it's the last one, or the next token
				// starts with a "-"
				if (i + 1 == split.length) {
					// We're at the end of the tokens, so store this one and
					// stop processing
					store();
					break;
				}

				if (split[i + 1].startsWith("--")) {
					// A new token is being started next iteration, so store
					// this one now
					store();
					this.currentOption = new StringBuilder();
					this.currentValue = new StringBuilder();
				}

				continue;
			}

			// This is an ordinary token, so store it now
			this.currentValue.append(currentToken);
			store();
			this.currentOption = new StringBuilder();
			this.currentValue = new StringBuilder();
		}

		// // Strip out an empty default option, if it was returned (ROO-379)
		// if (results.containsKey("") && results.get("").trim().equals("")) {
		// results.remove("");
		// }

		return results;
	}

}
