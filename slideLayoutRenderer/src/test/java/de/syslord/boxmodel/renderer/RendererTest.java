package de.syslord.boxmodel.renderer;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import de.syslord.boxmodel.Lorem;
import de.syslord.boxmodel.ManualDebuggingImageDisplayer;

public class RendererTest {

	@Test
	public void test() throws IOException {

		RenderableBoxImpl box = new RenderableBoxImpl(0, 0, 200, 200, 20, 10, true);
		box.setRenderType(RenderType.TEXT);
		box.setContent(Lorem.Ipsum);

		RenderableBoxImpl box1 = new RenderableBoxImpl(0, 250, 500, 400, 3, 4, true);
		box1.setRenderType(RenderType.TEXT);
		box1.setContent(Lorem.Ipsum);

		RenderableBoxImpl box2 = new RenderableBoxImpl(600, 200, 100, 100, 5, 5, true);
		box2.setRenderType(RenderType.TEXT);
		box2.setContent(Lorem.Ipsum);

		BufferedImage render = Renderer.render(1000, 700, Arrays.asList(box, box1, box2));

		// TODO compare with example image
		assertTrue(render != null);

		ManualDebuggingImageDisplayer.showImage(render);
	}

}
