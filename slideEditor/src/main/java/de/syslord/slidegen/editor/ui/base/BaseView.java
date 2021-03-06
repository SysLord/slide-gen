package de.syslord.slidegen.editor.ui.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public abstract class BaseView<T> extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected EventBus eventBus;

	protected T model;

	public void setModel(T model) {
		this.model = model;
	}

	protected void initView() {
		//
	}

	@Override
	public void attach() {
		super.attach();
		eventBus.attach(this);
	}

	@Override
	public void detach() {
		eventBus.detach(this);
		super.detach();
	}

	public void push(Runnable pushRunnable) {
		UI.getCurrent().access(pushRunnable);
		// only needed if push manual is active
		// UI.getCurrent().push();
	}

	protected void createWindow(String caption, Component content, int x, int y, int width, int height) {
		content.setSizeUndefined();

		Panel scrollPanel = new Panel(content);
		scrollPanel.setSizeFull();

		Window window = new Window(caption, scrollPanel);
		window.setResizable(true);
		window.setClosable(false);
		window.setModal(false);

		window.setPosition(x, y);
		window.setWidth(width, Unit.PIXELS);
		window.setHeight(height, Unit.PIXELS);

		window.addStyleName("properties-window");

		UI.getCurrent().addWindow(window);
	}

}
