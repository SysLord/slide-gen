package de.syslord.slidegen.editor.ui.editor;

import java.awt.Font;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import de.syslord.slidegen.editor.ui.elements.UiTextBoxStyleData;

public class StylableLabel extends Label {

	private static final long serialVersionUID = -7049421276744427939L;

	private Font font;

	private Color foregroundColor;

	private String text;

	private int padding;

	private int margin;

	public StylableLabel(String text) {
		super();
		this.text = text;
		this.setContentMode(ContentMode.HTML);
	}

	public void updateStyle(UiTextBoxStyleData uiBoxData) {
		font = uiBoxData.getFont();
		foregroundColor = uiBoxData.getForegroundColor();
		padding = uiBoxData.getPadding();
		margin = uiBoxData.getMargin();

		// TODO certain previews are still missing

		setValue(text);
	}

	@Override
	public void setValue(String newStringValue) {
		this.text = newStringValue;

		super.setValue(getStyledText());
	}

	private String getStyledText() {
		if (foregroundColor == null || font == null) {
			return getHtmlText();
		}

		String f = String.format(
				"<span  style='"
						+ "display:block; "
						+ "color: rgb(%d,%d,%d); "
						+ "font-size:%dpx; "
						+ "font-weight:%s; "
						+ "margin:%spx; "
						+ "padding:%spx; "
						+ "'>%s</span>",
				foregroundColor.getRed(),
				foregroundColor.getGreen(),
				foregroundColor.getBlue(),
				font.getSize(),
				(font.getStyle() & Font.BOLD) > 0 ? "bold" : "normal",
				margin,
				padding,
				getHtmlText());
		return f;
	}

	private String getHtmlText() {
		return toHtmlLineBreaks(text);
	}

	private String toHtmlLineBreaks(String string) {
		return string.replace("\n", "<br>");
	}

	public String getText() {
		return text;
	}

}
