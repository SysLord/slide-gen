package de.syslord.slidegen.uiedit.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
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
import de.syslord.boxmodel.TextBox;
import de.syslord.slidegen.uiedit.vaadinui.BaseEditorView;

@UIScope
@SpringView(name = MainView.VIEW_NAME)
@Push
public class MainView extends BaseEditorView<Model> {

	private static final Logger logger = LoggerFactory.getLogger(MainPresenter.class);

	private static final long serialVersionUID = -55365966110272137L;

	public static final String VIEW_NAME = "uiedit";

	@Autowired
	private MainPresenter presenter;

	private VerticalLayout previewImageHolder;

	@PostConstruct
	private void createUI() {
		presenter.setView(this);
	}

	@Override
	public void attach() {
		// without navigator
		presenter.onViewEntered();
		super.attach();
	}

	public void init() {
		GridLayout grid = new GridLayout(4, 10);
		grid.setMargin(true);
		grid.setSpacing(true);
		grid.setWidth("100%");

		grid.addComponent(createEditor(model.getEditorWidth(), model.getEditorHeight()), 0, 0);
		grid.addComponent(createProperties(), 1, 0);
		grid.addComponent(createPreview(), 2, 0);

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
		LayoutableBox exportLayout = exportLayout();
		presenter.renderPreviewAndPush(exportLayout);
	}

	// TODO Load from boxmodel
	private void initEditableLayout() {
		Label a = new Label("A");
		Label b = new Label("B");
		addToBox(editor, a, 150, 100, 200, 400);
		addToBox(editor, b, 0, 0, 40, 40);

		AbsoluteLayout nest = new AbsoluteLayout();
		addToBox(editor, nest, 700, 500, 200, 200);

		Label x = new Label("X");
		addToBox(nest, x, 10, 10, 20, 20);

		outline(a, b, nest, x);
	}

	private LayoutableBox exportLayout() {
		LayoutableBox rootBox = new LayoutableBox("root", 0, 0, model.getEditorWidth(), model.getEditorHeight());

		exportLayout(rootBox, editor);

		return rootBox;
	}

	private void exportLayout(LayoutableBox parentBox, AbsoluteLayout parentLayout) {

		Iterator<Component> iter = parentLayout.iterator();
		while (iter.hasNext()) {
			Component component = iter.next();
			ComponentPosition position = parentLayout.getPosition(component);

			if (Property.class.isAssignableFrom(component.getClass())) {
				LayoutableBox box = createTextBox(component, position);
				parentBox.addChild(box);

			} else if (AbsoluteLayout.class.isAssignableFrom(component.getClass())) {
				AbsoluteLayout layout = (AbsoluteLayout) component;

				LayoutableBox box = addNestesBox(layout, position);
				parentBox.addChild(box);

				exportLayout(box, layout);
			} else {
				logger.warn("UNKNOWN LAYOUT ELEMENT!!!");
			}
		}
	}

	private LayoutableBox addNestesBox(AbsoluteLayout layout, ComponentPosition position) {
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

	@Override
	protected void onEditableComponentClicked(Component clickedComponent) {
		clearProps();

		AbsoluteLayout parent = (AbsoluteLayout) clickedComponent.getParent();
		addPositioningProps(parent, clickedComponent);
		if (Sizeable.class.isAssignableFrom(clickedComponent.getClass())) {
			addResizableProps(clickedComponent);
		}
		// addMinMaxSizeProps(clickedComponent);

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

	private void addResizableProps(Sizeable sizeable) {
		int width = (int) sizeable.getWidth();
		int height = (int) sizeable.getHeight();

		TextField widthField = createStringPropertyField(
				"Width", String.valueOf(width),
				1, model.getEditorWidth(),
				s -> sizeable.setWidth(s + "px"));
		addProperty(widthField);

		TextField heightField = createStringPropertyField(
				"Height", String.valueOf(height),
				1, model.getEditorHeight(),
				s -> sizeable.setHeight(s + "px"));
		addProperty(heightField);
	}

	private void addPositioningProps(AbsoluteLayout layout, Component component) {
		ComponentPosition position = layout.getPosition(component);
		int x = position.getLeftValue().intValue();
		int y = position.getTopValue().intValue();

		TextField xField = createIntegerPropertyField(
				"X", x,
				0, model.getEditorWidth(),
				v -> setNewAbsoluteX(layout, component, v));
		addProperty(xField);

		TextField yField = createIntegerPropertyField(
				"Y", y,
				0, model.getEditorHeight(),
				v -> setNewAbsoluteY(layout, component, v));
		addProperty(yField);
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
