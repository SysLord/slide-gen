package de.syslord.boxmodel;

import java.awt.Color;

import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBox;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class LineBox extends LayoutableBox {

	private boolean horizontal;

	public LineBox(String name, int x, int y, int width, int height, Color color, boolean horizontal) {
		super(name, x, y, width, height);
		foregroundColor = color;
		this.horizontal = horizontal;
	}

	public static LineBox createHorizontal(String name, int x, int y, int width, int thickness, Color color) {
		return new LineBox(name, x, y, width, thickness, color, true);
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	@Override
	public RenderableBox toRenderable() {
		return new RenderableBoxImpl(
				"",
				absoluteX, absoluteY, width, height,
				margin, padding, null,
				visible,
				RenderType.LINE,
				foregroundColor);
	}

}
