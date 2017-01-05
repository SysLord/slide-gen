package de.syslord.slidegen.editor.ui.editor;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDragMode;

import de.syslord.slidegen.editor.ui.elements.ContainerBox;
import de.syslord.slidegen.editor.ui.elements.UiBox;

// Should EditorTree extend Tree instead of wrapping it?
public class EditorTree {

	private HierarchicalContainer treeContainer = new HierarchicalContainer();

	private Tree tree;

	private TreeListener<UiBox, ContainerBox> treeListener;

	public EditorTree(TreeListener<UiBox, ContainerBox> treeListener) {
		this.treeListener = treeListener;
		createTreeComponent();
	}

	public List<UiBox> getOrderedChildren(ContainerBox containerBox) {
		Collection<?> children = treeContainer.getChildren(containerBox);

		// Why would they return null, and the javadoc says nothing.
		if (children == null) {
			return ImmutableList.of();
		}

		return children.stream()
			.map(c -> (UiBox) c)
			.collect(Collectors.toList());
	}

	public void addTreeItem(UiBox box) {
		treeContainer.addItem(box);
		tree.setItemCaption(box, box.getTreeCaption());

		treeContainer.setChildrenAllowed(box, false);
	}

	public void addTreeItem(UiBox box, UiBox parent) {
		addTreeItem(box);
		treeContainer.setChildrenAllowed(parent, true);
		treeContainer.setParent(box, parent);
	}

	private void createTreeComponent() {
		tree = new Tree("Hierarchy");
		tree.setHeight("100%");

		tree.setContainerDataSource(treeContainer);
		tree.setImmediate(true);

		tree.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		tree.addItemSetChangeListener(x -> expandAll(tree));

		tree.setDragMode(TreeDragMode.NODE);
		tree.setDropHandler(new TreeDropHandler<UiBox, ContainerBox>(tree, treeListener, ContainerBox.class));
	}

	public void addTreeClickAction(Consumer<UiBox> action) {
		tree.addItemClickListener(clickEvent -> {
			UiBox uiBox = (UiBox) clickEvent.getItemId();
			action.accept(uiBox);
		});

	}

	private void expandAll(Tree tree) {
		for (Object id : tree.rootItemIds()) {
			tree.expandItemsRecursively(id);
		}
	}

	public Tree getTreeComponent() {
		return tree;
	}

	public void removeTreeItem(UiBox uiBox) {
		treeContainer.removeItemRecursively(uiBox);
	}

	public void selectTreeItem(UiBox clickedBox) {
		tree.select(clickedBox);
	}

}
