package de.syslord.slidegen.editor.ui.base;

import com.vaadin.ui.TextField;

public class CustomTextField extends TextField {

	private static final long serialVersionUID = 2454828565708104752L;

	private boolean fireValueChange = true;

	public CustomTextField(String caption) {
		super(caption);
	}

	@Override
	protected void fireValueChange(boolean repaintIsNotNeeded) {
		if (fireValueChange) {
			super.fireValueChange(repaintIsNotNeeded);
		}
	}

	public void setFireOnValueChange(boolean fireValueChange) {
		this.fireValueChange = fireValueChange;
	}

}
