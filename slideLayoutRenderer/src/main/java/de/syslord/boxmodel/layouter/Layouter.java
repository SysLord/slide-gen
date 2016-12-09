package de.syslord.boxmodel.layouter;

import de.syslord.boxmodel.LayoutableBox;
import de.syslord.boxmodel.PositionProperty;
import de.syslord.boxmodel.Stretch;
import de.syslord.boxmodel.TextBox;
import de.syslord.boxmodel.renderer.RenderHelper;

public class Layouter {

	public static void layout(LayoutableBox root) {

		/// initial ///

		// inheritMaxAndFixSizesTopDown(null, root);
		calcHeightNeeded(null, root);

		/// dynamic calculations ///

		// collect height wishes
		updateBoxHeightWithHeightNeededContentBottomUp(null, root);

		// push floating boxes down when siblings grow (child order important)
		updateBoxYForEveryBoxChildren(null, root);

		// find largest child and try to grow parent accordingly
		updateBoxSizeWithChildrenHeightsBottomUp(null, root);

		// children can grow with their siblings (stretch)
		// stretch will not affect parent growth!
		applyStretchChildrenToLargestSibling_TopDown(null, root);

		/// final steps ///

		// crop/hide children that are too large for a parent box
		updateBoxSizeWithYTopDown(null, root);

		// before exporting to renderer we want absolute xy values of the boxes calculated
		updateBoxAbsoluteXYTopDown(null, root);
	}

	private static void updateBoxYForEveryBoxChildren(LayoutableBox parent, LayoutableBox box) {
		int growY = 0;
		for (LayoutableBox b : box.getChildren()) {
			if (b.hasProp(PositionProperty.FLOAT_UP) || b.hasProp(PositionProperty.FLOAT_DOWN)) {
				b.setY(b.getY() + growY);
			}

			growY += b.getHeightChanged();
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
			int heightNeeded = RenderHelper.getHeight(tbox.getFont(), tbox.getContent(), tbox.getContentWidth());
			box.setHeightNeeded(heightNeeded + 2 * tbox.getPadding() + 2 * tbox.getMargin());
		}
		// else if (box instanceof LineBox) {
		// LineBox lbox = (LineBox) box;
		// box.setHeightNeeded(lbox.getHeight());
		// }

		for (LayoutableBox child : box.getChildren()) {
			calcHeightNeeded(box, child);
		}
	}

	private static void applyStretchChildrenToLargestSibling_TopDown(LayoutableBox parent, LayoutableBox box) {
		int mostGrownChild = box.getChildren().stream()
			.map(b -> b.getHeightChanged())
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
				// child may have shrunk or grown already (because of small content/min size)
				child.setHeight(child.getHeight() + mostGrownChild, false);
			} else if (child.hasProp(Stretch.LARGEST)) {
				child.setHeight(largestChild, false);
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

	private static void updateBoxHeightWithHeightNeededContentBottomUp(LayoutableBox parent, LayoutableBox box) {
		for (LayoutableBox b : box.getChildren()) {
			updateBoxHeightWithHeightNeededContentBottomUp(box, b);
		}

		// shrinking allowed
		box.setHeight(box.getHeightNeeded(), true);
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

		box.setHeight(largestChildSpaceUsage, false);
	}

}
