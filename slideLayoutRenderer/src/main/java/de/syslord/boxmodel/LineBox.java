package de.syslord.boxmodel;

import java.awt.Color;

import de.syslord.boxmodel.render.RenderType;
import de.syslord.boxmodel.render.RenderableBox;
import de.syslord.boxmodel.render.RenderableBoxImpl;

public class LineBox extends LayoutableBox {

	private Color color;

	private boolean horizontal;

	public LineBox(String name, int x, int y, int width, int height, Color color, boolean horizontal) {
		super(name, x, y, width, height);
		this.color = color;
		this.horizontal = horizontal;
	}

	public static LineBox createHorizontal(String name, int x, int y, int width, int thickness, Color color) {
		return new LineBox(name, x, y, width, thickness, color, true);
	}

	public Color getColor() {
		return color;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	@Override
	public RenderableBox toRenderable() {
		return new RenderableBoxImpl("", absoluteX, absoluteY, width, height, margin, padding, null, visible, RenderType.LINE,
				color);
	}

}
