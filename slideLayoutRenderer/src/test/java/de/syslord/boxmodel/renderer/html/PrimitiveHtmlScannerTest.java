package de.syslord.boxmodel.renderer.html;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class PrimitiveHtmlScannerTest {

	@Test
	public void testParse() throws Exception {
		List<AttributedStringInfo> infos = PrimitiveHtmlScanner.parse("normal<b>bold</b><i>italic</i><b><i>bolditalic</i></b>");

		assertEquals(new AttributedStringInfo("normal", false, false), infos.get(0));
		assertEquals(new AttributedStringInfo("bold", true, false), infos.get(1));

		assertEquals(new AttributedStringInfo("italic", false, true), infos.get(2));
		assertEquals(new AttributedStringInfo("bolditalic", true, true), infos.get(3));
	}

}