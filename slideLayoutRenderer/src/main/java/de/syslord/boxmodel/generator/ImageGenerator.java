package de.syslord.boxmodel.generator;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.layouter.Layouter;
import de.syslord.boxmodel.render.RenderableBox;
import de.syslord.boxmodel.render.Renderer;

public class ImageGenerator {

	public static BufferedImage generate(LayoutableBox root) {

		Layouter.layout(root);

		List<RenderableBox> collect = root
			.streamFlat()
			.map(layout -> layout.toRenderable())
			.filter(r -> r.isVisible())
			.collect(Collectors.toList());

		BufferedImage render = Renderer.render(root.getWidth(), root.getHeight(), collect);

		return render;
	}

}
