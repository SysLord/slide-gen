package de.syslord.boxmodel;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.syslord.boxmodel.renderer.RenderType;
import de.syslord.boxmodel.renderer.RenderableBoxImpl;

public class LayoutableBox {

	private Map<Object, Integer> layoutProperties = new HashMap<>();

	private List<LayoutableBox> children = new ArrayList<>();

	protected String name;

	protected ByteArrayInputStream backgroundImage;

	protected Color foregroundColor = Color.BLACK;

	// fix values
	protected int x = 0;

	protected int initialY = 0;

	protected int width = 0;

	protected int initialHeight = 0;

	// calculated values
	protected int heightNeeded = 0;

	protected int height = 0;

	protected int y = 0;

	protected int absoluteY = 0;

	protected int absoluteX;

	protected boolean visible = true;

	protected String styleIdentifier = "";

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
		box.setProp(HeightProperty.MIN, height);
		box.setProp(HeightProperty.MAX, height);
		return box;
	}

	public RenderableBoxImpl toRenderable() {
		RenderableBoxImpl renderableBoxImpl = new RenderableBoxImpl(
				absoluteX, absoluteY, width, height,
				visible);

		renderableBoxImpl.setColor(foregroundColor);
		renderableBoxImpl.setBackgroundImage(backgroundImage);

		renderableBoxImpl.setRenderType(RenderType.BOX);
		return renderableBoxImpl;
	}

	public void applyStyle(Style style) {
		style.getColor(styleIdentifier).ifPresent(c -> foregroundColor = c);

		children.stream().forEach(child -> child.applyStyle(style));
	}

	public int getHeightChanged() {
		return height - initialHeight;
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

	public void addChild(LayoutableBox child) {
		children.add(child);
	}

	public void addChildren(LayoutableBox... newChildren) {
		for (LayoutableBox child : newChildren) {
			children.add(child);
		}
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

	public void setHeight(int set, boolean allowShrinking) {
		int newsize = set;

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
			height = newsize;
		}
	}

	public void updateSizeIncludingYLimits(int parentHeight) {
		int maxPossibleHeight = parentHeight - y;

		// for example lines may have height 0
		if (maxPossibleHeight < 0) {
			visible = false;
		}

		if (height > maxPossibleHeight) {
			height = maxPossibleHeight;
		}
	}

	public void setY(int set) {
		int newY = set;

		if (newY < y && hasProp(PositionProperty.FLOAT_UP)) {
			// float only up to 0
			this.y = Math.max(0, newY);
		} else if (newY > y && hasProp(PositionProperty.FLOAT_DOWN)) {
			// floating off parent area downwards is allowed
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

	public int getHeightNeeded() {
		return heightNeeded;
	}

	public int getInitialHeight() {
		return initialHeight;
	}

	public int getHeight() {
		return height;
	}

	public int getY() {
		return y;
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

	public void setBackgroundImage(ByteArrayInputStream backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public ByteArrayInputStream getBackgroundImage() {
		return backgroundImage;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setStyleName(String styleIdentifier) {
		this.styleIdentifier = styleIdentifier;
	}

}
