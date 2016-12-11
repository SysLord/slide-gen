package de.syslord.slidegen.editor.base;

import java.util.function.Function;

import com.vaadin.ui.Component;

import de.syslord.slidegen.editor.baseui.ContextWindowHandler;

public class EditorContextMenu {

	private ContextWindowHandler contextWindowHandler = new ContextWindowHandler();

	private ContextMenuGenerator contextMenuGenerator;

	protected static interface ContextMenuGenerator extends Function<ContextMenuOpenInfo, Component> {
		//
	}

	protected static class ContextMenuOpenInfo {

		private UiBox clickedBox;

		private int relativeX;

		private int relativeY;

		private Runnable contextMenuCloser;

		public ContextMenuOpenInfo(UiBox clickedBox, int relativeX, int relativeY,
				Runnable contextMenuCloser) {
			this.clickedBox = clickedBox;
			this.relativeX = relativeX;
			this.relativeY = relativeY;
			this.contextMenuCloser = contextMenuCloser;
		}

		public UiBox getClickedBox() {
			return clickedBox;
		}

		public int getRelativeX() {
			return relativeX;
		}

		public int getRelativeY() {
			return relativeY;
		}

		public void closeContextMenu() {
			contextMenuCloser.run();
		}
	}

	public EditorContextMenu(ContextMenuGenerator contextMenuGenerator) {
		this.contextMenuGenerator = contextMenuGenerator;
	}

	public void showContextMenu(UiBox clickedBox, int boxRelativeX, int boxRelativeY, int clientX, int clientY, int width,
			int height) {
		// open contextMenu
		ContextMenuOpenInfo contextMenuData = new ContextMenuOpenInfo(
				clickedBox,
				boxRelativeX, boxRelativeY,
				contextWindowHandler.getCloser());

		Component menuContent = contextMenuGenerator.apply(contextMenuData);

		// TODO externalize values
		contextWindowHandler.showContextWindow(
				menuContent,
				clientX + 5,
				clientY + 5,
				width, height);
	}

}
