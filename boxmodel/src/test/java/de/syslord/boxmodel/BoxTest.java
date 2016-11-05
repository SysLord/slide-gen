package de.syslord.boxmodel;

import java.awt.Graphics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import de.syslord.boxmodel.properties.SizeProperty;
import de.syslord.boxmodel.properties.Stretch;

public class BoxTest {

	private ImageGenerator imageGen = new ImageGenerator();

	@Test
	public void testName() throws Exception {
		Box root = createSomeTestBoxes("top ");
		for (Box box : root.getChildren()) {
			box.addChild(createSomeTestBoxes("nest "));
		}

		streamFlat(root).forEach(x -> System.out.println(x.name));

		inheritMaxAndFixSizesTopDown(null, root);
		// print(root, "a");
		updateBoxSizeFromContentBottomUp(null, root);
		// print(root, "b");
		applyStretchLargest_TopDown(null, root);
		// print(root, "c");
		print(root, "d");
	}

	private Box createSomeTestBoxes(String prefix) {
		Box child1 = new Box(prefix + "1");
		child1.contentSize = 5;

		Box child2 = new Box(prefix + "2");
		child2.setProp(Stretch.LARGEST, null);
		child2.y = 10;

		Box child3 = new Box(prefix + "3");
		child3.setProp(Stretch.LARGEST_FROM_TOP, null);
		child3.setProp(Stretch.LARGEST, null);

		Box child4 = new Box(prefix + "4");
		child4.setProp(Stretch.LARGEST, null);
		child4.setProp(Stretch.LARGEST_FROM_TOP, null);
		child4.y = 10;

		Box root = new Box(prefix + "root");
		root.setProp(SizeProperty.FIX, 200);
		root.addChild(child1);
		root.addChild(child2);
		root.addChild(child3);
		root.addChild(child4);
		return root;
	}

	public Stream<Box> streamFlat(Box box) {
		if (box.getChildren().isEmpty()) {
			return Stream.of(box);
		} else {
			Stream<Box> reduce = box.getChildren().stream()
				.map(
						child -> streamFlat(child))
				.reduce(Stream.of(box),
						(s1, s2) -> Stream.concat(s1, s2));
			return reduce;
		}
	}

	// TODO nicht verwendet und vermutlich erstmal unnötig
	private void applyStretchLargestFromTop_TopDown(Box parent, Box box) {

		int miny = box.getChildren().stream().map(b -> b.y)
			// .peek(b -> System.out.println(b))
			.mapToInt(Integer::intValue).min().orElse(0);

		int maxy = box.getChildren().stream().map(b -> b.y)
			// .peek(b -> System.out.println(b))
			.mapToInt(Integer::intValue).max().orElse(0);

		for (Box b : box.getChildren()) {
			if (b.hasProp(Stretch.LARGEST_FROM_TOP)) {
				b.setSize(b.size + maxy);
			}
			applyStretchLargestFromTop_TopDown(box, b);
		}
	}

	private void applyStretchLargest_TopDown(Box parent, Box box) {
		// stretch: Either stretch or use content to size box. Box is box
		// container or content holder.
		// if (parent != null && box.hasP(Stretch.LARGEST)) {
		int largestChild = box.getChildren().stream()
			.map(b -> b.size)
			.peek(b -> System.out.println(b))
			.mapToInt(Integer::intValue)
			.max()
			.orElse(0);

		System.out.println(largestChild);
		System.out.println("-----------");

		for (Box child : box.getChildren()) {
			if (child.hasProp(Stretch.LARGEST)) {
				child.setSize(largestChild);
			}

			applyStretchLargest_TopDown(box, child);
		}
	}

	private void updateBoxSizeFromContentBottomUp(Box parent, Box box) {
		// zuerst die Kinder
		for (Box b : box.getChildren()) {
			updateBoxSizeFromContentBottomUp(box, b);
		}

		// wir schließen aus, dass es eine Box mit content UND Kindern gibt.
		// Entweder Kinder oder content.

		// Anpassung an Kindgröße kommt später! d.h. man könnte folgenden check einbauen:
		// if (!hasChildren)
		box.setSize(box.contentSize);
	}

	private void inheritMaxAndFixSizesTopDown(Box parent, Box box) {
		// erbe maxsize und fix size von parent box.
		if (parent != null) {
			if (parent.hasProp(SizeProperty.FIX)) {
				box.setProp(SizeProperty.MAX, parent.getProp(SizeProperty.FIX));
			} else if (parent.hasProp(SizeProperty.MAX)) {
				box.setProp(SizeProperty.MAX, parent.getProp(SizeProperty.MAX));
			}
		}

		// children space requirements
		for (Box b : box.getChildren()) {
			inheritMaxAndFixSizesTopDown(box, b);
		}
	}

	private void print(Box root, String filename) {
		imageGen.drawimage(graphics -> draw(graphics, 0, root), filename);
	}

	private void draw(Graphics g, int offs, Box root) {

		List<Box> allBoxes = streamFlat(root).collect(Collectors.toList());

		for (int i = 0; i < allBoxes.size(); i++) {
			Box b = allBoxes.get(i);

			// TODO TEST: zeichne boxen nebeneinander, später mit ihren echten x Werten
			g.drawRect(i * b.width, b.y, b.width, b.size);
		}
	}

}
