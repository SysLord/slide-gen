package de.syslord.slidegen.editor.util;

// Vaadin could have used an enum already...
public enum Key {

	ARROW_LEFT(37),
	ARROW_UP(38),
	ARROW_RIGHT(39),
	ARROW_DOWN(40);

	private int code;

	private Key(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
