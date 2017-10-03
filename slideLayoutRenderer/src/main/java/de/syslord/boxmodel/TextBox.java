package de.syslord.boxmodel;

import java.awt.Font;
import java.awt.Paint;

import de.syslord.boxmodel.renderer.FontProvider;
import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class TextBox extends LayoutableBox {

	private String content;

	private Font font = FontProvider.getDefaultFont();

	private Margin margin = Margin.noMargin();

	private Padding padding = Padding.noPadding();

	private Paint textBackgroundColor = null;

	private boolean htmlMode = false;

	private int textBackgroundPadding = 0;

	private int lineSpacing = 0;

	public TextBox(String name, String content, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		this.content = content;
	}

	public static TextBox createFixedHeightTextBox(String name, String content, int x, int y, int width, int height) {
		TextBox box = new TextBox(name, content, x, y, width, height);
		box.setProp(HeightProperty.MIN, height);
		box.setProp(HeightProperty.MAX, height);
		return box;
	}

	@Override
	public RenderableBoxImpl toRenderable() {
		RenderableBoxImpl renderableBoxImpl = super.toRenderable();

		renderableBoxImpl.setRenderType(htmlMode ? RenderType.HTML : RenderType.TEXT);
		renderableBoxImpl.setContent(content);
		renderableBoxImpl.setFont(font);
		renderableBoxImpl.setTextBackgroundColor(textBackgroundColor);
		renderableBoxImpl.setTextBackgroundPadding(textBackgroundPadding);
		renderableBoxImpl.setLineSpacing(lineSpacing);

		renderableBoxImpl.setMargin(margin);
		renderableBoxImpl.setPadding(padding);

		return renderableBoxImpl;
	}

	public int getContentWidth() {
		int spaceNeeded = margin.getHorizontalSpaceNeeded() + padding.getHorizontalSpaceNeeded();
		return Math.max(0, width - spaceNeeded);
	}

	public String getContent() {
		return content;
	}

	public Font getFont() {
		return font;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		style.getFont(styleIdentifier).ifPresent(f -> font = f);
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Margin getMargin() {
		return margin;
	}

	public Padding getPadding() {
		return padding;
	}

	public void setMargin(Margin margin) {
		this.margin = margin;
	}

	public void setPadding(Padding padding) {
		this.padding = padding;
	}

	public void setHtmlMode(boolean htmlMode) {
		this.htmlMode = htmlMode;
	}

	public boolean isHtmlMode() {
		return htmlMode;
	}

	public Paint getTextBackgroundColor() {
		return textBackgroundColor;
	}

	public void setTextBackgroundColor(Paint textBackgroundColor) {
		this.textBackgroundColor = textBackgroundColor;
	}

	public int getTextBackgroundPadding() {
		return textBackgroundPadding;
	}

	public void setTextBackgroundPadding(int textBackgroundPadding) {
		this.textBackgroundPadding = textBackgroundPadding;
	}

	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	public int getLineSpacing() {
		return lineSpacing;
	}

}
