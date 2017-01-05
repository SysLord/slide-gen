package de.syslord.slidegen.editor.persisting;

import java.awt.Color;

import org.junit.Test;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.LineBox;
import de.syslord.boxmodel.TextBox;

public class JavaCodeExporterTest {

	@Test
	public void testJavaExporter() throws Exception {

		String export = new JavaCodeExporter().export(createExampleBoxes(), "Bla", "dynload");
		System.out.println(export);
	}

	public LayoutableBox createExampleBoxes() {
		LayoutableBox root = new LayoutableBox("root", 1, 2, 3, 4);
		LayoutableBox nest = new LayoutableBox("nest", 11, 22, 33, 44);
		LayoutableBox another = new LayoutableBox("nest", 9, 8, 7, 6);
		LayoutableBox t1 = new TextBox("1", "content", 111, 222, 333, 444);
		LayoutableBox line = new LineBox("line öä ?", 0, 40, 200, 20, Color.GRAY, 4);

		root.addChild(nest);
		root.addChild(another);
		nest.addChild(t1);
		nest.addChild(line);

		return root;
	}

}
