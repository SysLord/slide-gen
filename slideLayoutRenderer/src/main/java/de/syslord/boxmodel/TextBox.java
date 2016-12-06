package de.syslord.boxmodel;

import java.awt.Font;

import de.syslord.boxmodel.renderer.FontProvider;
import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBox;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class TextBox extends LayoutableBox {

	private String content = "";

	private Font font = FontProvider.getDefaultFont();

	public TextBox(String name, String content, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		this.content = content;
	}

	@Override
	public RenderableBox toRenderable() {
		return new RenderableBoxImpl(content,
				absoluteX, absoluteY, width, height,
				margin, padding,
				font,
				visible,
				RenderType.TEXT, foregroundColor);
	}

	public String getContent() {
		return content;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

}
