package de.syslord.slidegen.uiedit.view;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import de.syslord.slidegen.uiedit.vaadinui.BasePresenter;

@UIScope
@SpringComponent
public class MainPresenter extends BasePresenter<Model> implements Serializable {

	private Logger logger = LoggerFactory.getLogger(MainPresenter.class);

	private static final long serialVersionUID = -751079627844818264L;

	private MainView view;

	public void setView(MainView view) {
		this.view = view;
	}

	public void onViewEntered() {
		Model model = new Model();

		setModel(model);
		view.setModel(model);

		view.init();
	}

	private BiFunction<Void, Throwable, Object> handleException() {
		return (aVoid, throwable) -> {
			if (throwable != null) {
				view.push(() -> Notification.show(throwable.getMessage(), "Fehler", Type.ERROR_MESSAGE));
				logger.error("", throwable);
			}
			return aVoid;
		};
	}

}
