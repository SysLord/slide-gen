package de.syslord.boxmodel.renderer;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class RenderHelper {

	private static final BufferedImage IMG = createImage(1, 1);

	public static int getHeight(Font font, String content, int width) {
		if (content == null || content.isEmpty()) {
			return 0;
		}

		AttributedString attributedString = new AttributedString(content);
		attributedString.addAttribute(TextAttribute.FONT, font);

		return getHeight(attributedString, content, width);
	}

	public static int getHeight(AttributedString attributedString, String content, int width) {
		if (content == null || content.isEmpty()) {
			return 0;
		}

		Graphics2D graphics = getGraphics(IMG);

		float y = 0;

		FontRenderContext fontRenderContext = graphics.getFontRenderContext();
		AttributedCharacterIterator charIterator = attributedString.getIterator();
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(charIterator, fontRenderContext);
		lineBreakMeasurer.setPosition(charIterator.getBeginIndex());

		while (lineBreakMeasurer.getPosition() < charIterator.getEndIndex()) {
			TextLayout layout = handleTextLinebreaks(content, width, lineBreakMeasurer);
			// ignores \n
			// TextLayout layout = lineBreakMeasurer.nextLayout(contentWidth);

			y += layout.getAscent();
			y += layout.getDescent() + layout.getLeading();
		}

		return (int) Math.ceil(y);
	}

	// centralize image and graphics acquisition to make sure the text space requirements are the same as
	// during text rendering later
	public static Graphics2D getGraphics(BufferedImage image) {
		Graphics2D graphics = image.createGraphics();

		graphics.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		return graphics;
	}

	public static BufferedImage createImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	}

	// http://stackoverflow.com/questions/1120151/handling-n-in-linebreakmeasurer
	// doesn't seem that efficient
	public static TextLayout handleTextLinebreaks(String text, int contentWidth, LineBreakMeasurer lineBreakMeasurer) {
		int next = lineBreakMeasurer.nextOffset(contentWidth);
		int limit = next;
		if (limit <= text.length()) {
			// find \n between current position and next position
			for (int i = lineBreakMeasurer.getPosition(); i < next; ++i) {
				char c = text.charAt(i);
				if (c == '\n') {
					limit = i + 1;
					break;
				}
			}
		}
		// already linebreak at new limit
		return lineBreakMeasurer.nextLayout(contentWidth, limit, false);
	}

}
