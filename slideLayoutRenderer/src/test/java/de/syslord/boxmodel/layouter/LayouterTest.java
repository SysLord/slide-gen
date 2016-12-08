package de.syslord.boxmodel.layouter;

import static org.junit.Assert.*;

import java.awt.Font;

import org.junit.Test;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.Lorem;
import de.syslord.boxmodel.TextBox;
import de.syslord.boxmodel.renderer.FontProvider;

public class LayouterTest {

	private Font font = FontProvider.getDefaultFont();

	@Test
	public void testLayout_MinSize() throws Exception {
		int initialHeight = 100;
		LayoutableBox root = new TextBox("root", "", font, 50, 50, 600, initialHeight);
		root.setProp(HeightProperty.MAX, 400);
		root.setProp(HeightProperty.MIN, 200);

		Layouter.layout(root);

		assertEquals(200, root.getHeight());
	}

	@Test
	public void testLayout_MaxSize() throws Exception {
		int initialHeight = 100;
		LayoutableBox root = new TextBox("root", Lorem.longIpsum, font, 50, 50, 200, initialHeight);
		root.setProp(HeightProperty.MAX, 400);
		root.setProp(HeightProperty.MIN, 200);

		Layouter.layout(root);

		assertEquals(400, root.getHeight());
	}

	// TODO this happens by accident and could break anytime!
	@Test
	public void testLayout_FixSizeDoesNotAffectRootBox() throws Exception {
		int initialHeight = 100;
		LayoutableBox root = new TextBox("root", Lorem.longIpsum, font, 50, 50, 200, initialHeight);
		root.setProp(HeightProperty.FIX, 333);

		Layouter.layout(root);

		assertEquals(333, root.getHeight());
	}
}
