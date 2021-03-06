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

	@Override
	public String toString() {
		return String.format("%s %s",
				parent == null ? "null" : parent.getName(),
				child == null ? "null" : child.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (child == null ? 0 : child.hashCode());
		result = prime * result + (parent == null ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ParentChild other = (ParentChild) obj;
		if (child == null) {
			if (other.child != null) {
				return false;
			}
		} else if (!child.equals(other.child)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}

}
