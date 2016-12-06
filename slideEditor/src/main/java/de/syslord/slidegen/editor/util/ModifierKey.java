package de.syslord.slidegen.editor.util;

// Vaadin could have used an enum already...
public enum ModifierKey {

	SHIFT(16),
	CTRL(17),
	ALT(18),
	META(91);

	private int code;

	private ModifierKey(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
