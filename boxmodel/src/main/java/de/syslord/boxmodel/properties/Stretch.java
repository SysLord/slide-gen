package de.syslord.boxmodel.properties;

public enum Stretch {

	// The siblings sizes will not affect this box
	NONE,
	// This box will have the size of the largest sibling
	LARGEST,
	// This box will have the size of the largest sibling + its y value
	LARGEST_FROM_TOP;

}
