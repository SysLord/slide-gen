package de.syslord.slidegen.editor.ui.editor;

import java.util.Set;

import com.google.common.collect.Sets;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDropCriterion;
import com.vaadin.ui.Tree.TreeTargetDetails;

public class TreeDropHandler<CHILD, PARENT> implements DropHandler {

	private static final long serialVersionUID = -1943502111664781099L;

	private Tree tree;

	private TreeListener<CHILD, PARENT> listener;

	private Class<? extends PARENT> containerClass;

	public TreeDropHandler(Tree tree, TreeListener<CHILD, PARENT> listener,
			Class<? extends PARENT> containerClass) {
		this.tree = tree;
		this.listener = listener;
		this.containerClass = containerClass;
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		// When we exclude certain nodes then we can't even drop before them.
		return createDropAnywhereCriterion();
	}

	private TreeDropCriterion createDropAnywhereCriterion() {
		return new TreeDropCriterion() {

			private static final long serialVersionUID = -1606788315637447301L;

			@Override
			protected Set<Object> getAllowedItemIds(DragAndDropEvent dragEvent, Tree tree) {
				return Sets.newHashSet(tree.getItemIds());
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drop(DragAndDropEvent event) {
		// Wrapper for the object that is dragged
		Transferable transferable = event.getTransferable();

		if (transferable.getSourceComponent() != tree) {
			return;
		}

		TreeTargetDetails target = (TreeTargetDetails) event.getTargetDetails();

		CHILD sourceItemId = (CHILD) transferable.getData("itemId");
		CHILD targetItemId = (CHILD) target.getItemIdOver();

		VerticalDropLocation location = target.getDropLocation();
		HierarchicalContainer container = (HierarchicalContainer) tree.getContainerDataSource();
		CHILD sourceParentId = (CHILD) container.getParent(sourceItemId);

		// prevent root nodes to be moved
		if (sourceParentId == null) {
			return;
		}

		if (location == VerticalDropLocation.MIDDLE) {
			if (isContainer(targetItemId)) {
				dropIntoContainerFirstPosition(sourceItemId, (PARENT) targetItemId, container);
			} else {
				PARENT parentId = (PARENT) container.getParent(targetItemId);
				if (parentId == null) {
					// this must be a root leaf and we don't want to put other items into root
					return;
				}
				dropIntoParentBelow(sourceItemId, targetItemId, parentId, container);
			}
		} else if (location == VerticalDropLocation.TOP) {
			dropAbove(sourceItemId, targetItemId, container);
		} else if (location == VerticalDropLocation.BOTTOM) {
			dropBelow(sourceItemId, targetItemId, container);
		}

		updateExpandAllowed(container, sourceParentId);
	}

	private void updateExpandAllowed(HierarchicalContainer container, CHILD sourceParentId) {
		if (!container.hasChildren(sourceParentId)) {
			tree.setChildrenAllowed(sourceParentId, false);
		}
	}

	@SuppressWarnings("unchecked")
	private void dropAbove(CHILD movingItem, CHILD dropAboveItem, HierarchicalContainer container) {
		PARENT parentId = (PARENT) container.getParent(dropAboveItem);
		if (parentId != null) {
			dropIntoParentBelow(movingItem, dropAboveItem, parentId, container);
			container.moveAfterSibling(dropAboveItem, movingItem);
		} else {
			dropIntoContainerFirstPosition(movingItem, (PARENT) dropAboveItem, container);
		}
	}

	@SuppressWarnings("unchecked")
	private void dropBelow(CHILD movingItem, CHILD dropBelowItem, HierarchicalContainer container) {
		// + tree root
		// | ---- ============ < insert here
		// o ---- leaf
		// o ---- leaf
		// yields null parent, but targetItemId is tree root

		PARENT parentId = (PARENT) container.getParent(dropBelowItem);
		if (parentId != null) {
			dropIntoParentBelow(movingItem, dropBelowItem, parentId, container);
		} else {
			dropIntoContainerFirstPosition(movingItem, (PARENT) dropBelowItem, container);
		}
	}

	private void dropIntoParentBelow(CHILD movingItem, CHILD dropBelowItem, PARENT parentId, HierarchicalContainer container) {
		listener.setNewParent(movingItem, parentId);

		tree.setChildrenAllowed(parentId, true);
		container.setParent(movingItem, parentId);
		container.moveAfterSibling(movingItem, dropBelowItem);
	}

	private void dropIntoContainerFirstPosition(CHILD movingItem, PARENT parentId, HierarchicalContainer container) {
		listener.setNewParent(movingItem, parentId);

		tree.setChildrenAllowed(parentId, true);
		container.setParent(movingItem, parentId);
		// undocumented: first position
		container.moveAfterSibling(movingItem, null);
	}

	private boolean isContainer(Object o) {
		return containerClass.isAssignableFrom(o.getClass());
	}

}
