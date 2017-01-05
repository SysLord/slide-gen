package de.syslord.slidegen.editor.persisting;

import java.util.Map;

import com.google.gwt.thirdparty.guava.common.collect.Maps;

import de.syslord.boxmodel.LayoutableBox;

// This is not to be taken very seriously
/*
 * Only LayoutableBox is supported, no other types yet. So this is incomplete.
 */
public class JavaCodeExporter {

	public String export(LayoutableBox root, String className, String packageName) {
		Map<LayoutableBox, String> variables = Maps.newHashMap();

		CodeBuilder builder = new CodeBuilder();
		builder.append("package " + packageName + ";");
		builder.append("import de.syslord.boxmodel.*;");
		builder.append("public class " + className + " implements java.util.function.Supplier<LayoutableBox>{");
		builder.indent();

		createMethod(root, variables, builder);

		builder.unIndent();
		builder.append("}");

		return builder.toString();
	}

	private void createMethod(LayoutableBox root, Map<LayoutableBox, String> variables, CodeBuilder builder) {

		builder.append("@Override");
		builder.append("public LayoutableBox get() {");
		builder.indent();

		generateMethodCode(root, variables, builder);

		builder.append(createReturn(variables, root));

		builder.unIndent();
		builder.append("}");
	}

	private String createReturn(Map<LayoutableBox, String> variables, LayoutableBox variable) {
		return String.format("return %s;", variables.get(variable));
	}

	private void generateMethodCode(LayoutableBox root, Map<LayoutableBox, String> variables, CodeBuilder builder) {
		generateBoxCode(root, null, variables, builder);

		root.streamFlatWithParents()
			.forEach(x -> generateBoxCode(x.getChild(), x.getParent(), variables, builder));
	}

	private void generateBoxCode(LayoutableBox box, LayoutableBox parent, Map<LayoutableBox, String> variables,
			CodeBuilder builder) {
		String variableName = createVariableName(box, variables);

		builder.append(createInstantiation(box, variableName));
		appendProperties(box, variableName, builder);

		if (parent != null) {
			String parentVariableName = variables.get(parent);
			builder.append(createAddChildCode(variableName, parentVariableName));
		}
		builder.append();
	}

	protected String createVariableName(LayoutableBox box, Map<LayoutableBox, String> variables) {
		String name = box.getName();
		String sane = name.replaceAll("[^A-Za-z0-9_-]", "_");
		if (!sane.matches("^[A-Za-z].*")) {
			sane = "box" + sane;
		}

		String numbered = sane;
		int number = 0;
		while (variables.containsValue(numbered)) {
			numbered = String.format("%s%d", sane, number);
			number++;
		}

		variables.put(box, numbered);

		return numbered;
	}

	private void appendProperties(LayoutableBox box, String varname, CodeBuilder builder) {
		box.getProperties().entrySet().stream()
			.forEach(e -> builder.append(createProperty(e.getKey(), e.getValue(), varname)));
	}

	private String createAddChildCode(String variableName, String parentVariableName) {
		return String.format("%s.addChild(%s);", parentVariableName, variableName);
	}

	private String createProperty(Object key, Integer value, String varname) {
		String classname = key.getClass().getSimpleName();
		String constantname = ((Enum<?>) key).name();

		if (value == null) {
			return String.format("%s.setProp(%s.%s);", varname, classname, constantname);
		} else {
			return String.format("%s.setProp(%s.%s, %d);", varname, classname, constantname, value);
		}
	}

	private String createInstantiation(LayoutableBox box, String variableName) {
		return String.format("LayoutableBox %s = new LayoutableBox(\"%s\", %d, %d, %d, %d);",
				variableName,
				box.getName(),
				box.getX(),
				box.getY(),
				box.getWidth(),
				box.getHeight());
	}

}
