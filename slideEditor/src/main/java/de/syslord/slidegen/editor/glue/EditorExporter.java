package de.syslord.slidegen.editor.glue;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.TextBox;
import de.syslord.slidegen.editor.model.UiBox;
import de.syslord.slidegen.editor.util.UiUtil;

@SpringComponent
public class EditorExporter {

	private static final Logger logger = LoggerFactory.getLogger(EditorExporter.class);

	public LayoutableBox exportLayout(AbsoluteLayout editor, int editorWidth, int editorHeight) {
		LayoutableBox rootBox = LayoutableBox.createFixedHeightBox("root", 0, 0, editorWidth, editorWidth);

		exportLayout(rootBox, editor);
		return rootBox;
	}

	private void exportLayout(LayoutableBox parentBox, AbsoluteLayout parentLayout) {

		Iterator<Component> iter = parentLayout.iterator();
		while (iter.hasNext()) {
			Component component = iter.next();
			ComponentPosition position = parentLayout.getPosition(component);
			UiBox uiBox = UiUtil.getUiBox(component);

			// Property: has value/getValue()
			if (Property.class.isAssignableFrom(component.getClass())) {
				LayoutableBox box = createTextBox(component, position);
				addUiBoxProperties(uiBox, box);
				parentBox.addChild(box);

			} else if (AbsoluteLayout.class.isAssignableFrom(component.getClass())) {
				AbsoluteLayout layout = (AbsoluteLayout) component;

				LayoutableBox box = addNestedBox(layout, position);
				addUiBoxProperties(uiBox, box);
				parentBox.addChild(box);

				exportLayout(box, layout);
			} else {
				logger.warn("UNKNOWN LAYOUT ELEMENT!!!");
			}
		}
	}

	private void addUiBoxProperties(UiBox uiBox, LayoutableBox box) {
		box.setProp(HeightProperty.MIN, uiBox.getMinHeight());
		box.setProp(HeightProperty.MAX, uiBox.getMaxHeight());
	}

	private LayoutableBox addNestedBox(AbsoluteLayout layout, ComponentPosition position) {
		LayoutableBox box = new LayoutableBox("",
				position.getLeftValue().intValue(), position.getTopValue().intValue(),
				(int) layout.getWidth(), (int) layout.getHeight());
		return box;
	}

	private LayoutableBox createTextBox(Component component, ComponentPosition position) {
		@SuppressWarnings("unchecked")
		Property<String> property = (Property<String>) component;
		String value = property.getValue();

		LayoutableBox box = new TextBox("",
				value,
				position.getLeftValue().intValue(), position.getTopValue().intValue(),
				(int) component.getWidth(), (int) component.getHeight());
		return box;
	}

}
