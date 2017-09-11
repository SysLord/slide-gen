package de.syslord.boxmodel.renderer;

import java.awt.font.TextAttribute;
import java.text.AttributedString;

import org.junit.Test;

public class RenderHelperTest {

	@Test
	public void testName() throws Exception {

		String text = "abcdefghijklmnopqrstuvwxyz";
		AttributedString attributedString = new AttributedString(text);
		attributedString.addAttribute(TextAttribute.FONT, FontProvider.getDefaultFont());

		// needs 87
		// TextRenderLines renderLines = RenderHelper.getRenderLines(attributedString, text, 40, 2);

		// TextRenderLines renderLines2 = RenderHelper.getRenderLines(attributedString, text, 40, 20);

		// System.out.println(renderLines);
		// System.out.println(renderLines2);

		// RenderHelper.insertEllipsis(attributedString, renderLines2, FontProvider.getDefaultFont());
	}
}
