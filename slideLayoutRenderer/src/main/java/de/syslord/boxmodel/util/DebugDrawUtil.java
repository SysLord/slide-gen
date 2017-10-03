package de.syslord.boxmodel.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;

import de.syslord.boxmodel.renderer.RenderConfig;
import de.syslord.boxmodel.renderer.RenderableBox;

public class DebugDrawUtil {

	public static void fillRectDebug(Graphics2D graphics, float x, float y, float width, float height) {
		graphics.fillRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
	}

	public static void drawHorizontalLineDebug(Graphics2D graphics, double y, Paint color) {
		graphics.setPaint(color);

		System.out.println(y + " ~" + (int) Math.round(y));

		graphics.drawLine(0, (int) Math.round(y), 2000, (int) Math.round(y));
	}

	public static void drawLines(Graphics2D graphics, double y1, double y2, double y3) {
		drawHorizontalLineDebug(graphics, y1, Color.BLUE);
		drawHorizontalLineDebug(graphics, y2, Color.RED);
		drawHorizontalLineDebug(graphics, y3, Color.GREEN);
	}

	public static void drawDebugBorders(Graphics2D graphics, RenderableBox box) {
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
