package de.syslord.slidegen.editor.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.generator.ImageGenerator;
import de.syslord.slidegen.editor.vaadinui.BasePresenter;

@UIScope
@SpringComponent
public class MainPresenter extends BasePresenter<Model> implements Serializable {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);

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

	@Async
	public void renderPreviewAndPush(LayoutableBox exportLayout) {
		BufferedImage generate = ImageGenerator.generate(exportLayout);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ImageIO.write(generate, "png", out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		view.showPreview(out);
	}

}
