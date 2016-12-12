package de.syslord.boxmodel.generator;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.junit.Test;

import de.syslord.boxmodel.*;
import de.syslord.boxmodel.renderer.FontProvider;

public class ImageGeneratorTest {

	private Font font = FontProvider.getDefaultFont();

	@Test
	public void testName() throws Exception {
		LayoutableBox root = new LayoutableBox("root", 0, 0, 1000, 700);
		root.setProp(HeightProperty.MIN, 700);
		root.setProp(HeightProperty.MAX, 700);

		LayoutableBox nest = new LayoutableBox("nest", 50, 50, 900, 600);
		nest.setProp(HeightProperty.MIN, 600);
		nest.setProp(HeightProperty.MAX, 600);

		TextBox t1 = new TextBox("t1", Lorem.Ips, font, 0, 0, 100, 30);
		t1.setPadding(20);
		t1.setProp(HeightProperty.MIN, 30);
		t1.setProp(HeightProperty.MAX, 100);

		LayoutableBox line = new LineBox("l", 0, 40, 200, 20, Color.GRAY, 4);
		line.setProp(HeightProperty.MAX, 0);
		line.setProp(HeightProperty.MIN, 0);
		line.setProp(PositionProperty.FLOAT_DOWN);
		line.setProp(PositionProperty.FLOAT_UP);

		root.addChild(nest);

		nest.addChild(t1);
		nest.addChild(line);

		BufferedImage generate = ImageGenerator.generate(root);

		ManualDebuggingImageDisplayer.showImage(generate);
	}

}
