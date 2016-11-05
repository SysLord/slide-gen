package de.syslord.slidegen.uiedit.view;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.syslord.slidegen.uiedit.vaadinui.BaseView;

@UIScope
@SpringView(name = MainView.VIEW_NAME)
public class MainView extends BaseView<Model> {

	private static final long serialVersionUID = -55365966110272137L;

	public static final String VIEW_NAME = "uiedit";

	@Autowired
	private MainPresenter presenter;

	private VerticalLayout editor;

	private VerticalLayout props;

	@PostConstruct
	private void createUI() {
		presenter.setView(this);
	}

	@Override
	public void attach() {
		super.attach();
	}

	@Override
	public void detach() {
		super.detach();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		presenter.onViewEntered();
	}

	public void init() {
		GridLayout grid = new GridLayout(4, 10);
		grid.setMargin(true);
		grid.setSpacing(true);
		grid.setWidth("100%");

		// Label debuglabel = new Label();

		// grid.addComponent(debuglabel);
		grid.addComponent(createEditor());
		grid.addComponent(createProperties());

		initEditableLayout();

		Panel scrollPanel = new Panel(grid);
		scrollPanel.setSizeFull();

		setSizeFull();
		setCompositionRoot(scrollPanel);
	}

	// TODO Das müsste aus einem BoxModel geladen werden.
	private void initEditableLayout() {
		Label a = new Label("A");
		Label b = new Label("B");
		outline(a);
		outline(b);
		HorizontalLayout aa = new HorizontalLayout(a, b);
		aa.setMargin(true);
		aa.setSpacing(true);
		aa.setSizeFull();
		outline(aa);
		editor.addComponent(aa);

		Label c = new Label("C");
		outline(c);
		Label d = new Label("D");
		outline(d);
		Button xx = new Button("Button");
		xx.setEnabled(false);
		outline(xx);

		HorizontalLayout vv = new HorizontalLayout(c, d, xx);
		vv.setMargin(true);
		vv.setSpacing(true);
		vv.setSizeFull();
		outline(vv);
		editor.addComponent(vv);
	}

	private void outline(Component x) {
		x.addStyleName("outlinedLayout");
	}

	private Component createProperties() {
		props = new VerticalLayout();
		props.setMargin(true);
		props.setSpacing(true);
		props.setSizeFull();
		return props;
	}

	private Component createEditor() {
		editor = new VerticalLayout();
		editor.setMargin(true);
		editor.setSpacing(true);
		// editor.setSizeFull();
		editor.setWidth("1024px");
		editor.setHeight("768px");

		editor.addLayoutClickListener(event -> onEditorClicked(event));
		return editor;
	}

	private void removeComponentSelection(Layout layout) {
		layout.iterator().forEachRemaining(x -> {
			x.removeStyleName("selectedLayout");
			maybeGetLayout(x).ifPresent(l -> removeComponentSelection(l));

		});
	}

	private Optional<Layout> maybeGetLayout(Component x) {
		return Layout.class.isAssignableFrom(x.getClass()) ? Optional.of((Layout) x) : Optional.empty();
	}

	private void onEditorClicked(LayoutClickEvent event) {
		removeComponentSelection(editor);

		System.out.println(event.getChildComponent());
		Component clickedComponent = event.getClickedComponent();
		System.out.println(clickedComponent);

		if (clickedComponent != null) {
			clickedComponent.addStyleName("selectedLayout");
			showProperties(clickedComponent);
		}

		System.out.println(event.getComponent());

	}

	private void showProperties(Component clickedComponent) {
		props.removeAllComponents();

		if (Label.class.isAssignableFrom(clickedComponent.getClass())) {
			showLabelProps((Label) clickedComponent);
		} else if (Button.class.isAssignableFrom(clickedComponent.getClass())) {
			showButtonProps((Button) clickedComponent);
		}

		// TODO Das müsste zukünftig so gehen:
		// ((BoxModel)clickedComponent.getData()).isSizeable()
		if (Sizeable.class.isAssignableFrom(clickedComponent.getClass())) {
			addResizableProps(clickedComponent);
		}
		addMinMaxSizeProps(clickedComponent);
	}

	private void showLabelProps(Label clickedComponent) {
		String value = clickedComponent.getValue();

		TextField tf = new TextField("Text", value);
		tf.addValueChangeListener(event -> clickedComponent.setValue(tf.getValue()));
		props.addComponent(tf);
	}

	private void showButtonProps(Button clickedComponent) {
		String value = clickedComponent.getCaption();

		TextField tf = new TextField("Text", value);
		tf.addValueChangeListener(event -> clickedComponent.setCaption(tf.getValue()));
		props.addComponent(tf);
	}

	private void addResizableProps(Sizeable sizeable) {
		String width = String.format("%s%s", String.valueOf(sizeable.getWidth()), sizeable.getWidthUnits().getSymbol());
		TextField wi = new TextField("width", width);
		wi.addValueChangeListener(event -> sizeable.setWidth(wi.getValue()));
		props.addComponent(wi);

		String height = String.format("%s%s", String.valueOf(sizeable.getHeight()),
				sizeable.getHeightUnits().getSymbol());
		TextField hei = new TextField("height", height);
		hei.addValueChangeListener(event -> sizeable.setHeight(hei.getValue()));
		props.addComponent(hei);
	}

	private void addMinMaxSizeProps(Component clickedComponent) {
		// TODO Auto-generated method stub
	}

}
