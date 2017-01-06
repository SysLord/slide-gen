package de.syslord.slidegen.editor.ui.editor;

import java.util.function.Consumer;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import de.syslord.slidegen.editor.ui.base.BaseView;
import de.syslord.slidegen.editor.ui.elements.ContainerBox;
import de.syslord.slidegen.editor.ui.elements.UiBox;
import de.syslord.slidegen.editor.util.KeyboardKey;
import de.syslord.slidegen.editor.util.ModifierKey;
import de.syslord.slidegen.editor.util.UiUtil;

public abstract class BaseEditorView<T extends EditorModel> extends BaseView<T> {

	private static final int ARROW_KEYS_RASTER = 20;

	private static final int NEW_BOX_HEIGHT = 40;

	private static final int NEW_BOX_WIDTH = 100;

	private static final String EDITOR_STYLE = "editor";

	private static final String EDITOR_WRAPPER_STYLE = "editor-wrapper";

	private static final long serialVersionUID = 1L;

	protected Editor editor;

	protected GridLayout editorWrapper;

	private EditorContextMenu editorContextMenu;

	private UiBox currentlySelectedBox = null;

	protected EditorProperties editorProperties = new EditorProperties(component -> stylePropertyComponents(component));

	protected abstract void onEditableComponentSelect(UiBox clickedBox);

	protected abstract void onEditableComponentUnselect();

	@Override
	protected void initView() {
		super.initView();

		editorContextMenu = new EditorContextMenu(menuInfo -> createEditorContextMenu(menuInfo));
	}

	private void stylePropertyComponents(Component component) {
		if (TextArea.class.isAssignableFrom(component.getClass())) {
			component.setWidth("200px");
		} else {
			component.setWidth("100px");
		}
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

		editor = new Editor(editorLayout, createTreeListener());
		editor.getLayout().addLayoutClickListener(event -> onEditorClicked(event));
		editor.setBackdropImage("slide_backdrop.png");
		initArrowKeyListeners();

		// This needs to be a grid to correctly wrap the absolute layout without collapsing.
		editorWrapper = new GridLayout(1, 1, editorLayout);
		editorWrapper.addStyleName(EDITOR_WRAPPER_STYLE);
		editorWrapper.setSpacing(false);
		editorWrapper.setMargin(false);

		editor.getEditorTree().addTreeClickAction(box -> select(box));
	}

	private TreeListener<UiBox, ContainerBox> createTreeListener() {
		return new TreeListener<UiBox, ContainerBox>() {

			@Override
			public void setNewParent(UiBox child, ContainerBox newParent) {
				if (child.getParent() != newParent) {
					child.move(newParent);
				}
			}

		};
	}

	public void initArrowKeyListeners() {
		Consumer<KeyboardKey> moveLeftRight = key -> {
			if (currentlySelectedBox != null) {
				currentlySelectedBox.setX(KeyboardKey.ARROW_RIGHT.equals(key) ? 20 : -20, true);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};
		Consumer<KeyboardKey> moveUpDown = key -> {
			if (currentlySelectedBox != null) {
				currentlySelectedBox.setY(KeyboardKey.ARROW_DOWN.equals(key) ? 20 : -20, true);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};

		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_right", KeyboardKey.ARROW_RIGHT, moveLeftRight));
		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_left", KeyboardKey.ARROW_LEFT, moveLeftRight));
		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_down", KeyboardKey.ARROW_DOWN, moveUpDown));
		editor.getLayout().addShortcutListener(UiUtil.createShortCut("arrow_up", KeyboardKey.ARROW_UP, moveUpDown));

		Consumer<KeyboardKey> changeWidth = key -> {
			if (currentlySelectedBox != null) {
				int oldWidth = currentlySelectedBox.getWidth();
				currentlySelectedBox.setWidth(KeyboardKey.ARROW_RIGHT.equals(key) ? oldWidth + ARROW_KEYS_RASTER : oldWidth - 20);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};
		Consumer<KeyboardKey> changeHeight = key -> {
			if (currentlySelectedBox != null) {
				int oldHeight = currentlySelectedBox.getHeight();
				currentlySelectedBox.setHeight(KeyboardKey.ARROW_DOWN.equals(key) ? oldHeight + 20 : oldHeight - 20);

				eventBus.fire(new BoxPropertyChangedEvent());
			}
		};

		ModifierKey ctrl = ModifierKey.CTRL;
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_right", KeyboardKey.ARROW_RIGHT, changeWidth, ctrl));
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_left", KeyboardKey.ARROW_LEFT, changeWidth, ctrl));
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_down", KeyboardKey.ARROW_DOWN, changeHeight, ctrl));
		editor.getLayout().addShortcutListener(
				UiUtil.createShortCut("arrow_up", KeyboardKey.ARROW_UP, changeHeight, ctrl));
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
			ContainerBox newBox = container.createBox(x + clickedBox.getX(), y + clickedBox.getY(), NEW_BOX_WIDTH,
					NEW_BOX_HEIGHT);
			select(newBox);
			return;
		}
		ContainerBox newBox = container.createBox(x, y, NEW_BOX_WIDTH, NEW_BOX_HEIGHT);
		select(newBox);
	}

	private void onAddTextBoxClicked(EditorContextMenu.ContextMenuOpenInfo menuInfo) {
		UiBox clickedBox = menuInfo.getClickedBox();

		int x = menuInfo.getRelativeX();
		int y = menuInfo.getRelativeY();

		ContainerBox container = clickedBox.getContainer();
		if (clickedBox != container) {
			UiBox newBox = container.createTextBox("Text", x + clickedBox.getX(), y + clickedBox.getY(), NEW_BOX_WIDTH,
					NEW_BOX_HEIGHT);
			select(newBox);
			return;
		}
		UiBox newBox = container.createTextBox("Text", x, y, NEW_BOX_WIDTH, NEW_BOX_HEIGHT);
		select(newBox);
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

	protected void select(UiBox clickedBox) {
		clearSelection();

		if (!clickedBox.isEditor()) {
			currentlySelectedBox = clickedBox;
			clickedBox.select();
			editor.getEditorTree().selectTreeItem(clickedBox);
			onEditableComponentSelect(clickedBox);
		}
	}

	protected void clearSelection() {
		editor.clearSelection();
		editorProperties.clear();
		currentlySelectedBox = null;
		onEditableComponentUnselect();
	}

	protected void clearEditor() {
		editor.removeAllBoxes();
	}

}
