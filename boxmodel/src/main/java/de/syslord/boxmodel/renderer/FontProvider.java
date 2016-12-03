package de.syslord.boxmodel.renderer;

import java.awt.Font;

public class FontProvider {

	public static Font getFont() {
		// hardcoded for now
		Font decode = Font.decode("Arial");
		// return new Font(decode.getName(), Font.PLAIN, 35);
		return new Font(decode.getName(), Font.PLAIN, 15);
	}
}
