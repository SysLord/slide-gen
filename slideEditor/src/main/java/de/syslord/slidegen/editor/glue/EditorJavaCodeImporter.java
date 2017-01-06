package de.syslord.slidegen.editor.glue;

import java.util.Map;

import com.google.gwt.thirdparty.guava.common.collect.Maps;

import de.syslord.boxmodel.HeightProperty;
import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.slidegen.editor.persisting.JavaBoxImporter;
import de.syslord.slidegen.editor.ui.editor.Editor;
import de.syslord.slidegen.editor.ui.elements.ContainerBox;
import de.syslord.slidegen.editor.ui.elements.UiBoxStyleData;

/*
 * Only LayoutableBox is supported, no other types yet. So this is incomplete.
 */
public class EditorJavaCodeImporter {

	public void importFromCode(Editor editor, String javaCode) {
		Map<LayoutableBox, ContainerBox> mapping = Maps.newHashMap();

		LayoutableBox root = new JavaBoxImporter().load(javaCode);

		// TODO does editor always need to be special?
		editor.setWidth(root.getWidth());
		editor.setHeight(root.getHeight());
		addStyles(editor, root);
		mapping.put(root, editor);

		root.streamFlatWithParents()
			.forEach(x -> createUiBox(x.getParent(), x.getChild(), mapping));
	}

	private void createUiBox(LayoutableBox parent, LayoutableBox child, Map<LayoutableBox, ContainerBox> mapping) {
		ContainerBox parentUiBox = mapping.get(parent);

		ContainerBox childUiBox = createUiBox(parentUiBox, child);
		mapping.put(child, childUiBox);
	}

	private ContainerBox createUiBox(ContainerBox parent, LayoutableBox child) {
		ContainerBox uiBox = parent.createBox(child.getX(), child.getY(), child.getWidth(), child.getHeight());

		addStyles(uiBox, child);

		return uiBox;
	}

	private void addStyles(ContainerBox uiBox, LayoutableBox child) {
		UiBoxStyleData uiStyleData = uiBox.getUiStyleData();

		uiStyleData.setFloatUp(child.hasProp(PositionProperty.FLOAT_UP));
		uiStyleData.setFloatDown(child.hasProp(PositionProperty.FLOAT_DOWN));

		uiStyleData.setMinHeight(child.getProp(HeightProperty.MIN));
		uiStyleData.setMaxHeight(child.getProp(HeightProperty.MAX));

		// TODO
		// image
		// foreground color
		// font
		// ...
	}

}
