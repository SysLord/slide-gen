package de.syslord.boxmodel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.stream.Collectors;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

public class LayoutableBoxTest {

	private LayoutableBox root;

	private LayoutableBox line;

	private TextBox t1;

	private LayoutableBox nest;

	private LayoutableBox onlyMyself = new LayoutableBox("noChildren", 0, 0, 0, 0);

	@Before
	public void setUp() {
		root = new LayoutableBox("root", 0, 0, 0, 0);
		nest = new LayoutableBox("nest", 0, 0, 0, 0);
		t1 = new TextBox("t1", "content", 0, 0, 0, 0);
		line = new LineBox("line", 0, 40, 200, 20, Color.GRAY, 4);

		root.addChild(nest);
		nest.addChild(t1);
		nest.addChild(line);
	}

	@Test
	public void streamFlatProducesRootBoxIfNoChildren() throws Exception {
		assertThat(onlyMyself.streamFlat().collect(Collectors.toList()), containsInAnyOrder(onlyMyself));
	}

	@Test
	public void streamFlatProducesAllChildren() throws Exception {
		assertThat(root.streamFlat().collect(Collectors.toList()), containsInAnyOrder(root, line, t1, nest));
	}

	@Test
	public void streamFlatWithClassParamProducesAllChildrenOfThatClass() throws Exception {
		assertThat(root.streamFlat(LayoutableBox.class).collect(Collectors.toList()), containsInAnyOrder(root, line, t1, nest));

		assertThat(root.streamFlat(LineBox.class).collect(Collectors.toList()), containsInAnyOrder(line));
		assertThat(root.streamFlat(TextBox.class).collect(Collectors.toList()), containsInAnyOrder(t1));
	}

	@Test
	public void streamFlatWithClassParamWithNoChildren() throws Exception {
		assertThat(onlyMyself.streamFlat(LayoutableBox.class).collect(Collectors.toList()), containsInAnyOrder(onlyMyself));

		assertThat(onlyMyself.streamFlat(LineBox.class).collect(Collectors.toList()), is(emptyIterable()));
		assertThat(onlyMyself.streamFlat(TextBox.class).collect(Collectors.toList()), is(emptyIterable()));
	}

	@Test
	public void testStreamFlatWithParents_noChildren() throws Exception {
		assertThat(onlyMyself.streamFlatWithParents().collect(Collectors.toList()), is(emptyIterable()));
	}

	@Test
	public void testStreamFlatWithParents() throws Exception {
		ParentChild pc1 = new ParentChild(root, nest);
		ParentChild pc2 = new ParentChild(nest, t1);
		ParentChild pc3 = new ParentChild(nest, line);

		assertThat(root.streamFlatWithParents().collect(Collectors.toList()),
				IsIterableContainingInOrder.contains(pc1, pc2, pc3));
	}

	@Test
	public void testChildren() throws Exception {
		LayoutableBox box = createBox();
		LayoutableBox child1 = createBox();
		LayoutableBox child2 = createBox();
		LayoutableBox child3 = createBox();

		box.addChild(child1);
		box.addChildren(child2, child3);

		assertThat(box.getChildren(), containsInAnyOrder(child1, child2, child3));

		box.removeChild(child2);

		assertThat(box.getChildren(), containsInAnyOrder(child1, child3));
	}

	@Test
	public void TestIsCropped_NeverWhenNeededHeightZero() throws Exception {
		LayoutableBox box = createBox();
		box.setHeightNeeded(0);

		assertFalse(box.isCropped());
	}

	@Test
	public void TestIsCropped_WhenSmallerHeightThanNeeded() throws Exception {
		LayoutableBox cropped = createBox();
		cropped.setHeightNeeded(20);
		cropped.setHeight(10, true);

		LayoutableBox notCropped = createBox();
		notCropped.setHeightNeeded(20);
		notCropped.setHeight(30, true);

		assertTrue(cropped.isCropped());
		assertFalse(notCropped.isCropped());
	}

	private LayoutableBox createBox() {
		return new LayoutableBox("box", 0, 0, 0, 0);
	}

}
