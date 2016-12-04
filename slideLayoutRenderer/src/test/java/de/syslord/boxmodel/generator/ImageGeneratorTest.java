package de.syslord.boxmodel.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Ignore;
import org.junit.Test;

import de.syslord.boxmodel.ImageDisplayer;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.LineBox;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.SizeProperty;
import de.syslord.boxmodel.Stretch;
import de.syslord.boxmodel.TextBox;
import de.syslord.boxmodel.debug.Lorem;

public class ImageGeneratorTest {

	@Test
	public void testName() throws Exception {
		LayoutableBox root = new LayoutableBox("root", 0, 0, 1000, 700);
		root.setProp(SizeProperty.FIX, 700);

		LayoutableBox nest = new LayoutableBox("nest", 50, 50, 900, 100);
		// nest.setProp(SizeProperty.MIN, 100);
		// nest.setProp(SizeProperty.MAX, 600);
		nest.setProp(SizeProperty.FIX, 600);

		root.addChild(nest);

		LayoutableBox line1 = new TextBox("line1", Lorem.Ips, 23, 20, 100, 30);
		line1.setPadding(10);
		line1.setProp(SizeProperty.MIN, 30);
		line1.setProp(SizeProperty.MAX, 100);

		LayoutableBox s = new TextBox("s", "nix", 500, 20, 100, 30);
		// s.setProp(SizeProperty.MIN, 30);
		// s.setProp(SizeProperty.MAX, 100);
		s.setProp(Stretch.GROW_LARGEST);

		LayoutableBox l = LineBox.createHorizontal("l", 40, 55, 500, 3, Color.GRAY);
		l.setProp(SizeProperty.FIX, 3);
		l.setProp(PositionProperty.FLOAT);

		LayoutableBox line2 = new TextBox("line2", Lorem.Ips, 27, 60, 100, 300);
		line2.setPadding(10);
		line2.setProp(SizeProperty.MIN, 1);
		line2.setProp(SizeProperty.MAX, 300);
		line2.setProp(PositionProperty.FLOAT);

		LayoutableBox line3 = new TextBox("line3", Lorem.Ipsum, 33, 100, 200, 30);
		line3.setPadding(7);
		line3.setProp(SizeProperty.MIN, 30);
		line3.setProp(SizeProperty.MAX, 100);
		line3.setProp(PositionProperty.FLOAT);

		nest.addChild(line1);
		nest.addChild(s);
		nest.addChild(l);
		nest.addChild(line2);
		// nest.addChild(line3);

		BufferedImage generate = ImageGenerator.generate(root);

		ImageDisplayer.showImage(generate);
	}

	@Ignore
	@Test
	public void testMinMax() throws Exception {
		LayoutableBox root = new TextBox("root", Lorem.Ipsum, 0, 0, 1000, 700);
		root.setProp(SizeProperty.MAX, 400);
		root.setProp(SizeProperty.MIN, 200);

		BufferedImage generate = ImageGenerator.generate(root);

		ImageDisplayer.showImage(generate);
	}
}
