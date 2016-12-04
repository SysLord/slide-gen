package de.syslord.slidegen.uiedit.vaadinui;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import de.syslord.slidegen.uiedit.view.MainView;

@Theme("uiedit")
@SpringUI
@Push
@PreserveOnRefresh
public class VaadinUi extends UI {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MainView mainView;

	// @Autowired
	// private SpringViewProvider viewProvider;

	@Override
	protected void init(VaadinRequest request) {
		setLocale(Locale.GERMANY);

		// Navigator navigator = new Navigator(this, this);
		// navigator.addProvider(viewProvider);

		getUI().setErrorHandler(errorHandler());
		// getUI().getNavigator().navigateTo(MainView.VIEW_NAME);

		setContent(mainView);
	}

	private ErrorHandler errorHandler() {
		return event -> Notification.show(event.getThrowable().toString(), "Fehler", Type.ERROR_MESSAGE);
	}

}