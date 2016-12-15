package de.syslord.boxmodel.generator;

import java.awt.image.BufferedImage;
import java.util.List;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.layouter.Layouter;
import de.syslord.boxmodel.renderer.RenderableBox;
import de.syslord.boxmodel.renderer.Renderer;

public class ImageGenerator {

	public static BufferedImage generate(LayoutableBox root) {

		Layouter.layout(root);

		List<RenderableBox> renderable = root.exportToRenderable();

		BufferedImage render = Renderer.render(root.getWidth(), root.getHeight(), renderable);

		return render;
	}

}
