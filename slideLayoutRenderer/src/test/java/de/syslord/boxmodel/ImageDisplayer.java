package de.syslord.boxmodel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDisplayer {

	// switch on for debugging

	public static final boolean showDebugImage = false;

	public static final String showImagePath = "Y:\\awt.png";

	public static void showImage(BufferedImage render) throws IOException {
		if (showDebugImage) {
			showImagForRealAndDebugging(render);
		}
	}

	private static void showImagForRealAndDebugging(BufferedImage render) throws IOException {
		ImageIO.write(render, "png", new File(showImagePath));
		java.awt.Desktop.getDesktop().open(new File(showImagePath));
	}
}
