package de.syslord.boxmodel.renderer;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class EllipsisText {

	private int heightNeeded;

	private List<Integer> lines;

	private String text;

	private int lastLineStart;

	private int lastLineEnd;

	public EllipsisText(String text, int heightNeeded, List<Integer> lines) {
		this.text = text;
		this.heightNeeded = heightNeeded;
		this.lines = lines;

		lastLineStart = lines.size() > 1 ? lines.get(lines.size() - 2) : 0;
		lastLineEnd = lines.size() > 0 ? lines.get(lines.size() - 1) : 0;
	}

	public int getHeightNeeded() {
		return heightNeeded;
	}

	public List<Integer> getLines() {
		return lines;
	}

	@Override
	public String toString() {
		return "TextRenderLines [heightNeeded=" + heightNeeded + ", lines=" + lines + "]" + toNiceString();
	}

	private String toNiceString() {
		List<String> s = Lists.newArrayList();

		int lastIndex = 0;
		for (Integer index : lines) {
			s.add(text.substring(lastIndex, index));
			lastIndex = index;
		}
		return s.stream().collect(Collectors.joining(","));
	}

	public String getText() {
		return text;
	}

	public int getLastFittingLineStart() {
		return lastLineStart;
	}

	public int getLastFittingLineEnd() {
		return lastLineEnd;
	}

}
