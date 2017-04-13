package de.syslord.boxmodel.renderer.html;

import java.text.AttributedString;

public class HtmlScannerResult {

	private String plainText;

	private AttributedString attributedString;

	public HtmlScannerResult(String plainText, AttributedString attributedString) {
		this.plainText = plainText;
		this.attributedString = attributedString;
	}

	public String getPlainText() {
		return plainText;
	}

	public AttributedString getAttributedString() {
		return attributedString;
	}

}
