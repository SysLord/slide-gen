package de.syslord.imagegen;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

/*
 * Voraussetzung: Benötigt -Djava.awt.headless=true um auf dem Server zu funktionieren.
 */
public class ImageUtil {

	public static ByteArrayOutputStream getHighQualityScaledJpeg(
			InputStream sourceStream,
			Dimension targetDimenstion,
			float quality)
			throws IOException {

		int targetWidth = targetDimenstion.getWidth();
		int targetHeight = targetDimenstion.getHeight();

		BufferedImage source = ImageIO.read(sourceStream);
		BufferedImage resize = Scalr.resize(source, Method.QUALITY, targetWidth, targetHeight);

		ByteArrayOutputStream byteArrayOutputStream = writeJpegWithQuality(resize, quality);
		return byteArrayOutputStream;
	}

	public static ByteArrayOutputStream getScaledJpeg(
			InputStream sourceStream,
			Dimension targetDimenstion,
			float quality)
			throws IOException {

		int targetWidth = targetDimenstion.getWidth();
		int targetHeight = targetDimenstion.getHeight();

		BufferedImage source = ImageIO.read(sourceStream);

		BufferedImage target = new BufferedImage(targetWidth, targetHeight, source.getType());
		Graphics2D g = target.createGraphics();

		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(source, 0, 0, targetWidth, targetHeight, null);
		g.dispose();

		ByteArrayOutputStream byteArrayOutputStream = writeJpegWithQuality(target, quality);
		return byteArrayOutputStream;
	}

	public static ByteArrayOutputStream writeJpegWithQuality(BufferedImage image, float quality) throws IOException {
		ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();

		ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
		jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpgWriteParam.setCompressionQuality(quality);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(byteArrayOutputStream);
		jpgWriter.setOutput(imageOutputStream);
		IIOImage outputImage = new IIOImage(image, null, null);
		jpgWriter.write(null, outputImage, jpgWriteParam);
		jpgWriter.dispose();

		return byteArrayOutputStream;
	}

}
