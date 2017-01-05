package de.syslord.slidegen.editor.util;

import java.util.function.Consumer;
import java.util.stream.Stream;

import com.vaadin.event.ShortcutListener;

public class UiUtil {

	// public static UiBoxData getUiBox(Component component) {
	// // assume we have an AbstractComponents which is very much probable as it is the only implementation
	// // of Component
	// AbstractComponent ac = (AbstractComponent) component;
	//
	// Object data = ac.getData();
	// return (UiBoxData) data;
	// }
	//
	// public static void setUiBox(Component component, UiBoxData uiBox) {
	// AbstractComponent ac = (AbstractComponent) component;
	// ac.setData(uiBox);
	// }
	//
	// public static boolean isEditor(Component component) {
	// return Editor.class.isAssignableFrom(component.getClass());
	// }

	// public static UiBox getParent(UiBox box) {
	// if (isEditor(box)) {
	// return box;
	// }
	//
	// Optional<AbsoluteLayout> areWeBox = EditorComponents.getBox(component);
	//
	// push(() -> createBox(parent,
	// menuInfo.getRelativeX(), relativeY,
	// 40, 40));
	//
	// Component childComponentClicked = null;
	//
	// Optional<AbsoluteLayout> maybeBox = EditorComponents.getBox(clicked);
	// if (!maybeBox.isPresent()) {
	// childComponentClicked = clicked;
	// // parent must be a box, as only box can hold other elements
	// maybeBox = EditorComponents.getBox(clicked.getParent());
	// }
	//
	// if (maybeBox.isPresent()) {
	// AbsoluteLayout box = maybeBox.get();
	//
	// if (childComponentClicked != null) {
	//
	// } else {
	// push(() -> createBox(box,
	// relativeX, relativeY,
	// 40, 40));
	// }
	// }
	// return null;
	// }

	public static ShortcutListener createShortCut(String caption, KeyboardKey key, Consumer<KeyboardKey> action) {
		return createShortcut(caption, key, action, new int[] {});
	}

	private static ShortcutListener createShortcut(String caption, KeyboardKey key, Consumer<KeyboardKey> action, int[] modifiers) {
		ShortcutListener shortcut = new ShortcutListener(caption, key.getCode(), modifiers) {

			private static final long serialVersionUID = -2630182663706626973L;

			@Override
			public void handleAction(Object sender, Object target) {
				action.accept(key);
			}

		};
		return shortcut;
	}

	public static ShortcutListener createShortCut(String caption, KeyboardKey key, Consumer<KeyboardKey> action, ModifierKey... modifiers) {
		int[] intModifiers = Stream.of(modifiers)
			.mapToInt(m -> m.getCode())
			.toArray();

		return createShortcut(caption, key, action, intModifiers);
	}
}
