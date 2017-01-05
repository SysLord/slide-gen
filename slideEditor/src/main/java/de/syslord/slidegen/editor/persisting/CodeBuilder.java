package de.syslord.slidegen.editor.persisting;

import com.google.common.base.Strings;

public class CodeBuilder {

	private StringBuilder builder = new StringBuilder();

	private int indent = 0;

	public void append(String string) {
		builder.append(Strings.repeat("\t", indent));
		builder.append(string);
		builder.append("\n");
	}

	public void append() {
		builder.append("\n");
	}

	@Override
	public String toString() {
		return builder.toString();
	}

	public void indent() {
		indent++;
	}

	public void unIndent() {
		indent = Math.max(0, --indent);
	}

}
