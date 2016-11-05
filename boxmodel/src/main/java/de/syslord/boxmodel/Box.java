package de.syslord.boxmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.syslord.boxmodel.properties.SizeProperty;

public class Box {

	private Map<Object, Object> properties = new HashMap<>();

	private List<Box> children = new ArrayList<>();

	String name;

	// initial values
	int contentSize = 0;

	int width = 20;

	int childStretch = 0;

	// errechnete Werte
	int size = 0;

	int y = 0;

	int x = 0;

	public Box(String name) {
		this.name = name;
	}

	public void setProp(Object property, Object value) {
		properties.put(property, value);
	}

	public void addChild(Box b) {
		children.add(b);
	}

	public List<Box> getChildren() {
		return children;
	}

	public boolean hasProp(Object key) {
		return properties.containsKey(key);
	}

	public boolean hasP(Object key, Object value) {
		return properties.containsKey(key) && properties.get(key) == value;
	}

	public Object getProp(Object key) {
		return properties.get(key);
	}

	public int getYSpaceUsed() {
		return y + size;
	}

	public int getWidth() {
		return width;
	}

	public void setSize(int set) {

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
			if (newsize + y > max) {
				newsize = Math.max(max - y, 0);
			}
		}
		size = newsize;
	}
}
