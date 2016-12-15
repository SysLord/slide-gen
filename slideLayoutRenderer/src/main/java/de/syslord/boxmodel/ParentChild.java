package de.syslord.boxmodel;

public class ParentChild {

	private LayoutableBox parent;

	private LayoutableBox child;

	public ParentChild(LayoutableBox parent, LayoutableBox child) {
		this.parent = parent;
		this.child = child;
	}

	public LayoutableBox getParent() {
		return parent;
	}

	public LayoutableBox getChild() {
		return child;
	}

}
