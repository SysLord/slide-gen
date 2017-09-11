package de.syslord.boxmodel.renderer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.*;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

import com.google.common.collect.Lists;

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

	public static EllipsisText getRenderLines(AttributedString attributedString, String content, int width, int heightLimit) {
		Graphics2D graphics = getGraphics(IMG);

		float y = 0;

		FontRenderContext fontRenderContext = graphics.getFontRenderContext();

		AttributedCharacterIterator charIterator = attributedString.getIterator();
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(charIterator, fontRenderContext);
		lineBreakMeasurer.setPosition(charIterator.getBeginIndex());

		List<Integer> lines = Lists.newArrayList();
		while (lineBreakMeasurer.getPosition() < charIterator.getEndIndex()) {
			TextLayout layout = handleTextLinebreaks(content, width, lineBreakMeasurer);

			y += layout.getAscent();
			y += layout.getDescent();

			lines.add(lineBreakMeasurer.getPosition());

			boolean thisLineDoesNotFit = y > heightLimit;
			if (thisLineDoesNotFit) {
				break;
			}

			y += layout.getLeading();
		}

		int heightNeeded = (int) Math.ceil(y);

		return new EllipsisText(content, heightNeeded, lines);
	}

	public static void insertEllipsis(AttributedString attributedString, EllipsisText lines, Font ellipsisfont) {
		float ellipsisWidth = getEllipsisWidth(ellipsisfont);

		TextMeasurer textMeasurer = new TextMeasurer(attributedString.getIterator(), getGraphics(IMG).getFontRenderContext());

		int lastFittingLineStart = lines.getLastFittingLineStart();
		int lastFittingLineEnd = lines.getLastFittingLineEnd();

		float maxAvailableSpace = textMeasurer.getAdvanceBetween(lastFittingLineStart, lastFittingLineEnd);
		if (maxAvailableSpace < ellipsisWidth) {

			// TODO 21.04.2017
			System.out.println("Kein PLATZ");
		}

		for (int i = (lastFittingLineEnd - 1); i >= lastFittingLineStart; i--) {
			float spaceForEllipsis = textMeasurer.getAdvanceBetween(i, lastFittingLineEnd);

			if (spaceForEllipsis > ellipsisWidth) {
				System.out.println("Ellipsis bei " + i);
				System.out.println("Ellipsis bei " + lines.getText().substring(lastFittingLineStart, i) + RenderConfig.ellipsis);
			}
		}
	}

	private static float getEllipsisWidth(Font font) {
		FontMetrics metrics = IMG.getGraphics().getFontMetrics(font);
		return metrics.stringWidth(RenderConfig.ellipsis);
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
