package de.syslord.slidegen.editor.util;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

import de.syslord.slidegen.editor.model.UiBox;

public class UiUtil {

	public static UiBox getUiBox(Component component) {
		// assume we have an AbstractComponents which is very much probable as it is the only implementation
		// of Component
		AbstractComponent ac = (AbstractComponent) component;

		Object data = ac.getData();
		return (UiBox) data;
	}
}
