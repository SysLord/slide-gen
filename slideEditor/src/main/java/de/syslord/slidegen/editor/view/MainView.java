package de.syslord.slidegen.editor.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.slidegen.editor.glue.EditorExporter;
import de.syslord.slidegen.editor.model.UiBox;
import de.syslord.slidegen.editor.vaadinui.BaseEditorView;
import de.syslord.slidegen.editor.vaadinui.FieldFactory;

@UIScope
@SpringView(name = MainView.VIEW_NAME)
@Push
public class MainView extends BaseEditorView<Model> {

	private static final long serialVersionUID = -55365966110272137L;

	public static final String VIEW_NAME = "slideeditor";

	public final FieldFactory fieldFactory = new FieldFactory();

	@Autowired
	private MainPresenter presenter;

	@Autowired
	private EditorExporter editorExporter;

	private VerticalLayout previewImageHolder;

	@PostConstruct
	private void createUI() {
		presenter.setView(this);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// Only with navigator, otherwise use attach()
		presenter.onViewEntered();
	}

	@Override
	public void attach() {
		super.attach();
		// without navigator
		presenter.onViewEntered();
	}

	public void init() {
		GridLayout grid = new GridLayout(4, 10);
		grid.setMargin(true);
		grid.setSpacing(true);
		grid.setWidth("100%");
		// grid.setSizeUndefined();

		grid.addComponent(
				createEditor(model.getEditorWidth(), model.getEditorHeight()),
				0, 0);
		// grid.addComponent(
		// createProperties(),
		// 0, 1);
		// grid.addComponent(
		// createPreview(),
		// 1, 0);

		createWindow("Properties", createProperties(), 0, model.getEditorHeight(), 1024, 300);
		createWindow("Preview", createPreview(), model.getEditorWidth(), 0, model.getEditorWidth(), model.getEditorHeight());

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

		createTextBox("A", editor, 0, 0, 40, 40);
		createTextBox("B", editor, 150, 100, 200, 400);

		AbsoluteLayout nest = createBox(editor, 700, 500, 200, 200);
		createTextBox("X", nest, 10, 10, 20, 20);
	}

	@Override
	protected void onEditableComponentClicked(Component clickedComponent) {
		clearProps();

		AbsoluteLayout parent = (AbsoluteLayout) clickedComponent.getParent();
		addPositioningProps(parent, clickedComponent);
		addResizableProps(clickedComponent);
		addMinMaxHeightProps(clickedComponent);

		if (Label.class.isAssignableFrom(clickedComponent.getClass())) {
			showLabelProps((Label) clickedComponent);
		} else if (Button.class.isAssignableFrom(clickedComponent.getClass())) {
			showButtonProps((Button) clickedComponent);
		}
	}

	private void showLabelProps(Label clickedComponent) {
		String value = clickedComponent.getValue();

		TextField tf = new TextField("Text", value);
		tf.addValueChangeListener(event -> clickedComponent.setValue(tf.getValue()));
		addProperty(tf);
	}

	private void showButtonProps(Button clickedComponent) {
		String value = clickedComponent.getCaption();

		TextField tf = new TextField("Text", value);
		tf.addValueChangeListener(event -> clickedComponent.setCaption(tf.getValue()));
		addProperty(tf);
	}

	private void addMinMaxHeightProps(Component component) {
		UiBox uiBox = getUiBox(component);

		TextField minHeight = fieldFactory.createNullableIntegerPropertyField(
				"Min Height", uiBox.getMinHeight(),
				1, model.getEditorWidth(),
				s -> uiBox.setMinHeight(s));

		TextField maxHeight = fieldFactory.createNullableIntegerPropertyField(
				"Max Height", uiBox.getMaxHeight(),
				1, model.getEditorHeight(),
				s -> uiBox.setMaxHeight(s));

		addProperties(minHeight, maxHeight);
	}

	private void addResizableProps(Component sizeable) {
		int width = (int) sizeable.getWidth();
		int height = (int) sizeable.getHeight();

		TextField widthField = fieldFactory.createIntegerAsStringPropertyField(
				"Width", String.valueOf(width),
				1, model.getEditorWidth(),
				s -> sizeable.setWidth(s + "px"));

		TextField heightField = fieldFactory.createIntegerAsStringPropertyField(
				"Height", String.valueOf(height),
				1, model.getEditorHeight(),
				s -> sizeable.setHeight(s + "px"));

		addProperties(0, widthField, heightField);
	}

	private void addPositioningProps(AbsoluteLayout layout, Component component) {
		ComponentPosition position = layout.getPosition(component);
		int x = position.getLeftValue().intValue();
		int y = position.getTopValue().intValue();

		TextField xField = fieldFactory.createIntegerPropertyField(
				"X", x,
				0, model.getEditorWidth(),
				v -> setNewAbsoluteX(layout, component, v));

		TextField yField = fieldFactory.createIntegerPropertyField(
				"Y", y,
				0, model.getEditorHeight(),
				v -> setNewAbsoluteY(layout, component, v));
		addProperties(0, xField, yField);
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
