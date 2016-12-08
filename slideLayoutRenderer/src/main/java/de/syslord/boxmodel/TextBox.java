package de.syslord.boxmodel;

import java.awt.Font;

import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class TextBox extends LayoutableBox {

	private String content;

	private Font font;

	public TextBox(String name, String content, Font font, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		this.content = content;
		this.font = font;
	}

	@Override
	public RenderableBoxImpl toRenderable() {
		RenderableBoxImpl renderableBoxImpl = super.toRenderable();

		renderableBoxImpl.setRenderType(RenderType.TEXT);
		renderableBoxImpl.setContent(content);
		renderableBoxImpl.setFont(font);

		renderableBoxImpl.setBackgroundImage(backgroundImage);
		return renderableBoxImpl;
	}

	public String getContent() {
		return content;
	}

	public Font getFont() {
		return font;
	}

}
