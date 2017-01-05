package de.syslord.slidegen.editor.ui.elements;

import com.vaadin.ui.UI;

public class UiObject {

	protected void push(Runnable runnable) {
		UI.getCurrent().access(runnable);
	}
}
