package com.startupoxygen.craft;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import com.startupoxygen.craft.CraftScriptParser;
import com.startupoxygen.craft.metadata.Entity;
import com.startupoxygen.craft.metadata.Field;
import com.startupoxygen.craft.metadata.Project;

public class CraftScriptParserTest extends CraftScriptParser {

	@Test
	public void testParse() {
		String fileName = "./test/resources/optimaze.craft";
		CraftScriptParser craftScriptParser = new CraftScriptParser();
		craftScriptParser.setFileName(fileName);
		craftScriptParser.parse();
		Project project = craftScriptParser.getProject();
		Entity entity = project.getEntityByName("Candidate");
		Map<String, Field> fqnVsFieldMap = entity
				.getFullyQualifiedNameVsFieldMap(project.getTopLevelPackage(),
						false);
		Assert.assertNotNull(fqnVsFieldMap);
		Set<Entry<String, Field>> entrySet = fqnVsFieldMap.entrySet();
		System.out.println("FQN Field Names for entity: "
				+ entity.getFullyQualifiedName() + ":");
		for (Entry<String, Field> entry : entrySet) {
			System.out.println(entry.getKey());
		}
		// System.out.println(project);
		// if (project != null && project.getIgnoredLines() != null
		// && !project.getIgnoredLines().isEmpty()) {
		// for (String ignoredLine : project.getIgnoredLines()) {
		// System.out.println("IGNORED: " + ignoredLine);
		// }
		// fail("CraftScriptParser is ignoring some lines.");
		// }
	}

}
