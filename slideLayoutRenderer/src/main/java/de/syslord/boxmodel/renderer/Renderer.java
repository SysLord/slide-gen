package de.syslord.boxmodel.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
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

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import de.syslord.boxmodel.ImageScaling;
import de.syslord.boxmodel.renderer.html.HtmlScannerResult;
import de.syslord.boxmodel.renderer.html.PrimitiveHtmlScanner;
import de.syslord.boxmodel.util.DebugDrawUtil;

public class Renderer {

	public static BufferedImage render(int width, int height, List<RenderableBox> boxes) {

		BufferedImage image = RenderHelper.createImage(width, height);
		Graphics2D graphics = RenderHelper.getGraphics(image);

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		for (RenderableBox box : boxes) {
			DebugDrawUtil.drawDebugBorders(graphics, box);
			if (!box.isVisible()) {
				continue;
			}

			if (box.getBackgroundImage() != null) {
				drawBackgroundImage(graphics, box);
			}

			if (box.getRenderType().equals(RenderType.TEXT)) {
				drawText(graphics, box);
			} else if (box.getRenderType().equals(RenderType.HTML)) {
				drawHtml(graphics, box);
			} else if (box.getRenderType().equals(RenderType.LINE)) {
				drawLine(graphics, box);
			}
		}

		return image;
	}

	private static void drawBackgroundImage(Graphics2D graphics, RenderableBox box) {
		int targetWidth = box.getWidth();
		int targetHeight = box.getHeight();

		if (targetHeight <= 0 || targetWidth <= 0) {
			return;
		}

		ByteArrayInputStream backgroundImage = box.getBackgroundImage();
		backgroundImage.reset();
		BufferedImage image;
		try {
			image = ImageIO.read(backgroundImage);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		boolean sizesMatchExact = targetWidth == image.getWidth() && targetHeight == image.getHeight();
		if (sizesMatchExact) {
			graphics.drawImage(image, box.getX(), box.getY(), targetWidth, targetHeight, null);
			return;
		}

		drawBackgroundScaled(box, image, graphics);
	}

	private static void drawBackgroundScaled(RenderableBox box, BufferedImage image, Graphics2D graphics) {
		int targetWidth = box.getWidth();
		int targetHeight = box.getHeight();
		ImageScaling scalingProperty = box.getBackgroundScaling();

		if (ImageScaling.STRETCH_PROPORTIONALLY_CENTERED.equals(scalingProperty)) {
			double imageRatio = image.getWidth() / (double) image.getHeight();
			double boundingBoxRatio = targetWidth / (double) targetHeight;

			int newWidth;
			int newHeight;
			// image is wider, box is higher. image touches bounding box left and right. image width needs to
			// be box width.
			if (imageRatio > boundingBoxRatio) {
				newWidth = targetWidth;
				newHeight = (int) (targetWidth / imageRatio);
			} else {
				// image is higher, box is wider. image touches bounding box top and bottom. image height
				// needs to be box height.
				newWidth = (int) (imageRatio * targetHeight);
				newHeight = targetHeight;
			}

			BufferedImage scaled = Scalr.resize(image, Method.QUALITY, newWidth, newHeight);

			int x = box.getX() + (targetWidth - newWidth) / 2;
			int y = box.getY() + (targetHeight - newHeight) / 2;
			graphics.drawImage(scaled, x, y, newWidth, newHeight, null);

		} else if (ImageScaling.STRETCH_TO_BOX_DIMENSION.equals(scalingProperty)) {
			BufferedImage scaled = Scalr.resize(image, Method.QUALITY, targetWidth, targetHeight);
			graphics.drawImage(scaled, box.getX(), box.getY(), targetWidth, targetHeight, null);

		} else if (ImageScaling.ORIGINAL_SIZE_CUT.equals(scalingProperty)) {
			BufferedImage cropped = image.getSubimage(0, 0,
					Math.min(targetWidth, image.getWidth()),
					Math.min(targetHeight, image.getHeight()));

			graphics.drawImage(cropped, box.getX(), box.getY(), targetWidth, targetHeight, null);
		} else {
			throw new RuntimeException("No background scaling defined.");
		}
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

		AttributedString attributedString = new AttributedString(box.getContent());
		attributedString.addAttribute(TextAttribute.FONT, box.getFont());

		drawAutoLinebreakString(attributedString, box.getContent(), graphics, box);
	}

	private static void drawHtml(Graphics2D graphics, RenderableBox box) {
		if (box.getContent() == null || box.getContent().isEmpty()) {
			return;
		}

		HtmlScannerResult htmlScannerResult = PrimitiveHtmlScanner.generateAttributedString(box.getContent(), box.getFont());

		drawAutoLinebreakString(
				htmlScannerResult.getAttributedString(),
				htmlScannerResult.getPlainText(),
				graphics, box);
	}

	private static void drawAutoLinebreakString(
			AttributedString attributedString, String text, Graphics2D graphics, RenderableBox box) {

		int contentX = box.getContentX();
		int contentY = box.getContentY();
		int contentWidth = box.getContentWidth();
		int contentHeight = box.getContentHeight();

		// @formatter:off
		//
		// All values are baseline-relative!
		// 0/line origin ------------------
		//            ^                       <-- some space above normal letters
		//  /_\  |_)  |      __
		// /   \ |_) .|...  (__)
		// ascent/baseline---- |-----------
		//                  \__/
		// descent-------------------------
		// leading (line spacing) ---------   <-- usually very small like 0.9 pixels.
		//
		// @formatter:on

		float y = contentY;

		FontRenderContext fontRenderContext = graphics.getFontRenderContext();
		AttributedCharacterIterator charIterator = attributedString.getIterator();
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(charIterator, fontRenderContext);
		lineBreakMeasurer.setPosition(charIterator.getBeginIndex());

		while (lineBreakMeasurer.getPosition() < charIterator.getEndIndex()) {
			TextLayout layout = RenderHelper.handleTextLinebreaks(text, contentWidth, lineBreakMeasurer);

			final float textTopRelative = 0;
			final float baselineRelative = layout.getAscent();
			final float textBottomRelative = baselineRelative + layout.getDescent();
			final float nextLineRelative = textBottomRelative + layout.getLeading();

			final float textHeight = layout.getAscent() + layout.getDescent();

			final float nextY = y + nextLineRelative + box.getLineSpacing();

			// TODO !!! this may not work anymore.
			// do not render lines outside of box content area
			if (y + textBottomRelative > contentY + contentHeight) {
				break;
			}

			// draw textbackground
			if (box.getTextBackgroundColor() != null) {

				/*
				 * layout.getBounds()/layout.getPixelBounds are not useful here, because they yield for a line
				 * like "___" just a bounding box like this "___" while we would like to get the whole height
				 * like "|||".
				 */

				int paddingSizePixels = box.getTextBackgroundPadding();
				graphics.setPaint(box.getTextBackgroundColor());
				graphics.fillRect(
						contentX - paddingSizePixels,
						Math.round(y + textTopRelative - paddingSizePixels),
						Math.round(layout.getVisibleAdvance() + 2 * paddingSizePixels),
						Math.round(textHeight + 2 * paddingSizePixels));
			}

			// draw text relative to baseline
			graphics.setPaint(box.getColor());
			layout.draw(graphics, contentX, y + baselineRelative);

			y = nextY;
		}
	}

}
