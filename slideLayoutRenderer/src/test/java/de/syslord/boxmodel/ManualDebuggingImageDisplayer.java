package de.syslord.boxmodel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ManualDebuggingImageDisplayer {

	// switch on for manual image inspection
	public static final boolean SHOW_DEBUG_IAMGE = true;

	public static final String DEBUGGING_IMAGE_SAVE_PATH = "D:\\awt.png";

	public static void showImage(BufferedImage render) throws IOException {
		if (SHOW_DEBUG_IAMGE) {
			showImagForRealAndDebugging(render);
		}
	}

	private static void showImagForRealAndDebugging(BufferedImage render) throws IOException {
		ImageIO.write(render, "png", new File(DEBUGGING_IMAGE_SAVE_PATH));
		java.awt.Desktop.getDesktop().open(new File(DEBUGGING_IMAGE_SAVE_PATH));
	}
}
