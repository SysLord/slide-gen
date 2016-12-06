package de.syslord.slidegen.editor.baseui;

import java.util.UUID;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ContextWindowHandler {

	private Window currentWindow = null;

	private ClickListener currentListener = null;

	private void updateContextWindow(Window w, ClickListener l) {
		if (currentWindow != null) {
			currentWindow.close();
			UI.getCurrent().removeClickListener(currentListener);
		}
		currentWindow = w;
		currentListener = l;
	}

	// This is rather complicated as we are probably in the middle of a right clickevent. A global
	// clicklistener might be executed after we left this method and already close the new window. So we keep
	// Track of our state and allow only to close the currently open window whilst keeping the global
	// clicklistener count to exectly 1 for this operation.
	public Window showContextWindow(Component content, int x, int y, String width, String heigth) {
		Window w = new Window();
		String s = UUID.randomUUID().toString();
		w.setId(s);
		w.setWidth(width);
		w.setHeight(heigth);

		w.setDraggable(false);
		w.setModal(false);
		w.setResizable(false);

		w.setContent(content);

		ClickListener listener = addGlobalCloseContextWindowsListener(w);
		updateContextWindow(w, listener);

		UI.getCurrent().addWindow(w);

		w.setPosition(x, y);
		return w;
	}

	private ClickListener addGlobalCloseContextWindowsListener(Window w) {
		ClickListener listener = event -> {
			if (event.getButton().equals(MouseButton.LEFT)) {
				updateContextWindow(null, null);
			}
		};
		UI.getCurrent().addClickListener(listener);
		return listener;
	}

	public Runnable getCloser() {
		return () -> updateContextWindow(null, null);
	}

}
