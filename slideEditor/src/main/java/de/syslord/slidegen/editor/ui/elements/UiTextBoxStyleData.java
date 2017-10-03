package de.syslord.slidegen.editor.ui.elements;

import java.awt.Font;

import com.vaadin.shared.ui.colorpicker.Color;

import de.syslord.boxmodel.renderer.FontProvider;

public class UiTextBoxStyleData extends UiBoxStyleData {

	private Font font = FontProvider.getDefaultFont();

	private int padding = 0;

	private int margin = 0;

	private com.vaadin.shared.ui.colorpicker.Color textBackgroundColor = Color.WHITE;

	private Integer textBackgroundColorAlpha = 0;

	private int textBackgroundPadding = 0;

	private int lineSpacing = 0;

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getPadding() {
		return padding;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public com.vaadin.shared.ui.colorpicker.Color getTextBackgroundColor() {
		return textBackgroundColor;
	}

	public void setTextBackgroundColor(com.vaadin.shared.ui.colorpicker.Color textBackgroundColor) {
		this.textBackgroundColor = textBackgroundColor;
	}

	public Integer getTextBackgroundColorAlpha() {
		return textBackgroundColorAlpha;
	}

	public void setTextBackgroundColorAlpha(Integer textBackgroundColorAlpha) {
		this.textBackgroundColorAlpha = textBackgroundColorAlpha;
	}

	public int getTextBackgroundPadding() {
		return textBackgroundPadding;
	}

	public void setTextBackgroundPadding(int textBackgroundPadding) {
		this.textBackgroundPadding = textBackgroundPadding;
	}

	public int getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
}
