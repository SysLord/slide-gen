package de.syslord.boxmodel;

public class Inset {

	private int left = 0;

	private int right = 0;

	private int top = 0;

	private int bottom = 0;

	protected Inset(int left, int right, int top, int bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public int getHorizontalSpaceNeeded() {
		return left + right;
	}

	public int getVerticalSpaceNeeded() {
		return top + bottom;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}

}
