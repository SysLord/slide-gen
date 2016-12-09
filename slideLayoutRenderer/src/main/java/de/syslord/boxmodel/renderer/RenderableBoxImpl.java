package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;

//TODO 08.12.2016 needs a proper builder when architecture relatively stable
public class RenderableBoxImpl implements RenderableBox {

	private String content = "";

	private Font font = FontProvider.getDefaultFont();

	private RenderType renderType = RenderType.BOX;

	private Color color = Color.BLACK;

	private int x;

	private int y;

	private int width;

	private int height;

	private int margin;

	private int padding;

	private boolean visible;

	private ByteArrayInputStream backgroundImage;

	private int lineThickness;

	public RenderableBoxImpl(int x, int y, int width, int height, boolean visible) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.visible = visible;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public int getContentX() {
		return x + margin + padding;
	}

	@Override
	public int getContentY() {
		return y + margin + padding;
	}

	@Override
	public int getContentWidth() {
		return Math.max(0, width - 2 * (margin + padding));
	}

	@Override
	public int getContentHeight() {
		return Math.max(0, height - 2 * (margin + padding));
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getMargin() {
		return margin;
	}

	@Override
	public int getPadding() {
		return padding;
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public RenderType getRenderType() {
		return renderType;
	}

	@Override
	public Color getColor() {
		return color;
	}

	public void setBackgroundImage(ByteArrayInputStream backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	@Override
	public ByteArrayInputStream getBackgroundImage() {
		return backgroundImage;
	}

	public void setRenderType(RenderType renderType) {
		this.renderType = renderType;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}

	@Override
	public int getLineThickness() {
		return lineThickness;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

}
