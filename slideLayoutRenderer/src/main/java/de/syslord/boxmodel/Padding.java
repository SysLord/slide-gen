package de.syslord.boxmodel;

public class Padding extends Inset {

	protected Padding(int left, int right, int top, int bottom) {
		super(left, right, top, bottom);
	}

	public static Padding noPadding() {
		return new Padding(0, 0, 0, 0);
	}

	public static Padding create(int leftRight, int topBottom) {
		return new Padding(leftRight, leftRight, topBottom, topBottom);
	}

}
