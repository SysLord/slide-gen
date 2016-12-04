package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import de.syslord.boxmodel.ImageDisplayer;
import de.syslord.boxmodel.debug.Lorem;
import de.syslord.boxmodel.render.FontProvider;
import de.syslord.boxmodel.render.RenderType;
import de.syslord.boxmodel.render.RenderableBoxImpl;
import de.syslord.boxmodel.render.Renderer;

public class RendererTest {

	@Test
	public void test() throws IOException {

		RenderableBoxImpl box = new RenderableBoxImpl(Lorem.Ipsum, 0, 0, 200, 200, 20, 10, FontProvider.getDefaultFont(), true,
				RenderType.TEXT, Color.BLACK);
		RenderableBoxImpl box1 = new RenderableBoxImpl(Lorem.Ipsum, 0, 250, 500, 400, 3, 4, FontProvider.getDefaultFont(), true,
				RenderType.TEXT, Color.BLACK);
		RenderableBoxImpl box2 = new RenderableBoxImpl(Lorem.Ipsum, 600, 200, 100, 100, 5, 5, FontProvider.getDefaultFont(), true,
				RenderType.TEXT, Color.BLACK);

		BufferedImage render = Renderer.render(1000, 700, Arrays.asList(box, box1, box2));

		ImageDisplayer.showImage(render);
	}

}
