package de.syslord.boxmodel;

import java.awt.Font;

import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBox;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class TextBox extends LayoutableBox {

	private String content;

	private Font font;

	public TextBox(String name, String content, int x, int y, int width, int height, Font font) {
		super(name, x, y, width, height);
		this.content = content;
		this.font = font;
	}

	@Override
	public RenderableBox toRenderable() {
		return new RenderableBoxImpl(content, absoluteX, absoluteY, width, height, margin, padding, font, visible,
				RenderType.TEXT, null);
	}

	public String getContent() {
		return content;
	}

	public Font getFont() {
		return font;
	}

}
