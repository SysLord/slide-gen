package de.syslord.boxmodel;

import java.awt.Font;

import de.syslord.boxmodel.render.FontProvider;
import de.syslord.boxmodel.render.RenderType;
import de.syslord.boxmodel.render.RenderableBox;
import de.syslord.boxmodel.render.RenderableBoxImpl;

public class TextBox extends LayoutableBox {

	private String content = "";

	private Font font = FontProvider.getDefaultFont();

	public TextBox(String name, String content, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		this.content = content;
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
