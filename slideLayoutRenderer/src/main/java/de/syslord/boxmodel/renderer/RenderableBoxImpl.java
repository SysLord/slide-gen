package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;

// TODO god object. Might just use LayoutableBox?
public class RenderableBoxImpl implements RenderableBox {

	private String content;

	private int x;

	private int y;

	private int width;

	private int height;

	private int margin;

	private int padding;

	private Font font;

	private boolean visible;

	private RenderType renderType;

	private Color color;

	private ByteArrayInputStream backgroundImage;

	public RenderableBoxImpl(String content, int x, int y, int width, int height, int margin, int padding, Font font,
			boolean visible, RenderType renderType, Color color) {
		this.content = content;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.margin = margin;
		this.padding = padding;
		this.font = font;
		this.visible = visible;
		this.renderType = renderType;
		this.color = color;
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
	public Color getForegroundColor() {
		return color;
	}

	public void setBackgroundImage(ByteArrayInputStream backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public ByteArrayInputStream getBackgroundImage() {
		return backgroundImage;
	}

}
