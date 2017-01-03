package de.syslord.slidegen.editor.base;

import java.util.function.Consumer;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import de.syslord.slidegen.editor.baseui.BaseView;
import de.syslord.slidegen.editor.util.Key;
import de.syslord.slidegen.editor.util.ModifierKey;
import de.syslord.slidegen.editor.util.UiUtil;

public abstract class BaseEditorView<T extends EditorModel> extends BaseView<T> {

	private static final String EDITOR_STYLE = "editor";

	private static final String EDITOR_WRAPPER_STYLE = "editor-wrapper";

	private static final long serialVersionUID = 1L;

	protected Editor editor;

	protected VerticalLayout editorWrapper;

	private EditorContextMenu editorContextMenu;

	private UiBox currentlySelectedBox = null;

	protected EditorProperties editorProperties = new EditorProperties(component -> component.setWidth("100px"));

	protected abstract void onEditableComponentSelect(UiBox clickedBox);

	protected abstract void onEditableComponentUnselect();

	@Override
	protected void initView() {
		super.initView();

		editorContextMenu = new EditorContextMenu(menuInfo -> createEditorContextMenu(menuInfo));
	}

	private Component createEditorContextMenu(EditorContextMenu.ContextMenuOpenInfo menuInfo) {
		select(menuInfo.getClickedBox());

		Button deleteBoxButton = new Button("Löschen", btnClicked -> {
			onDeleteBoxClicked(menuInfo.getClickedBox());
			menuInfo.closeContextMenu();
		});
		Button newBoxButton = new Button("neuer Container", btnClick -> {
			onAddBoxClicked(menuInfo);
			menuInfo.closeContextMenu();
		});
		Button newTextBoxButton = new Button("neue TextBox", btnClick -> {
			onAddTextBoxClicked(menuInfo);
			menuInfo.closeContextMenu();
		});

		return new VerticalLayout(newTextBoxButton, newBoxButton, deleteBoxButton);
	}

	protected void initializeEditor(int width, int height) {

		AbsoluteLayout editorLayout = new AbsoluteLayout();
		editorLayout.setWidth(width + "px");
		editorLayout.setHeight(height + "px");
		editorLayout.addStyleName(EDITOR_STYLE);

		editor = new Editor(editorLayout);
		editor.getLayout().addLayoutClickListener(event -> onEditorClicked(event));
		editor.setBackdropImage("slide_backdrop.png");
		initArrowKeyListeners();

		editorWrapper = new VerticalLayout(editorLayout);
		editorWrapper.addStyleName(EDITOR_WRAPPER_STYLE);
		editorWrapper.setSpacing(false);
		editorWrapper.setMargin(false);
	}

	// TODO does not update the shown properties. But should do it.
	public void initArrowKeyListeners() {
		Consumer<Key> moveLeftRight = key -> {
			if (currentlySelectedBox != null) {
				currentlySelectedBox.setX(Key.ARROW_RIGHT.equals(key) ? 20 : -20, true);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};
		Consumer<Key> moveUpDown = key -> {
			if (currentlySelectedBox != null) {
				currentlySelectedBox.setY(Key.ARROW_DOWN.equals(key) ? 20 : -20, true);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};

		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_right", Key.ARROW_RIGHT, moveLeftRight));
		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_left", Key.ARROW_LEFT, moveLeftRight));
		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_down", Key.ARROW_DOWN, moveUpDown));
		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_up", Key.ARROW_UP, moveUpDown));

		Consumer<Key> changeWidth = key -> {
			if (currentlySelectedBox != null) {
				int oldWidth = currentlySelectedBox.getWidth();
				currentlySelectedBox.setWidth(Key.ARROW_RIGHT.equals(key) ? oldWidth + 20 : oldWidth - 20);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};
		Consumer<Key> changeHeight = key -> {
			if (currentlySelectedBox != null) {
				int oldHeight = currentlySelectedBox.getHeight();
				currentlySelectedBox.setHeight(Key.ARROW_DOWN.equals(key) ? oldHeight + 20 : oldHeight - 20);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};

		ModifierKey ctrl = ModifierKey.CTRL;
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_right", Key.ARROW_RIGHT, changeWidth, ctrl));
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_left", Key.ARROW_LEFT, changeWidth, ctrl));
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_down", Key.ARROW_DOWN, changeHeight, ctrl));
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_up", Key.ARROW_UP, changeHeight, ctrl));
	}

	private void onDeleteBoxClicked(UiBox clicked) {
		editor.clearSelection();
		clicked.remove();
	}

	private void onAddBoxClicked(EditorContextMenu.ContextMenuOpenInfo menuInfo) {
		UiBox clickedBox = menuInfo.getClickedBox();

		int x = menuInfo.getRelativeX();
		int y = menuInfo.getRelativeY();

		ContainerBox container = clickedBox.getContainer();
		if (clickedBox != container) {
			container.createBox(x - clickedBox.getX(), y - clickedBox.getY(), 40, 40);
			return;
		}
		container.createBox(x, y, 40, 40);
	}

	private void onAddTextBoxClicked(EditorContextMenu.ContextMenuOpenInfo menuInfo) {
		UiBox clickedBox = menuInfo.getClickedBox();

		int x = menuInfo.getRelativeX();
		int y = menuInfo.getRelativeY();

		ContainerBox container = clickedBox.getContainer();
		if (clickedBox != container) {
			container.createTextBox("Text", x - clickedBox.getX(), y - clickedBox.getY(), 40, 40);
			return;
		}
		container.createTextBox("Text", x, y, 40, 40);
	}

	protected void onEditorClicked(LayoutClickEvent event) {
		Component clickedComponent = event.getClickedComponent();

		boolean nocChildLayoutClicked = clickedComponent == null;
		UiBox clickedBox = nocChildLayoutClicked ? editor : UiBox.of(clickedComponent);

		MouseButton mouseButton = event.getButton();

		// relative to editor, not the clickedComponent!
		int editorRelativeX = event.getRelativeX();
		int editorRelativeY = event.getRelativeY();
		int clientX = event.getClientX();
		int clientY = event.getClientY();

		// do not use event below, as clickedComponent can be null

		// so we need to subtract all nested box positions to get the relative inside the arbitraryly nested
		// clickedBox

		int boxRelativeX = editorRelativeX;
		int boxRelativeY = editorRelativeY;
		UiBox box = clickedBox;

		while (!box.isEditor()) {
			ContainerBox parent = box.getParent();

			boxRelativeX -= box.getX();
			boxRelativeY -= box.getY();

			box = parent;
		}

		if (MouseButton.LEFT.equals(mouseButton)) {
			select(clickedBox);
		}
		if (MouseButton.RIGHT.equals(mouseButton)) {
			editorContextMenu.showContextMenu(clickedBox, boxRelativeX, boxRelativeY, clientX, clientY, 200, 200);
		}
	}

	public void select(UiBox clickedBox) {
		editor.clearSelection();
		editorProperties.clear();
		currentlySelectedBox = null;
		onEditableComponentUnselect();

		if (!clickedBox.isEditor()) {
			currentlySelectedBox = clickedBox;
			clickedBox.select();
			onEditableComponentSelect(clickedBox);
		}
	}

}
