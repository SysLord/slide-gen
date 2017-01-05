package de.syslord.slidegen.editor.persisting;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.syslord.boxmodel.LayoutableBox;
import net.openhft.compiler.CompilerUtils;

public class JavaBoxImporter {

	public static JavaBoxImporter IMPORTER = new JavaBoxImporter();

	@SuppressWarnings("unchecked")
	public LayoutableBox load(String javaCode) {
		try {
			String packageName = extractString(javaCode, ".*package (\\S+);.*");
			String className = extractString(javaCode, ".*class (\\S+) implements java.util.function.Supplier<LayoutableBox>.*");
			String fullyQualClassName = packageName + "." + className;

			ThrowAwayClassLoader classLoader = new ThrowAwayClassLoader(getClass().getClassLoader());

			Class<Supplier<LayoutableBox>> clazz = CompilerUtils.COMPILER.loadFromJava(classLoader, fullyQualClassName, javaCode);

			Supplier<LayoutableBox> instance = clazz.newInstance();
			return instance.get();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private String extractString(String javaCode, String pattern) {
		Pattern compile = Pattern.compile(pattern, Pattern.DOTALL);
		Matcher matcher = compile.matcher(javaCode);

		boolean matches = matcher.matches();
		if (!matches || matcher.groupCount() < 1) {
			throw new RuntimeException();
		}
		return matcher.group(1);
	}

}
