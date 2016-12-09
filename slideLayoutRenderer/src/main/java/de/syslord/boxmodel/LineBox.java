package de.syslord.boxmodel;

import java.awt.Color;

import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class LineBox extends LayoutableBox {

	private int thickness;

	public LineBox(String name, int x, int y, int width, int height, Color color, int thickness) {
		super(name, x, y, width, height);
		foregroundColor = color;
		this.thickness = thickness;
	}

	@Override
	public RenderableBoxImpl toRenderable() {
		RenderableBoxImpl renderableBoxImpl = super.toRenderable();

		renderableBoxImpl.setRenderType(RenderType.LINE);
		renderableBoxImpl.setLineThickness(thickness);

		return renderableBoxImpl;
	}

}
