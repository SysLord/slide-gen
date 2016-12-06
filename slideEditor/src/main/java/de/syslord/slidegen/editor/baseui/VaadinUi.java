package de.syslord.slidegen.editor.baseui;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.thirdparty.guava.common.base.Throwables;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import de.syslord.slidegen.editor.view.MainView;

@Theme("slideedit")
@SpringUI
@Push
@PreserveOnRefresh
public class VaadinUi extends UI {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VaadinUi.class);

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

		// ContextWindows.enableContextWindows();

		setContent(mainView);
	}

	private ErrorHandler errorHandler() {
		return event -> {
			Notification.show(event.getThrowable().toString()
					+ "\n"
					+ event.getThrowable().getMessage()
					+ "\n"
					+ Throwables.getStackTraceAsString(event.getThrowable()),
					"Fehler",
					Type.ERROR_MESSAGE);

			logger.error("", event.getThrowable());
		};
	}

}