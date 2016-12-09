package de.syslord.slidegen.editor.view;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.slidegen.editor.base.*;
import de.syslord.slidegen.editor.glue.EditorExporter;
import de.syslord.slidegen.editor.model.UiBoxStyleData;
import de.syslord.slidegen.editor.util.Lorem;

@UIScope
@SpringView(name = MainView.VIEW_NAME)
@Push
public class MainView extends BaseEditorView<Model> {

	private static final long serialVersionUID = -55365966110272137L;

	public static final String VIEW_NAME = "slideeditor";

	public final PropertyFieldFactory fieldFactory = new PropertyFieldFactory();

	@Autowired
	private MainPresenter presenter;

	@Autowired
	private EditorExporter editorExporter;

	private VerticalLayout previewImageHolder;

	@PostConstruct
	protected void createUI() {
		presenter.setView(this);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// Only with navigator, otherwise use attach()
		// presenter.onViewEntered();
	}

	@Override
	public void attach() {
		super.attach();
		// without navigator
		presenter.onViewEntered();
	}

	@Override
	public void initView() {
		super.initView();

		GridLayout grid = new GridLayout(4, 10);
		grid.setMargin(true);
		grid.setSpacing(true);
		grid.setSizeUndefined();

		grid.addComponent(
				createEditor(model.getEditorWidth(), model.getEditorHeight()),
				0, 0);
		// TODO introduce options to show Properties or preview as window or in page
		// grid.addComponent(
		// createProperties(),
		// 0, 1);
		// grid.addComponent(
		// createPreview(),
		// 1, 0);
		createWindow(
				"Properties", editorProperties.createProperties(),
				0, model.getEditorHeight() - 300,
				1024, 300);
		createWindow(
				"Preview", createPreview(),
				model.getEditorWidth(), 0,
				model.getEditorWidth() + 80, model.getEditorHeight() - 200);

		initEditableLayout();

		Panel scrollPanel = new Panel(grid);
		scrollPanel.setSizeFull();

		setSizeFull();
		setCompositionRoot(scrollPanel);
	}

	private Component createPreview() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		Button button = new Button("Pushrender");
		button.addClickListener(event -> pushrender());
		layout.addComponent(button);

		previewImageHolder = new VerticalLayout();
		layout.addComponent(previewImageHolder);

