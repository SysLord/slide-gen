package de.syslord.boxmodel.renderer;

import java.awt.Font;

public class FontProvider {

	public static Font getDefaultFont() {
		// hardcoded for now
		Font decode = Font.decode("Arial");
		return new Font(decode.getName(), Font.PLAIN, 15);
	}

	public static Font getStyled(Font font, boolean bold, boolean italic) {
		int style = (bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0);
		return new Font(font.getName(), style, font.getSize());
	}

	public static Font getStandardStyled(Font font) {
		return new Font(font.getName(), Font.PLAIN, font.getSize());
	}

}
