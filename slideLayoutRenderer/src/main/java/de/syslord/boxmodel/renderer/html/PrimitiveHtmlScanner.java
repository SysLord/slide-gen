package de.syslord.boxmodel.renderer.html;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import com.google.common.collect.Lists;

import de.syslord.boxmodel.renderer.FontProvider;

/**
 *
 * Understands <i> and <b> tags and throws everything else away
 *
 */
public class PrimitiveHtmlScanner {

	public static HtmlScannerResult generateAttributedString(String html, Font font) {

		List<AttributedStringInfo> attributedStringInfos = parse(html);

		String plainText = getPlainText(attributedStringInfos);

		AttributedString attributedString = toAttributedString(attributedStringInfos, plainText, font);

		return new HtmlScannerResult(plainText, attributedString);
	}

	private static AttributedString toAttributedString(
			List<AttributedStringInfo> attributedStringInfos,
			String plainText,
			Font font) {

		AttributedString attributedString = new AttributedString(plainText);
		attributedString.addAttribute(TextAttribute.FONT, font);

		int index = 0;
		for (AttributedStringInfo info : attributedStringInfos) {
			int length = info.getLength();

			// i don't know what kind of Font is required to make make TextAttribute.WEIGHT work, so for now
			// we generate the font we need.
			attributedString.addAttribute(TextAttribute.FONT, FontProvider.getStyled(
					font,
					info.isBold(), info.isItalic()),
					index, index + length);

			index += length;
		}

		return attributedString;
	}

	private static String getPlainText(List<AttributedStringInfo> attributedStringInfos) {
		String wholeString = attributedStringInfos.stream()
			.map(info -> info.getString())
			.collect(Collectors.joining());
		return wholeString;
	}

	protected static List<AttributedStringInfo> parse(String simpleHtml) {
		Document document = Jsoup.parseBodyFragment(simpleHtml);
		return visitCreatingAttributedStringInfos(document);
	}

	private static List<AttributedStringInfo> visitCreatingAttributedStringInfos(Document document) {
		List<AttributedStringInfo> strings = Lists.newArrayList();

		document.traverse(new NodeVisitor() {

			boolean bold = false;

			boolean italic = false;

			// entering a node
			@Override
			public void head(Node node, int depth) {
				if (isTag(node, "br")) {
					AttributedStringInfo attributedStringInfo = new AttributedStringInfo("\n", bold, italic);
					strings.add(attributedStringInfo);

				} else if (isTag(node, "i")) {
					italic = true;

				} else if (isTag(node, "b")) {
					bold = true;

				} else if (node instanceof TextNode) {
					TextNode textNode = (TextNode) node;

					String wholeText = textNode.getWholeText();
					String removedNewLines = wholeText.replace("\n", " ");
					AttributedStringInfo attributedStringInfo = new AttributedStringInfo(removedNewLines, bold, italic);
					strings.add(attributedStringInfo);
				}
			}

			// leaving a node
			@Override
			public void tail(Node node, int depth) {
				if (isTag(node, "i")) {
					italic = false;
				} else if (isTag(node, "b")) {
					bold = false;
				}
			}

		});

		return strings;
	}

	private static boolean isTag(Node node, String tagname) {
		return (node instanceof Element)
				&& ((Element) node).tag().getName().equals(tagname);
	}

}