		return layout;
	}

	private void pushrender() {
		LayoutableBox exportLayout = editorExporter.exportLayout(editor, model.getEditorWidth(), model.getEditorHeight());
		presenter.renderPreviewAndPush(exportLayout);
	}

	// TODO Load from boxmodel
	private void initEditableLayout() {
		editor.createTextBox(Lorem.ASDASD, 0, 0, 100, 25);

		UiBox fl = editor.createTextBox("Floater", 0, 40, 200, 200);
		fl.getUiStyleData().setFloatUp(true);
		fl.getUiStyleData().setFloatDown(true);

		editor.createTextBox("B", 150, 100, 200, 200);

		ContainerBox nest = editor.createBox(600, 400, 200, 200);
		nest.createTextBox("X", 10, 10, 20, 20);
	}

	@Override
	protected void onEditableComponentClicked(UiBox clickedBox) {
		addPositioningProps(clickedBox);
		addResizableProps(clickedBox);
		addMinMaxHeightProps(clickedBox);
		addDynamicPositioningProps(clickedBox);

		if (clickedBox.componentIsA(Property.class)) {
			showLabelProps(clickedBox);
		}
		if (clickedBox.componentIsA(StylableLabel.class)) {
			StylableLabel label = clickedBox.getComponentAs();
			addColorProps(clickedBox, label);
			addFontProps(clickedBox, label);
		}
	}

	private void showLabelProps(UiBox clickedBox) {
		String value = clickedBox.getValue();

		TextField tf = new TextField("Text", value);
		tf.addValueChangeListener(event -> clickedBox.setValue(tf.getValue()));
		editorProperties.addProperty(tf);
	}

	private void addFontProps(UiBox box, StylableLabel label) {
		// TODO
		UiBoxStyleData uiBoxData = box.getUiStyleData();
		int oldSize = uiBoxData.getFont().getSize();
		boolean oldBold = (uiBoxData.getFont().getStyle() & Font.BOLD) > 0;

		CheckBox boldCheck = fieldFactory.createCheckbox(
				"Fett", oldBold,
				s -> {
					// need to use current font, which maybe updated after prop field creation!
					Font oldFont = uiBoxData.getFont();
					Font font = new Font(oldFont.getName(), s ? Font.BOLD : Font.PLAIN, oldFont.getSize());
					uiBoxData.setFont(font);
					label.updateStyle(uiBoxData);
				});

		TextField size = fieldFactory.createIntegerField(
				"Fontsize", oldSize,
				1, 400,
				v -> {
					// need to use current font, which maybe updated after prop field creation!
					Font oldFont = uiBoxData.getFont();
					Font font = new Font(oldFont.getName(), oldFont.getStyle(), v);
					uiBoxData.setFont(font);
					label.updateStyle(uiBoxData);
				});

		editorProperties.addProperties(size, boldCheck);
	}

	private void addColorProps(UiBox box, StylableLabel label) {

		UiBoxStyleData uiBoxStyleData = box.getUiStyleData();
		ColorPickerArea colorPickerArea = fieldFactory.createColorPickerArea(
				"Float Up", uiBoxStyleData.getForegroundColor(),
				s -> {
					uiBoxStyleData.setForegroundColor(s);
					label.updateStyle(uiBoxStyleData);
				});

		editorProperties.addProperties(colorPickerArea);
	}

	private void addDynamicPositioningProps(UiBox box) {
		UiBoxStyleData uiBoxData = box.getUiStyleData();

		CheckBox floatUpCheckbox = fieldFactory.createCheckbox(
				"Float Up", uiBoxData.getFloatUp(),
				s -> uiBoxData.setFloatUp(s));
		CheckBox floatDownCheckbox = fieldFactory.createCheckbox(
				"Float Down", uiBoxData.getFloatDown(),
				s -> uiBoxData.setFloatDown(s));

		editorProperties.addProperties(floatUpCheckbox, floatDownCheckbox);
	}

	private void addMinMaxHeightProps(UiBox box) {
		UiBoxStyleData uiBoxData = box.getUiStyleData();

		TextField minHeight = fieldFactory.createNullableIntegerField(
				"Min Height", uiBoxData.getMinHeight(),
				1, model.getEditorWidth(),
				s -> uiBoxData.setMinHeight(s));

		TextField maxHeight = fieldFactory.createNullableIntegerField(
				"Max Height", uiBoxData.getMaxHeight(),
				1, model.getEditorHeight(),
				s -> uiBoxData.setMaxHeight(s));

		editorProperties.addProperties(minHeight, maxHeight);
	}

	private void addResizableProps(UiBox box) {
		TextField widthField = fieldFactory.createIntegerField(
				"Width", box.getWidth(),
				1, model.getEditorWidth(),
				v -> box.setWidth(v));

		TextField heightField = fieldFactory.createIntegerField(
				"Height", box.getHeight(),
				1, model.getEditorHeight(),
				v -> box.setHeight(v));

		editorProperties.addProperties(0, widthField, heightField);
	}

	private void addPositioningProps(UiBox box) {
		TextField xField = fieldFactory.createIntegerField(
				"X", box.getX(),
				0, model.getEditorWidth(),
				v -> box.setX(v, false));

		TextField yField = fieldFactory.createIntegerField(
				"Y", box.getY(),
				0, model.getEditorHeight(),
				v -> box.setY(v, false));
		editorProperties.addProperties(0, xField, yField);
	}

	public void showPreview(ByteArrayOutputStream out) {
		push(() -> pushPreviewImage(out));
	}

	private void pushPreviewImage(ByteArrayOutputStream out) {
		StreamSource source = () -> new ByteArrayInputStream(out.toByteArray());
		Resource r = new StreamResource(source, "whatever.png");

		// push updating an image with setResource() works only first update.
		previewImageHolder.removeAllComponents();
		previewImageHolder.addComponent(new Image("Preview", r));
	}

}
