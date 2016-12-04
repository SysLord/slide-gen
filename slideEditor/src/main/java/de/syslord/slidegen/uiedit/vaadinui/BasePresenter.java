package de.syslord.slidegen.uiedit.vaadinui;

public class BasePresenter<T> {

	protected T model;

	protected void setModel(T model) {
		this.model = model;
	}

}
