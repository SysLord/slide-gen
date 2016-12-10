package de.syslord.slidegen.editor.model;

import java.awt.Font;

import de.syslord.boxmodel.renderer.FontProvider;

public class UiTextBoxStyleData extends UiBoxStyleData {

	private Font font = FontProvider.getDefaultFont();

	private int padding;

	private int margin;

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
}
