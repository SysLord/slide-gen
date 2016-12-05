package de.syslord.slidegen.editor.view;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;

public class ImageProvider {

	public static Image getGreen() {
		return new Image(null, new ThemeResource("images/green.png"));
	}

}
