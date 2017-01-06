package de.syslord.slidegen.editor.glue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.spring.annotation.SpringComponent;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.Margin;
import de.syslord.boxmodel.Padding;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.TextBox;
import de.syslord.slidegen.editor.ui.editor.Editor;
import de.syslord.slidegen.editor.ui.elements.ContainerBox;
import de.syslord.slidegen.editor.ui.elements.UiBox;
import de.syslord.slidegen.editor.ui.elements.UiBoxStyleData;
import de.syslord.slidegen.editor.ui.elements.UiTextBox;
import de.syslord.slidegen.editor.ui.elements.UiTextBoxStyleData;

@SpringComponent
public class EditorExporter {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EditorExporter.class);

	// TODO creation of root should not be special, reason is mainly that the uiStyleData is not properly
	// set up.
	public LayoutableBox exportLayout(Editor editor, int editorWidth, int editorHeight) {
		LayoutableBox rootBox = LayoutableBox.createFixedHeightBox("root", 0, 0, editorWidth, editorHeight);

		rootBox.setBackgroundImage(editor.getUiStyleData().getImage());

		exportLayout(rootBox, editor);
		return rootBox;
	}

	private void exportLayout(LayoutableBox parentBox, ContainerBox container) {

		List<UiBox> children = container.getChildrenOrdered();

		children.forEach(child -> {
			if (child.isA(ContainerBox.class)) {
				ContainerBox containerBox = (ContainerBox) child;
				LayoutableBox box = exportContainer(parentBox, containerBox);

				exportLayout(box, containerBox);
			} else if (child.isA(UiTextBox.class)) {
				exportTextBox(parentBox, (UiTextBox) child);
			}

		});

	}

	private LayoutableBox exportContainer(LayoutableBox parentBox, ContainerBox containerToExport) {
		LayoutableBox box = new LayoutableBox("",
				containerToExport.getX(), containerToExport.getY(),
				containerToExport.getWidth(), containerToExport.getHeight());

		addUiBoxProperties(containerToExport.getUiStyleData(), box);
		parentBox.addChild(box);
		return box;
	}

	private void exportTextBox(LayoutableBox parentBox, UiTextBox childToExport) {
		UiTextBoxStyleData styleData = childToExport.getUiTextBoxStyleData();

		TextBox box = new TextBox("",
				childToExport.getValue(),
				childToExport.getX(), childToExport.getY(),
				childToExport.getWidth(), childToExport.getHeight());

		box.setFont(styleData.getFont());
		addUiBoxProperties(styleData, box);

		// TODO editor can't do all paddings and margins yet
		box.setPadding(Padding.create(styleData.getPadding(), styleData.getPadding()));
		box.setMargin(Margin.create(styleData.getMargin(), styleData.getMargin()));

		parentBox.addChild(box);
	}

	private void addUiBoxProperties(UiBoxStyleData uiBox, LayoutableBox box) {
		box.setProp(HeightProperty.MIN, uiBox.getMinHeight());
		box.setProp(HeightProperty.MAX, uiBox.getMaxHeight());

		box.setPropIf(PositionProperty.FLOAT_UP, uiBox.getFloatUp());
		box.setPropIf(PositionProperty.FLOAT_DOWN, uiBox.getFloatDown());

		box.setBackgroundImage(uiBox.getImage());
		Color vaadinColor = uiBox.getForegroundColor();

		box.setForegroundColor(new java.awt.Color(vaadinColor.getRGB()));
	}

}
