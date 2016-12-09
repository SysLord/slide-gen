package de.syslord.boxmodel.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

import javax.imageio.ImageIO;

public class Renderer {

	public static BufferedImage render(int width, int height, List<RenderableBox> boxes) {

		BufferedImage image = RenderHelper.createImage(width, height);
		Graphics2D graphics = RenderHelper.getGraphics(image);

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		for (RenderableBox box : boxes) {
			drawDebugBorders(graphics, box);

			if (box.getBackgroundImage() != null) {
				drawBackgroundImage(graphics, box);
			}

			if (box.getRenderType().equals(RenderType.TEXT)) {
				drawText(graphics, box);
			} else if (box.getRenderType().equals(RenderType.LINE)) {
				drawLine(graphics, box);
			}
		}

		return image;
	}

	private static void drawBackgroundImage(Graphics2D graphics, RenderableBox box) {

		ByteArrayInputStream backgroundImage = box.getBackgroundImage();
		backgroundImage.reset();
		BufferedImage read;
		try {
			read = ImageIO.read(backgroundImage);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		BufferedImage cropped = read.getSubimage(0, 0, box.getWidth(), box.getHeight());

		graphics.drawImage(cropped, box.getX(), box.getY(), box.getWidth(), box.getHeight(), null);
	}

	private static void drawLine(Graphics2D graphics, RenderableBox box) {
		graphics.setColor(box.getColor());
		graphics.setStroke(new BasicStroke(box.getLineThickness()));

		graphics.drawLine(box.getContentX(), box.getContentY(),
				box.getContentX() + box.getContentWidth(),
				box.getContentY() + box.getContentHeight());
	}

	private static void drawText(Graphics2D graphics, RenderableBox box) {
		if (box.getContent() == null || box.getContent().isEmpty()) {
			return;
		}

		graphics.setColor(box.getColor());

		AttributedString attributedString = new AttributedString(box.getContent());
		attributedString.addAttribute(TextAttribute.FONT, box.getFont());

		drawAutoLinebreakString(attributedString, box.getContent(), graphics, box);
	}

	private static void drawAutoLinebreakString(
			AttributedString attributedString, String text, Graphics2D graphics, RenderableBox box) {

		int contentX = box.getContentX();
		int contentY = box.getContentY();
		int contentWidth = box.getContentWidth();
		int contentHeight = box.getContentHeight();

		float y = contentY;

		FontRenderContext fontRenderContext = graphics.getFontRenderContext();
		AttributedCharacterIterator charIterator = attributedString.getIterator();
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(charIterator, fontRenderContext);
		lineBreakMeasurer.setPosition(charIterator.getBeginIndex());

		while (lineBreakMeasurer.getPosition() < charIterator.getEndIndex()) {
			TextLayout layout = RenderHelper.handleTextLinebreaks(text, contentWidth, lineBreakMeasurer);
			// ignores \n
			// TextLayout layout = lineBreakMeasurer.nextLayout(contentWidth);

			y += layout.getAscent();

			// do not render lines outside of box content area
			if (y > contentY + contentHeight) {
				break;
			}

			layout.draw(graphics, contentX, y);
			y += layout.getDescent() + layout.getLeading();
		}
	}

	private static void drawDebugBorders(Graphics2D graphics, RenderableBox box) {
		if (RenderConfig.drawDebugBorders) {
			Color savedColor = graphics.getColor();
			Stroke savedStroke = graphics.getStroke();

			graphics.setColor(Color.CYAN);
			graphics.setStroke(new BasicStroke(4));
			graphics.drawRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());

			graphics.setColor(savedColor);
			graphics.setStroke(savedStroke);
		}
		if (RenderConfig.drawDebugHeights) {
			Color savedColor = graphics.getColor();
			Stroke savedStroke = graphics.getStroke();

			graphics.setColor(Color.BLACK);
			String format = String.format("x %d y %d h %d", box.getX(), box.getY(), box.getHeight());
			graphics.drawString(format, box.getX() + box.getWidth() - 100, box.getY() + 20);

			graphics.setColor(savedColor);
			graphics.setStroke(savedStroke);
		}
	}

}
