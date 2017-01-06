package de.syslord.slidegen.editor.util;

public class Xy {

	private int x;

	private int y;

	public Xy(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Xy subtract(Xy xy) {
		return new Xy(x - xy.x, y - xy.y);
	}

	public Xy add(int x, int y) {
		return new Xy(this.x + x, this.y + y);
	}

	public Xy clamped(int minx, int maxx, int miny, int maxy) {
		return new Xy(Math.min(maxx, Math.max(minx, x)), Math.min(maxy, Math.max(miny, y)));
	}

}
