package de.syslord.boxmodel.layouter;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import org.junit.Test;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.Lorem;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.Stretch;
import de.syslord.boxmodel.TextBox;
import de.syslord.boxmodel.renderer.FontProvider;

public class LayouterTest {

	private static final int CHILD_ROOT_HEIGHT = 1000;

	private static final String EMPTY = "";

	private static final String LONG_TEXT = Lorem.longIpsum;

	private Font font = FontProvider.getDefaultFont();

	@Test
	public void testLayout_BoxWithMinSizeWillBeMinSize() throws Exception {
		int min = 200;

		LayoutableBox root = createTextWithHeight(100, EMPTY);
		root.setProp(HeightProperty.MAX, 400);
		root.setProp(HeightProperty.MIN, min);

		Layouter.layout(root);

		assertEquals(min, root.getHeight());
	}

	@Test
	public void testLayout_WhenMinAndMaxThenSizeIsFixWithLongText() throws Exception {
		int fixExpected = 400;

		LayoutableBox root = createTextWithHeight(100, LONG_TEXT);
		root.setProp(HeightProperty.MIN, fixExpected);
		root.setProp(HeightProperty.MAX, fixExpected);

		Layouter.layout(root);

		assertEquals(fixExpected, root.getHeight());
	}

	@Test
	public void testLayout_WhenMinAndMaxThenSizeIsFixWithShortText() throws Exception {
		int fixExpected = 400;

		LayoutableBox root = createTextWithHeight(100, EMPTY);
		root.setProp(HeightProperty.MIN, fixExpected);
		root.setProp(HeightProperty.MAX, fixExpected);

		Layouter.layout(root);

		assertEquals(fixExpected, root.getHeight());
	}

	@Test
	public void testLayout_BoxWithMaxSizeWillNotGrowBeyondMaxSize() throws Exception {
		int max = 400;

		LayoutableBox root = createTextWithHeight(100, LONG_TEXT);
		root.setProp(HeightProperty.MAX, max);
		root.setProp(HeightProperty.MIN, 200);

		Layouter.layout(root);

		assertEquals(max, root.getHeight());
	}

	@Test
	public void testLayout_SiblingWithStretchLargestWillBeAsLargeAsTheLargestSibling() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(100, 50, LONG_TEXT, 200, 60, EMPTY);
		boxes[1].setProp(HeightProperty.MAX, 500);
		boxes[2].setProp(Stretch.LARGEST);

		Layouter.layout(boxes[0]);

		assertEquals(500, boxes[2].getHeight());
	}

	@Test
	public void testLayout_SiblingWithGrowLargestWillGrowWithTheLargestSibling() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(100, 50, LONG_TEXT, 200, 60, EMPTY);
		boxes[1].setProp(HeightProperty.MAX, 500);
		boxes[2].setProp(HeightProperty.MIN, 200);
		boxes[2].setProp(Stretch.GROW_LARGEST);

		Layouter.layout(boxes[0]);

		assertEquals(500, boxes[1].getHeight());
		assertEquals(500 - 100 + 200, boxes[2].getHeight());
	}

	@Test
	public void testLayout_SiblingWithGrowLargestWillGrowWithTheLargestSiblingButMayAlsoShrinkWithContent() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(100, 50, LONG_TEXT, 200, 60, EMPTY);
		boxes[1].setProp(HeightProperty.MAX, 500);
		boxes[2].setProp(Stretch.GROW_LARGEST);

		Layouter.layout(boxes[0]);

		assertEquals(500, boxes[1].getHeight());
		assertEquals(500 - 100 + 0, boxes[2].getHeight());
	}

	@Test
	public void testLayout_WithoutStretchPropertyTheSiblingsWillNotInteract() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(100, 50, LONG_TEXT, 200, 60, EMPTY);
		boxes[1].setProp(HeightProperty.MAX, 500);

		Layouter.layout(boxes[0]);

		assertEquals(500, boxes[1].getHeight());
		assertEquals(0, boxes[2].getHeight());
	}

	@Test
	public void testLayout_FloatUp_ChildWillFloatUpwardsBy100IfPredecessorShrinksBy100() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(500, 50, EMPTY, 200, 560, EMPTY);
		boxes[1].setProp(HeightProperty.MIN, 400);
		boxes[1].setProp(HeightProperty.MAX, 400);
		boxes[2].setProp(PositionProperty.FLOAT_UP);

		Layouter.layout(boxes[0]);

		assertEquals(400, boxes[1].getHeight());
		assertEquals(560 - (500 - 400), boxes[2].getY());
	}

	@Test
	public void testLayout_FloatDown_ChildWillFloatDownwardsBy200IfPredecessorGrowsBy200() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(200, 50, EMPTY, 100, 260, EMPTY);
		boxes[1].setProp(HeightProperty.MIN, 400);
		boxes[1].setProp(HeightProperty.MAX, 400);
		boxes[2].setProp(PositionProperty.FLOAT_DOWN);

		Layouter.layout(boxes[0]);

		assertEquals(400, boxes[1].getHeight());
		assertEquals(260 + 400 - 200, boxes[2].getY());
	}

	@Test
	public void testLayout_FloatUp_Child1HasNoPredecessorAndWontFloat_FloatNeedsProperChildOrder() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(200, 560, EMPTY, 500, 50, EMPTY);
		boxes[2].setProp(HeightProperty.MIN, 400);
		boxes[2].setProp(HeightProperty.MAX, 400);
		boxes[1].setProp(PositionProperty.FLOAT_UP);

		Layouter.layout(boxes[0]);

		assertEquals(400, boxes[2].getHeight());
		assertEquals(560, boxes[1].getY());
	}

	@Test
	public void testLayout_BoxWillNotOverflowParentNoMatterWhat() throws Exception {
		LayoutableBox[] boxes = createWith2Children_HeightYContent(0, 0, EMPTY, 10000, 10, EMPTY);
		boxes[1].setProp(HeightProperty.MIN, 200);
		boxes[2].setProp(HeightProperty.MIN, 10000);
		boxes[2].setProp(PositionProperty.FLOAT_DOWN);

		Layouter.layout(boxes[0]);

		assertEquals(200, boxes[1].getHeight());
		assertEquals(10 + 200, boxes[2].getY());
		assertEquals(CHILD_ROOT_HEIGHT - boxes[2].getY(), boxes[2].getHeight());
	}

	private LayoutableBox createBoxWithHeight(int height) {
		return new LayoutableBox("", 50, 50, 500, height);
	}

	private TextBox createTextWithHeight(int height, String con) {
		return new TextBox("", con, font, 50, 50, 500, height);
	}

	private TextBox createTextWithHeightAndY(int height, int y, String con) {
		return new TextBox("", con, font, 50, y, 500, height);
	}

	private LayoutableBox[] createWith2Children_HeightYContent(int height1, int y1, String content1, int height2, int y2,
			String content2) {
		LayoutableBox root = createBoxWithHeight(CHILD_ROOT_HEIGHT);
		root.setProp(HeightProperty.MAX, CHILD_ROOT_HEIGHT);
		root.setProp(HeightProperty.MIN, CHILD_ROOT_HEIGHT);

		LayoutableBox child1 = createTextWithHeightAndY(height1, y1, content1);
		LayoutableBox child2 = createTextWithHeightAndY(height2, y2, content2);

		root.addChild(child1);
		root.addChild(child2);
		return new LayoutableBox[] { root, child1, child2 };
	}

}
