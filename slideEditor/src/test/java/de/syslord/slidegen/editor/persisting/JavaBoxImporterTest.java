package de.syslord.slidegen.editor.persisting;

import org.junit.Test;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.Stretch;

public class JavaBoxImporterTest {

	JavaBoxImporter javaBoxImporter = new JavaBoxImporter();

	@Test
	public void testName() throws Exception {
		LayoutableBox exampleBoxes = new JavaCodeExporterTest().createExampleBoxes();
		exampleBoxes.setProp(Stretch.LARGEST);

		String packageName = "dynload";
		String className = "Bla";

		// String code2 = new JavaExporter().export(exampleBoxes, className, packageName);

		int i = 0;
		// while (10 % 3 == 0) {
		// ThrowAwayClassLoader classLoader = new ThrowAwayClassLoader(getClass().getClassLoader());
		exampleBoxes.setProp(PositionProperty.FLOAT_UP, i);
		++i;
		String code = new JavaCodeExporter().export(exampleBoxes, className, packageName);

		LayoutableBox load = javaBoxImporter.load(code);
		Thread.sleep(1);
		System.out.println(load.getProp(PositionProperty.FLOAT_UP));
		// }

		// LayoutableBox load = javaBoxImporter.load(packageName + "." + className, code);

		// System.out.println(new JavaExporter().export(load, className, packageName));

		// LayoutableBox load2 = javaBoxImporter.load(packageName + "." + className, code2);
		// System.out.println(new JavaExporter().export(load2, className, packageName));
	}

}
