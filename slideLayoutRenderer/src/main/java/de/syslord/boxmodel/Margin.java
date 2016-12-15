package de.syslord.boxmodel;

public class Margin extends Inset {

	protected Margin(int left, int right, int top, int bottom) {
		super(left, right, top, bottom);
	}

	public static Margin noMargin() {
		return new Margin(0, 0, 0, 0);
	}

	public static Margin create(int leftRight, int topBottom) {
		return new Margin(leftRight, leftRight, topBottom, topBottom);
	}

}
