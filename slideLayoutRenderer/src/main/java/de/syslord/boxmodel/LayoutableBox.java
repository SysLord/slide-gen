package de.syslord.boxmodel;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBox;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class LayoutableBox {

	private Map<Object, Integer> layoutProperties = new HashMap<>();

	private List<LayoutableBox> children = new ArrayList<>();

	protected String name;

	protected ByteArrayInputStream image;

	protected Color foregroundColor;

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

	public static LayoutableBox createFixedHeightBox(String name, int x, int y, int width, int height) {
		LayoutableBox box = new LayoutableBox(name, x, y, width, height);
		box.setProp(HeightProperty.FIX, height);
		return box;
	}

	public RenderableBox toRenderable() {
		return new RenderableBoxImpl(
				"",
				absoluteX, absoluteY, width, height,
				margin, padding, null,
				visible, RenderType.BOX,
				foregroundColor);
	}

	public int getHeightChanged() {
		int v = height - initialHeight;
		if (v < 0) {
			System.out.println(name + " shrunk!!");
		}
		// TODO maybe needs property: mayRise or something
		// return Math.max(0, v);
		return v;
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

	public void setProp(Object property, Integer value) {
		if (value == null) {
			layoutProperties.remove(property);
		} else {
			layoutProperties.put(property, value);
		}
	}

	public void setProp(Object property) {
		if (property != null) {
			layoutProperties.put(property, null);
		}
	}

	public void setPropIf(Object property, boolean doSet) {
		if (doSet) {
			layoutProperties.put(property, null);
		}
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

	public Integer getProp(Object key) {
		return layoutProperties.get(key);
	}

	public void setSize(int set, boolean allowShrinking) {
		int newsize = set;

		if (hasProp(HeightProperty.FIX)) {
			int fix = getProp(HeightProperty.FIX);
			newsize = fix;
		}
		if (hasProp(HeightProperty.MIN)) {
			int min = getProp(HeightProperty.MIN);
			if (newsize < min) {
				newsize = min;
			}
		}
		if (hasProp(HeightProperty.MAX)) {
			int max = getProp(HeightProperty.MAX);
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

		if (hasProp(HeightProperty.FIX)) {
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
		} else if (hasProp(HeightProperty.MIN)) {
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

	public void setY(int set) {
		int newY = set;

		if (newY < y && hasProp(PositionProperty.FLOAT_UP)) {
			this.y = newY;
		} else if (newY > y && hasProp(PositionProperty.FLOAT_DOWN)) {
			this.y = newY;
		}
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

	public void setAbsoluteX(int absoluteX) {
		this.absoluteX = absoluteX;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setBackgroundImage(ByteArrayInputStream image) {
		this.image = image;
	}

	public ByteArrayInputStream getImage() {
		return image;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

}
