package de.syslord.boxmodel.generator;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.junit.Ignore;
import org.junit.Test;

import de.syslord.boxmodel.*;
import de.syslord.boxmodel.renderer.FontProvider;

public class ImageGeneratorTest {

	private Font font = FontProvider.getDefaultFont();

	@Test
	public void testName() throws Exception {
		LayoutableBox root = new LayoutableBox("root", 0, 0, 1000, 700);
		root.setProp(HeightProperty.FIX, 700);

		LayoutableBox nest = new LayoutableBox("nest", 50, 50, 900, 100);
		// nest.setProp(SizeProperty.MIN, 100);
		// nest.setProp(SizeProperty.MAX, 600);
		nest.setProp(HeightProperty.FIX, 600);

		root.addChild(nest);

		LayoutableBox line1 = new TextBox("line1", Lorem.Ips, font, 23, 20, 100, 30);
		line1.setPadding(10);
		line1.setProp(HeightProperty.MIN, 30);
		line1.setProp(HeightProperty.MAX, 100);

		LayoutableBox s = new TextBox("s", "nix", font, 500, 20, 100, 30);
		// s.setProp(SizeProperty.MIN, 30);
		// s.setProp(SizeProperty.MAX, 100);
		s.setProp(Stretch.GROW_LARGEST);

		LayoutableBox l = LineBox.createHorizontal("l", 40, 55, 500, 3, Color.GRAY);
		l.setProp(HeightProperty.FIX, 3);
		l.setProp(PositionProperty.FLOAT_DOWN);
		l.setProp(PositionProperty.FLOAT_UP);

		LayoutableBox line2 = new TextBox("line2", Lorem.Ips, font, 27, 60, 100, 300);
		line2.setPadding(10);
		line2.setProp(HeightProperty.MIN, 1);
		line2.setProp(HeightProperty.MAX, 300);
		line2.setProp(PositionProperty.FLOAT_DOWN);
		line2.setProp(PositionProperty.FLOAT_UP);

		LayoutableBox line3 = new TextBox("line3", Lorem.Ipsum, font, 33, 100, 200, 30);
		line3.setPadding(7);
		line3.setProp(HeightProperty.MIN, 30);
		line3.setProp(HeightProperty.MAX, 100);
		line3.setProp(PositionProperty.FLOAT_DOWN);
		line3.setProp(PositionProperty.FLOAT_UP);

		nest.addChild(line1);
		nest.addChild(s);
		nest.addChild(l);
		nest.addChild(line2);
		// nest.addChild(line3);

		BufferedImage generate = ImageGenerator.generate(root);

		ManualDebuggingImageDisplayer.showImage(generate);
	}

	@Ignore
	@Test
	public void testMinMax() throws Exception {
		LayoutableBox root = new TextBox("root", Lorem.Ipsum, font, 0, 0, 1000, 700);
		root.setProp(HeightProperty.MAX, 400);
		root.setProp(HeightProperty.MIN, 200);

		BufferedImage generate = ImageGenerator.generate(root);

		ManualDebuggingImageDisplayer.showImage(generate);
	}
}
