package de.syslord.boxmodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParentChildTest {

	@Test
	public void testEqualsByParentChildInstance() throws Exception {
		LayoutableBox parent = new LayoutableBox("", 0, 0, 0, 0);
		LayoutableBox child = new LayoutableBox("", 0, 0, 0, 0);

		ParentChild instance1 = new ParentChild(parent, child);
		ParentChild instance2 = new ParentChild(parent, child);

		assertEquals(instance1, instance2);
	}
}
