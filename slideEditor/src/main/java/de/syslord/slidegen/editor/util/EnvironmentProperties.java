package de.syslord.slidegen.editor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentProperties {

	@Autowired
	private Environment environment;

	public String getProperty(String format, String... placeholderReplacements) {
		Object[] args = placeholderReplacements;
		String propertyName = String.format(format, args);

		return environment.getProperty(propertyName);
	}

}
