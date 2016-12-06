package de.syslord.slidegen.editor.glue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.spring.annotation.SpringComponent;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.TextBox;
import de.syslord.slidegen.editor.base.ContainerBox;
import de.syslord.slidegen.editor.base.Editor;
import de.syslord.slidegen.editor.base.UiBox;
import de.syslord.slidegen.editor.model.UiBoxData;

@SpringComponent
public class EditorExporter {

	private static final Logger logger = LoggerFactory.getLogger(EditorExporter.class);

	public LayoutableBox exportLayout(Editor editor, int editorWidth, int editorHeight) {
		LayoutableBox rootBox = LayoutableBox.createFixedHeightBox("root", 0, 0, editorWidth, editorHeight);
		exportLayout(rootBox, editor);
		return rootBox;
	}

	private void exportLayout(LayoutableBox parentBox, ContainerBox container) {

		List<UiBox> children = container.getChildren();

		children.forEach(child -> {
			// Property: has value/getValue()

			if (child.isA(ContainerBox.class)) {
				ContainerBox containerBox = (ContainerBox) child;
				LayoutableBox box = exportContainer(parentBox, containerBox);

				exportLayout(box, containerBox);
			} else {
				exportTextBox(parentBox, child);
			}

		});

	}

	private LayoutableBox exportContainer(LayoutableBox parentBox, ContainerBox containerToExport) {
		LayoutableBox box = new LayoutableBox("",
				containerToExport.getX(), containerToExport.getY(),
				containerToExport.getWidth(), containerToExport.getHeight());

		addUiBoxProperties(containerToExport.getUiBoxData(), box);
		parentBox.addChild(box);
		return box;
	}

	private void exportTextBox(LayoutableBox parentBox, UiBox childToExport) {
		TextBox box = new TextBox("",
				childToExport.getValue(),
				childToExport.getX(), childToExport.getY(),
				childToExport.getWidth(), childToExport.getHeight());

		addUiBoxProperties(childToExport.getUiBoxData(), box);
		parentBox.addChild(box);
	}

	private void addUiBoxProperties(UiBoxData uiBox, LayoutableBox box) {
		box.setProp(HeightProperty.MIN, uiBox.getMinHeight());
		box.setProp(HeightProperty.MAX, uiBox.getMaxHeight());

		box.setPropIf(PositionProperty.FLOAT_UP, uiBox.getFloatUp());
		box.setPropIf(PositionProperty.FLOAT_DOWN, uiBox.getFloatDown());

		box.setBackgroundImage(uiBox.getImage());
		box.setForegroundColor(uiBox.getForegroundColor());
	}

}
