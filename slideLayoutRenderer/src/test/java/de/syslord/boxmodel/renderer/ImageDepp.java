package de.syslord.boxmodel.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.syslord.boxmodel.ManualDebuggingImageDisplayer;
import de.syslord.boxmodel.util.ResourceUtil;

public class ImageDepp {

	@Test
	public void testName() throws Exception {
		List<Histo> histos = Lists.newArrayList();

		BufferedImage read = ImageIO.read(ResourceUtil.getResourceAsStream("test.jpg"));

		int width = read.getWidth();
		int height = read.getHeight();

		for (int x = 0; x < width; x++) {

			Histo h = new Histo();
			for (int y = 0; y < height; y++) {
				int[] pixel = read.getRaster().getPixel(x, y, (int[]) null);
				h.add(pixel);
			}
			histos.add(h);

		}

		List<double[]> collect = histos.stream().map(h -> h.getSum()).collect(Collectors.toList());

		List<double[]> diff = Lists.newArrayList();

		for (int i = 0; i < collect.size() - 1; i++) {

			double[] d = new double[3];

			d[0] = collect.get(i)[0] - collect.get(i + 1)[0];
			d[1] = collect.get(i)[1] - collect.get(i + 1)[1];
			d[2] = collect.get(i)[2] - collect.get(i + 1)[2];

			diff.add(d);
		}

		for (double[] ds : diff) {
			System.out.println(ds[0] + " " + ds[1] + " " + ds[2]);
		}

		List<Integer> throwaway = Lists.newArrayList();

		Comparator<double[]> comparator = (o1, o2) -> {

			double x = o1[0] + o1[1] + o1[2];
			double v = o2[0] + o2[1] + o2[2];

			return Double.compare(x, v);
		};
		Comparator<Pair<Integer, double[]>> xcc = (o1, o2) -> comparator.compare(o1.b, o2.b);

		IntStream.range(0, diff.size() - 1).boxed()
			.map(xxx -> new Pair<double[], double[]>(diff.get(xxx), diff.get(xxx + 1)))
			.map(p -> comparator.compare(p.a, p.b))
			.forEach(io -> System.out.println(io));

		for (int i = 0; i < 100; i++) {

			Pair<Integer, double[]> pair = IntStream.range(0, diff.size()).boxed()
				.map(xxx -> new Pair<Integer, double[]>(xxx, diff.get(xxx)))
				.min(xcc).get();

			Integer rr = pair.a;

			throwaway.add(rr);

			diff.remove(rr);
			diff.add(rr, new double[] { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE });

		}

		System.out.println(throwaway);

		Graphics2D g = (Graphics2D) read.getGraphics();

		for (Integer j : throwaway) {
			g.setColor(Color.black);
			g.drawLine(j, 0, j, height);
		}

		ManualDebuggingImageDisplayer.showImage(read);

	}
}
