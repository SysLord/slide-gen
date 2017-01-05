package de.syslord.slidegen.editor.ui.elements;

import com.vaadin.ui.AbstractComponent;

public class UiTextBox extends UiBox {

	public UiTextBox(AbstractComponent component) {
		super(component, new UiTextBoxStyleData());
	}

	public UiTextBoxStyleData getUiTextBoxStyleData() {
		return (UiTextBoxStyleData) super.getUiStyleData();
	}

	@Override
	public String getTreeCaption() {
		return "Text: " + getValue();
	}

}
