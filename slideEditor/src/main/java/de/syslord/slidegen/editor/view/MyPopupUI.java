package de.syslord.slidegen.editor.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import de.syslord.slidegen.editor.ui.base.BaseView;
import de.syslord.slidegen.editor.ui.base.EventBus;
import de.syslord.slidegen.editor.ui.editor.BoxPropertyChangedEvent;

//@Theme("slideedit")
//@SpringUI(path = "/pop")
//@Push
//@SpringView(name = "pop")
public class MyPopupUI extends UI {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EventBus bus;

	protected void initView() {
		//
		System.out.println("INIT");
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(new Label("aaaaa"));

		BaseView<String> view = new BaseView<String>() {

			@Override
			public void enter(ViewChangeEvent event) {
			}

		};

		bus.register(view, BoxPropertyChangedEvent.class, x -> setContent(new Label("" + x.getX())));

	}

}