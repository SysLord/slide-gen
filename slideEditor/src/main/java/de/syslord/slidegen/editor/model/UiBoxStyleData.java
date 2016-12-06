package de.syslord.slidegen.editor.model;

import java.awt.Font;
import java.io.ByteArrayInputStream;

import com.vaadin.shared.ui.colorpicker.Color;

import de.syslord.boxmodel.renderer.FontProvider;

public class UiBoxStyleData {

	private Integer maxHeight = null;

	private Integer minHeight = null;

	private boolean floatUp = false;

	private boolean floatDown = false;

	// TODO story font metadata and generate font only on export
	private Font font = FontProvider.getDefaultFont();

	private ByteArrayInputStream image = null;

	private Color foregroundColor = Color.BLACK;

	public Integer getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}

	public Integer getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}

	public boolean getFloatUp() {
		return floatUp;
	}

	public void setFloatUp(boolean floatUp) {
		this.floatUp = floatUp;
	}

	public boolean getFloatDown() {
		return floatDown;
	}

	public void setFloatDown(boolean floatDown) {
		this.floatDown = floatDown;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public ByteArrayInputStream getImage() {
		return image;
	}

	public void setImage(ByteArrayInputStream image) {
		this.image = image;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

}