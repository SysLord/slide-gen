package de.syslord.boxmodel;

import java.awt.Font;

import de.syslord.boxmodel.renderer.FontProvider;
import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class TextBox extends LayoutableBox {

	private String content;

	private Font font = FontProvider.getDefaultFont();

	private int margin = 0;

	private int padding = 0;

	private String stylename;

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

		renderableBoxImpl.setMargin(margin);
		renderableBoxImpl.setPadding(padding);

		return renderableBoxImpl;
	}

	public int getContentWidth() {
		return Math.max(0, width - 2 * (margin + padding));
	}

	public String getContent() {
		return content;
	}

	public Font getFont() {
		return font;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		style.getFont(stylename).ifPresent(f -> font = f);
	}
}
