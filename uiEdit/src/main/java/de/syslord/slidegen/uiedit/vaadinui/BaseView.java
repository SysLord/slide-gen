package de.syslord.slidegen.uiedit.vaadinui;

import com.vaadin.navigator.View;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;

public abstract class BaseView<T> extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	protected T model;

	public void setModel(T model) {
		this.model = model;
	}

	public void push(Runnable pushRunnable) {
		UI.getCurrent().access(pushRunnable);
	}

}
