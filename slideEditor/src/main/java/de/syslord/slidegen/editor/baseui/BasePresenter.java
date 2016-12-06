package de.syslord.slidegen.editor.baseui;

public class BasePresenter<T> {

	protected T model;

	protected void setModel(T model) {
		this.model = model;
	}

}
