package de.syslord.slidegen.editor.view;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ColorPickerArea;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.slidegen.editor.base.BaseEditorView;
import de.syslord.slidegen.editor.base.BoxPropertyChangedEvent;
import de.syslord.slidegen.editor.base.ContainerBox;
import de.syslord.slidegen.editor.base.PropertyFieldFactory;
import de.syslord.slidegen.editor.base.StylableLabel;
import de.syslord.slidegen.editor.base.UiBox;
import de.syslord.slidegen.editor.base.UiTextBox;
import de.syslord.slidegen.editor.glue.EditorExporter;
import de.syslord.slidegen.editor.glue.EditorJavaCodeImporter;
import de.syslord.slidegen.editor.model.UiBoxStyleData;
import de.syslord.slidegen.editor.model.UiTextBoxStyleData;
import de.syslord.slidegen.editor.persisting.JavaCodeExporter;
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

	private Consumer<BoxPropertyChangedEvent> boxPropertyChangedListener = event -> editorProperties.reReadValues();

	private TextArea javaCodeImportExportField;

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

		initializeEditor(model.getEditorWidth(), model.getEditorHeight());

		grid.addComponent(editorWrapper, 0, 0);
		grid.addComponent(createJavaImporterExporter(), 0, 1);
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

	private Component createJavaImporterExporter() {
		VerticalLayout layout = new VerticalLayout();

		javaCodeImportExportField = new TextArea();
		layout.addComponent(javaCodeImportExportField);

		Button javaCodeExportButton = new Button("Export", event -> exportToJavaCode());
		Button javaCodeImportButton = new Button("Import/Run", event -> importFromJavaCode(javaCodeImportExportField.getValue()));
		layout.addComponent(javaCodeExportButton);
		layout.addComponent(javaCodeImportButton);

		return layout;
	}

	private void exportToJavaCode() {
		LayoutableBox root = exportLayout();

		String export = new JavaCodeExporter().export(root, "ExportedLayout", "exportedlayout");

		javaCodeImportExportField.setValue(export);
	}

	private void importFromJavaCode(String javaCode) {
		clearSelection();
		clearEditor();

		new EditorJavaCodeImporter().importFromCode(editor, javaCode);
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
		LayoutableBox exportLayout = exportLayout();
		presenter.renderPreviewAndPush(exportLayout);
	}

	private LayoutableBox exportLayout() {
		return editorExporter.exportLayout(editor, model.getEditorWidth(), model.getEditorHeight());
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
	protected void onEditableComponentUnselect() {
		eventBus.unregister(this, BoxPropertyChangedEvent.class, boxPropertyChangedListener);
	}

	@Override
	protected void onEditableComponentSelect(UiBox clickedBox) {
		addPositioningProps(clickedBox);
		addResizableProps(clickedBox);
		addMinMaxHeightProps(clickedBox);
		addDynamicPositioningProps(clickedBox);

		if (clickedBox.isA(UiTextBox.class)) {
			UiTextBox textBox = (UiTextBox) clickedBox;
			StylableLabel label = textBox.getComponentAs();

			showTextValueProps(textBox);
			addTextColorProps(textBox, label);
			addFontProps(textBox, label);
			addMarginPaddingProps(textBox, label);
		}

		eventBus.register(this, BoxPropertyChangedEvent.class, boxPropertyChangedListener);
	}

	private void showTextValueProps(UiBox clickedBox) {
		TextField tf = new TextField("Text", clickedBox.getValue());
		tf.addValueChangeListener(event -> clickedBox.setValue(tf.getValue()));
		editorProperties.addProperty(tf);
	}

	private void addFontProps(UiTextBox box, StylableLabel label) {
		UiTextBoxStyleData uiBoxData = box.getUiTextBoxStyleData();

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

	private void addMarginPaddingProps(UiTextBox box, StylableLabel label) {
		UiTextBoxStyleData textboxStyleData = box.getUiTextBoxStyleData();

		TextField margin = fieldFactory.createIntegerField(
				"Margin", textboxStyleData.getMargin(),
				0, Math.min(box.getWidth() / 2, box.getHeight() / 2),
				s -> {
					textboxStyleData.setMargin(s);
					label.updateStyle(textboxStyleData);
				});

		TextField padding = fieldFactory.createIntegerField(
				"Padding", textboxStyleData.getPadding(),
				0, Math.min(box.getWidth() / 2, box.getHeight() / 2),
				s -> {
					textboxStyleData.setPadding(s);
					label.updateStyle(textboxStyleData);
				});
		editorProperties.addProperties(margin, padding);
	}

	private void addTextColorProps(UiTextBox box, StylableLabel label) {
		UiTextBoxStyleData textboxStyleData = box.getUiTextBoxStyleData();

		ColorPickerArea colorPickerArea = fieldFactory.createColorPickerArea(
				"Float Up", textboxStyleData.getForegroundColor(),
				s -> {
					textboxStyleData.setForegroundColor(s);
					label.updateStyle(textboxStyleData);
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

		editorProperties.setValueProvider(widthField, () -> box.getWidth());
		editorProperties.setValueProvider(heightField, () -> box.getHeight());
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

		editorProperties.setValueProvider(xField, () -> box.getX());
		editorProperties.setValueProvider(yField, () -> box.getY());
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
