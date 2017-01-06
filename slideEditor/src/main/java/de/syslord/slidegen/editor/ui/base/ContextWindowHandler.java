package de.syslord.slidegen.editor.ui.base;

import java.util.UUID;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ContextWindowHandler {

	private Window currentWindow = null;

	private ClickListener currentListener = null;

	// The new state can be null if there should be no new window after closing the old one.
	private void closeCurrentContextWindowAndRegisterNewContextWindowState(
			Window newlyOpenedWindow,
			ClickListener newlyCreatedGlobalCLickListener) {

		if (currentWindow != null) {
			// also removes window from UI
			currentWindow.close();
			UI.getCurrent().removeClickListener(currentListener);
		}
		currentWindow = newlyOpenedWindow;
		currentListener = newlyCreatedGlobalCLickListener;
	}

	private void closeContextWindow() {
		closeCurrentContextWindowAndRegisterNewContextWindowState(null, null);
	}

	/*
	 * This is rather complicated as we are probably in the middle of a right clickevent to show our window. A
	 * global clicklistener might be executed after we left this method and already close the new window. So
	 * we keep Track of our state and allow any old global clicklistener only to close the currently open
	 * window, which is never the new window. We take care that we will be using exactly 1 global
	 * clickListener at any time.
	 */
	public Window showContextWindow(Component content, int x, int y, int width, int heigth) {
		Window w = new Window();
		String s = UUID.randomUUID().toString();
		w.setId(s);
		w.setWidth(width, Unit.PIXELS);
		w.setHeight(heigth, Unit.PIXELS);

		w.setDraggable(false);
		w.setModal(false);
		w.setResizable(false);

		w.setContent(content);

		ClickListener listener = addGlobalCloseContextWindowsListener();
		closeCurrentContextWindowAndRegisterNewContextWindowState(w, listener);

		UI.getCurrent().addWindow(w);

		// w.setPosition(x, y);
		w.setPositionX(x);
		w.setPositionY(y);
		return w;
	}

	/*
	 * Create a new global clicklistener to refresh only this contextWindowHandlers state when activated. Then
	 * this contexthandler can decide what to do with its current window if it is open.
	 */
	private ClickListener addGlobalCloseContextWindowsListener() {
		ClickListener listener = event -> {
			if (event.getButton().equals(MouseButton.LEFT)) {
				closeContextWindow();
			}
		};

		UI.getCurrent().addClickListener(listener);
		return listener;
	}

	public Runnable getCloser() {
		return () -> closeContextWindow();
	}

}
