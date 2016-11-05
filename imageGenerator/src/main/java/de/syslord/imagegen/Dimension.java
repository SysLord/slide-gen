package de.syslord.imagegen;

public class Dimension {

	private int width;

	private int height;

	public Dimension(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static Dimension of(int width, int height) {
		return new Dimension(width, height);
	}
}