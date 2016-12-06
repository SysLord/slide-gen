package de.syslord.slidegen.editor.base;

import java.io.ByteArrayInputStream;

import org.springframework.util.Assert;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;

import de.syslord.slidegen.editor.util.ResourceUtil;

public class Editor extends ContainerBox {

	public Editor(AbsoluteLayout absoluteLayout) {
		super(absoluteLayout);
		editor = this;
	}

	@Override
	public boolean isEditor() {
		return true;
	}

	public void setBackdropImage(String backgroundImageResource) {
		getUiStyleData().setImage(ResourceUtil.getResourceAsStream(backgroundImageResource));

		StreamResource source = new StreamResource(() -> {
			// Stream needs to be created here as a page refresh will try to reread the resource which will
			// fail even with reset()
			ByteArrayInputStream in = ResourceUtil.getResourceAsStream(backgroundImageResource);
			Assert.notNull(in);

			return in;
		}, "backdrop.png");
		Image image = new Image("", source);
		image.setSizeFull();

		getLayout().addComponent(image);
	}

}
