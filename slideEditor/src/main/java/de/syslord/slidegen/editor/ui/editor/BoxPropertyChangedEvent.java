package de.syslord.slidegen.editor.ui.editor;

import de.syslord.slidegen.editor.ui.base.EventBusEvent;

public class BoxPropertyChangedEvent extends EventBusEvent {

	// TODO
	private int x;

	public BoxPropertyChangedEvent() {
	}

	public BoxPropertyChangedEvent(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

}
