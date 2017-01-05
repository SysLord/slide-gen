package de.syslord.slidegen.editor.ui.base;

public class BasePresenter<T> {

	protected T model;

	protected void setModel(T model) {
		this.model = model;
	}

}
