package de.syslord.boxmodel;

public enum Stretch {

	// The siblings sizes will not affect this box
	NONE,
	// This box will have the size of the largest sibling
	LARGEST,
	// This box will increase its by the size increased of the largest sibling
	GROW_LARGEST;

}
