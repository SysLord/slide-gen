package de.syslord.slidegen.editor.base;

import com.vaadin.ui.AbstractComponent;

import de.syslord.slidegen.editor.model.UiTextBoxStyleData;

public class UiTextBox extends UiBox {

	public UiTextBox(AbstractComponent component) {
		super(component, new UiTextBoxStyleData());
	}

	public UiTextBoxStyleData getUiTextBoxStyleData() {
		return (UiTextBoxStyleData) super.getUiStyleData();
	}

}
