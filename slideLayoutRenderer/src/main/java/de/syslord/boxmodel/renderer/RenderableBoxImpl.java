package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;

import de.syslord.boxmodel.Margin;
import de.syslord.boxmodel.Padding;

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

	private Margin margin = Margin.noMargin();

	private Padding padding = Padding.noPadding();

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
		return x + margin.getLeft() + padding.getLeft();
	}

	@Override
	public int getContentY() {
		return y + margin.getTop() + padding.getTop();
	}

	@Override
	public int getContentWidth() {
		int spaceNeeded = margin.getHorizontalSpaceNeeded() + padding.getHorizontalSpaceNeeded();
		return Math.max(0, width - spaceNeeded);
	}

	@Override
	public int getContentHeight() {
		int spaceNeeded = margin.getVerticalSpaceNeeded() + padding.getVerticalSpaceNeeded();
		return Math.max(0, height - spaceNeeded);
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
	public Margin getMargin() {
		return margin;
	}

	@Override
	public Padding getPadding() {
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

	public void setMargin(Margin margin) {
		this.margin = margin;
	}

	public void setPadding(Padding padding) {
		this.padding = padding;
	}

}
