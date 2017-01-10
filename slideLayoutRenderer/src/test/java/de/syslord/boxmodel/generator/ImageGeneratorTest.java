package de.syslord.boxmodel.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.LineBox;
import de.syslord.boxmodel.Lorem;
import de.syslord.boxmodel.ManualDebuggingImageDisplayer;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.TextBox;
import de.syslord.boxmodel.util.ResourceUtil;

public class ImageGeneratorTest {

	private static final boolean WITH_IMAGES = false;

	@Test
	public void manuallyTestRenderImage_NoTestAssertYet_NeedsManualInspection() throws Exception {
		LayoutableBox root = new LayoutableBox("root", 0, 0, 1000, 700);
		root.setProp(HeightProperty.MIN, 700);
		root.setProp(HeightProperty.MAX, 700);

		LayoutableBox nest = new LayoutableBox("nest", 50, 50, 900, 600);
		nest.setProp(HeightProperty.MIN, 600);
		nest.setProp(HeightProperty.MAX, 600);

		TextBox t1 = new TextBox("t1", Lorem.Ips, 0, 0, 100, 30);
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

		if (WITH_IMAGES) {
			root.setBackgroundImage(ResourceUtil.getResourceAsStream("testimage_medium.png"));
			nest.setBackgroundImage(ResourceUtil.getResourceAsStream("testimage_large.png"));
			t1.setBackgroundImage(ResourceUtil.getResourceAsStream("testimage_tiny.png"));
		}

		BufferedImage generate = ImageGenerator.generate(root);

		ManualDebuggingImageDisplayer.showImage(generate);
	}

}
