package de.syslord.slidegen.editor.vaadinui;

public class BasePresenter<T> {

	protected T model;

	protected void setModel(T model) {
		this.model = model;
	}

}
