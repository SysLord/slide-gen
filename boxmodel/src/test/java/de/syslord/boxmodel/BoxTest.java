package de.syslord.boxmodel;

public class BoxTest {

	// private ImageGenerator imageGen = new ImageGenerator();
	//
	// @Test
	// public void testName() throws Exception {
	// LayoutableBox root = createSomeTestBoxes("top ");
	// // for (LayoutableBox box : root.getChildren()) {
	// // box.addChild(createSomeTestBoxes("nest "));
	// // }
	//
	// root.streamFlat().forEach(x -> System.out.println(x.getName()));
	//
	// print(root, "d");
	// }
	//
	// private LayoutableBox createSomeTestBoxes(String prefix) {
	// LayoutableBox child1 = new LayoutableBox(prefix + "1");
	// child1.contentSize = 5;
	//
	// LayoutableBox child2 = new LayoutableBox(prefix + "2");
	// child2.setProp(Stretch.LARGEST, null);
	// child2.y = 10;
	//
	// LayoutableBox child3 = new LayoutableBox(prefix + "3");
	// child3.setProp(Stretch.LARGEST_FROM_TOP, null);
	// child3.setProp(Stretch.LARGEST, null);
	//
	// LayoutableBox child4 = new LayoutableBox(prefix + "4");
	// child4.setProp(Stretch.LARGEST, null);
	// child4.setProp(Stretch.LARGEST_FROM_TOP, null);
	// child4.y = 10;
	//
	// LayoutableBox root = new LayoutableBox(prefix + "root");
	// root.setProp(SizeProperty.FIX, 200);
	// root.addChild(child1);
	// root.addChild(child2);
	// root.addChild(child3);
	// root.addChild(child4);
	// return root;
	// }

	// // TODO nicht verwendet und vermutlich erstmal unnötig
	// private void applyStretchLargestFromTop_TopDown(LayoutableBox parent, LayoutableBox box) {
	//
	// int miny = box.getChildren().stream().map(b -> b.y)
	// // .peek(b -> System.out.println(b))
	// .mapToInt(Integer::intValue).min().orElse(0);
	//
	// int maxy = box.getChildren().stream().map(b -> b.y)
	// // .peek(b -> System.out.println(b))
	// .mapToInt(Integer::intValue).max().orElse(0);
	//
	// for (LayoutableBox b : box.getChildren()) {
	// if (b.hasProp(Stretch.LARGEST_FROM_TOP)) {
	// b.setSize(b.size + maxy);
	// }
	// applyStretchLargestFromTop_TopDown(box, b);
	// }
	// }
	//
	// private void print(LayoutableBox root, String filename) {
	// imageGen.drawimage(graphics -> draw(graphics, 0, root), filename);
	// }
	//
	// private void draw(Graphics g, int offs, LayoutableBox root) {
	//
	// List<LayoutableBox> allBoxes = streamFlat(root).collect(Collectors.toList());
	//
	// for (int i = 0; i < allBoxes.size(); i++) {
	// LayoutableBox b = allBoxes.get(i);
	//
	// // TODO TEST: zeichne boxen nebeneinander, später mit ihren echten x Werten
	// g.drawRect(i * b.width, b.y, b.width, b.size);
	// }
	// }

}
