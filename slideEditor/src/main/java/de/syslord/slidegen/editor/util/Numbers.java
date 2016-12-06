package de.syslord.slidegen.editor.util;

public class Numbers {

	public static int limit(int value, int min, int max) {
		return value > max ? max : value < min ? min : value;
	}

	public static Float limit(Float value, int min, int max) {
		return value > max ? max : value < min ? min : value;
	}

}
