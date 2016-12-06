package de.syslord.slidegen.editor.base;

import java.io.InputStream;

import org.springframework.util.Assert;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;

public class Editor extends ContainerBox {

	public Editor(AbsoluteLayout absoluteLayout) {
		super(absoluteLayout);
		editor = this;
	}

	@Override
	public boolean isEditor() {
		return true;
	}

	public void setBackdropImage(InputStream in) {
		Assert.notNull(in);

		StreamResource source = new StreamResource(() -> in, "backdrop.png");
		Image image = new Image("", source);
		image.setSizeFull();
		getLayout().addComponent(image);
	}

}
