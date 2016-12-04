package de.syslord.boxmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.syslord.boxmodel.render.RenderType;
import de.syslord.boxmodel.render.RenderableBox;
import de.syslord.boxmodel.render.RenderableBoxImpl;

public class LayoutableBox {

	private Map<Object, Object> layoutProperties = new HashMap<>();

	private List<LayoutableBox> children = new ArrayList<>();

	private String name;

	// fix values
	protected int x = 0;

	protected int initialY = 0;

	protected int width = 0;

	protected int initialHeight = 0;

	protected int margin = 0;

	protected int padding = 0;

	// calculated values
	protected int heightNeeded = 0;

	protected int height = 0;

	protected int y = 0;

	protected int absoluteY = 0;

	protected int absoluteX;

	protected boolean visible = true;

	//

	public LayoutableBox(String name, int x, int y, int width, int height) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.initialY = y;
		this.width = width;
		this.height = height;
		this.initialHeight = height;
	}

	public RenderableBox toRenderable() {
		return new RenderableBoxImpl("", absoluteX, absoluteY, width, height, margin, padding, null, visible, RenderType.BOX,
				null);
	}

	public int getGrownHeight() {
		int v = height - initialHeight;
		if (v < 0) {
			System.out.println(name + " shrunk!!");
		}
		return Math.max(0, v);
	}

	public int getContentX() {
		return x + margin + padding;
	}

	public int getContentY() {
		return y + margin + padding;
	}

	public int getContentWidth() {
		return Math.max(0, width - 2 * (margin + padding));
	}

	public int getContentHeight() {
		return Math.max(0, height - 2 * (margin + padding));
	}

	public void setHeightNeeded(int heightNeeded) {
		this.heightNeeded = heightNeeded;
	}

	public void setProp(Object property, Object value) {
		layoutProperties.put(property, value);
	}

	public void setProp(Object property) {
		layoutProperties.put(property, null);
	}

	public void addChild(LayoutableBox b) {
		children.add(b);
	}

	public List<LayoutableBox> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public boolean hasProp(Object key) {
		return layoutProperties.containsKey(key);
	}

	public boolean hasProp(Object key, Object value) {
		return layoutProperties.containsKey(key) && layoutProperties.get(key) == value;
	}

	public Object getProp(Object key) {
		return layoutProperties.get(key);
	}

	public void setSize(int set, boolean allowShrinking) {
		int newsize = set;

		if (hasProp(SizeProperty.FIX)) {
			int fix = (int) getProp(SizeProperty.FIX);
			newsize = fix;
		}
		if (hasProp(SizeProperty.MIN)) {
			int min = (int) getProp(SizeProperty.MIN);
			if (newsize < min) {
				newsize = min;
			}
		}
		if (hasProp(SizeProperty.MAX)) {
			int max = (int) getProp(SizeProperty.MAX);
			if (newsize > max) {
				newsize = Math.max(max, 0);
			}
		}

		if (allowShrinking || newsize > height) {

			if (newsize < height) {
				System.out.println("shrinking!");
			}
			height = newsize;
		}
	}

	public void updateSizeIncludingYLimits(int parentHeight) {
		int maxPossibleHeight = parentHeight - y;

		if (maxPossibleHeight < 0) {
			visible = false;
		}

		int newsize = height;

		if (hasProp(SizeProperty.FIX)) {
			if (maxPossibleHeight < height) {
				// TODO
				// if (printFixIfCropped) {
				// __size smaller
				newsize = maxPossibleHeight;
				// } else {
				// __remove completely
				// visible=false;
				// }
			}
		} else if (hasProp(SizeProperty.MIN)) {
			if (maxPossibleHeight < height) {
				// TODO
				// if (printMinIfCropped) {
				// __size smaller
				newsize = maxPossibleHeight;
				// } else {
				// __remove completely
				// visible=false;
				// }
			}
		} else if (maxPossibleHeight < height) {
			newsize = maxPossibleHeight;
		}
		height = newsize;
	}

	public Stream<LayoutableBox> streamFlat() {
		if (getChildren().isEmpty()) {
			return Stream.of(this);
		} else {
			Stream<LayoutableBox> reduce = getChildren().stream()
				.map(child -> child.streamFlat())
				.reduce(Stream.of(this), (s1, s2) -> Stream.concat(s1, s2));
			return reduce;
		}
	}

	public int getWidth() {
		return width;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getMargin() {
		return margin;
	}

	public int getPadding() {
		return padding;
	}

	public int getHeightNeeded() {
		return heightNeeded;
	}

	public int getHeight() {
		return height;
	}

	public int getY() {
		return y;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getAbsoluteY() {
		return absoluteY;
	}

	public int getAbsoluteX() {
		return absoluteX;
	}

	public void setAbsoluteY(int absoluteY) {
		this.absoluteY = absoluteY;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setAbsoluteX(int absoluteX) {
		this.absoluteX = absoluteX;
	}

	public boolean isVisible() {
		return visible;
	}

}
