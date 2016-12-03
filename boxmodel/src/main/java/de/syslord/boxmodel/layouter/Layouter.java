package de.syslord.boxmodel.layouter;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.LineBox;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.SizeProperty;
import de.syslord.boxmodel.Stretch;
import de.syslord.boxmodel.TextBox;
import de.syslord.boxmodel.renderer.RenderHelper;

public class Layouter {

	public static void layout(LayoutableBox root) {

		inheritMaxAndFixSizesTopDown(null, root);
		calcHeightNeeded(null, root);

		// collect height wishes
		updateBoxSizeWithHeightNeededContentBottomUp(null, root);

		// push floating boxes down when siblings grow (child order important)
		updateBoxYForEveryBoxChildren(null, root);

		// find largest child and try to grow parent accordingly
		updateBoxSizeWithChildrenHeightsBottomUp(null, root);

		// test!!
		applyStretchChildrenToLargestSibling_TopDown(null, root);

		// crop/hide children that are too large for a parent box
		updateBoxSizeWithYTopDown(null, root);

		// before exporting to renderer we want absolute xy values
		updateBoxAbsoluteXYTopDown(null, root);
	}

	private static void updateBoxYForEveryBoxChildren(LayoutableBox parent, LayoutableBox box) {
		int growY = 0;
		for (LayoutableBox b : box.getChildren()) {
			if (b.hasProp(PositionProperty.FLOAT)) {
				b.setY(b.getY() + growY);
				// } else if (b.hasProp(PositionProperty.FIX_BOTTOM)) {
				// TODO
			}

			growY += b.getGrownHeight();
		}

		for (LayoutableBox b : box.getChildren()) {
			updateBoxYForEveryBoxChildren(box, b);
		}
	}

	private static void updateBoxAbsoluteXYTopDown(LayoutableBox parent, LayoutableBox box) {
		int x = parent == null ? 0 : parent.getAbsoluteX();
		int y = parent == null ? 0 : parent.getAbsoluteY();

		box.setAbsoluteY(box.getY() + y);
		box.setAbsoluteX(box.getX() + x);

		for (LayoutableBox b : box.getChildren()) {
			updateBoxAbsoluteXYTopDown(box, b);
		}
	}

	private static void calcHeightNeeded(LayoutableBox parent, LayoutableBox box) {

		if (box instanceof TextBox) {
			TextBox tbox = (TextBox) box;
			int heightNeeded = RenderHelper.getHeight(tbox.getFont(), tbox.getContent(), box.getContentWidth());
			box.setHeightNeeded(heightNeeded + 2 * box.getPadding() + 2 * box.getMargin());
		} else if (box instanceof LineBox) {
			LineBox lbox = (LineBox) box;
			box.setHeightNeeded(lbox.getHeight());
		}

		for (LayoutableBox child : box.getChildren()) {
			calcHeightNeeded(box, child);
		}
	}

	private static void inheritMaxAndFixSizesTopDown(LayoutableBox parent, LayoutableBox box) {
		// erbe maxsize und fix size von parent box.
		if (parent != null) {
			if (parent.hasProp(SizeProperty.FIX)) {
				int parentFix = (int) parent.getProp(SizeProperty.FIX);

				if (box.hasProp(SizeProperty.MAX)
						&& (int) box.getProp(SizeProperty.MAX) < parentFix) {
				} else {
					box.setProp(SizeProperty.MAX, parent.getProp(SizeProperty.FIX));
				}

			} else if (parent.hasProp(SizeProperty.MAX)) {
				box.setProp(SizeProperty.MAX, parent.getProp(SizeProperty.MAX));
			}
		}

		// children space requirements
		for (LayoutableBox b : box.getChildren()) {
			inheritMaxAndFixSizesTopDown(box, b);
		}
	}

	private static void applyStretchChildrenToLargestSibling_TopDown(LayoutableBox parent, LayoutableBox box) {
		int mostGrownChild = box.getChildren().stream()
			.map(b -> b.getGrownHeight())
			.mapToInt(Integer::intValue)
			.max()
			.orElse(0);
		int largestChild = box.getChildren().stream()
			.map(b -> b.getHeight())
			.mapToInt(Integer::intValue)
			.max()
			.orElse(0);

		for (LayoutableBox child : box.getChildren()) {
			if (child.hasProp(Stretch.GROW_LARGEST)) {
				child.setSize(mostGrownChild, false);
			}
			if (child.hasProp(Stretch.LARGEST)) {
				child.setSize(largestChild, false);
			}
		}
		for (LayoutableBox child : box.getChildren()) {
			applyStretchChildrenToLargestSibling_TopDown(box, child);
		}

	}

	private static void updateBoxSizeWithYTopDown(LayoutableBox parent, LayoutableBox box) {
		if (parent != null) {
			int parentHeight = parent.getHeight();
			box.updateSizeIncludingYLimits(parentHeight);
		}

		for (LayoutableBox b : box.getChildren()) {
			updateBoxSizeWithYTopDown(box, b);
		}

	}

	private static void updateBoxSizeWithHeightNeededContentBottomUp(LayoutableBox parent, LayoutableBox box) {
		for (LayoutableBox b : box.getChildren()) {
			updateBoxSizeWithHeightNeededContentBottomUp(box, b);
		}

		// shrinking allowd
		box.setSize(box.getHeightNeeded(), true);
	}

	private static void updateBoxSizeWithChildrenHeightsBottomUp(LayoutableBox parent, LayoutableBox box) {
		// inherit size
		for (LayoutableBox b : box.getChildren()) {
			updateBoxSizeWithChildrenHeightsBottomUp(box, b);
		}

		int largestChildSpaceUsage = box.getChildren().stream()
			.map(b -> b.getHeight() + b.getY())
			.mapToInt(Integer::intValue)
			.max()
			.orElse(0);

		box.setSize(largestChildSpaceUsage, false);
	}

}
