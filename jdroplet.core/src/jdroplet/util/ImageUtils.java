package jdroplet.util;

import jdroplet.core.SystemConfig;
import jdroplet.exceptions.ApplicationException;
import jdroplet.net.WebRequest;
import jdroplet.net.WebResponse;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ImageUtils {
	public static final int IMAGE_UNKNOWN = -1;
	public static final int IMAGE_JPEG = 0;
	public static final int IMAGE_PNG = 1;
	public static final int IMAGE_GIF = 2;

	public static void createMaskImage(BufferedImage srcImg, BufferedImage maskImg) {
		int x = 0;
		int y = 0;
		int width = srcImg.getWidth(null);
		int height = srcImg.getHeight(null);

		int wideth_biao = maskImg.getWidth(null);//水印图片宽   
		int height_biao = maskImg.getHeight(null);//水印图片高
		
		x = width - wideth_biao;
		y = height - height_biao;
		
		createMaskImage(srcImg, maskImg, x, y);
	}
	
	public static void createMaskImage(BufferedImage srcImg, BufferedImage maskImg, int x, int y){
		try{
			Graphics2D g = srcImg.createGraphics();
			int wideth_biao = maskImg.getWidth(null);//水印图片宽   
			int height_biao = maskImg.getHeight(null);//水印图片高
			
			g.drawImage(maskImg, x, y, wideth_biao, height_biao, null);  
			g.dispose();
		}catch(Exception e){
		}
	}
		
	public static void createMaskText(BufferedImage srcImg, String text, String fontName, int fontStyle, int fontSize, int fontColor, int x, int y) {
		Font font = null;
		
		font = new Font(fontName, fontStyle, fontSize);
		createMaskText(srcImg, text, font, fontColor, x, y);
	}
		
	public static void createMaskText(BufferedImage srcImg, String text, Font font, int fontColor, int x, int y) {
		Color color = new Color(fontColor);
		
		createMaskText(srcImg, text, font, color, x, y);
	}
	
	
	public static void createMaskText(BufferedImage srcImg, String text, String fontName, int fontStyle, int fontSize, Color color, int x, int y) {
		Font font = null;
		
		font = new Font(fontName, fontStyle, fontSize);
		createMaskText(srcImg, text, font, color, x, y);
	}
	
	public static void createMaskText(BufferedImage srcImg, String text, Font font, Color color, int x, int y) {
		try {
			Graphics2D g = srcImg.createGraphics();

			g.setColor(color);
			g.setFont(font);
			g.drawString(text, x, y);
			g.dispose();
		} catch (Exception e) {
		}
	}

	public static BufferedImage getRAWImage(String file) {
		
		if (FileUtil.isLocalFile(file)) {
			try {
				return ImageIO.read(new File(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			byte[] bytes =null;
			
			bytes = getBytes(file);
			try {
				return ImageIO.read( new ByteArrayInputStream(bytes) );
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static byte[] getBytes(String url) {
		byte[] data = null;
		WebRequest req = null;
		WebResponse resp = null;
		HashMap<String, String> propertys = null;
		Pattern pattern = null;
		Matcher matcher = null;
		String referer = null;
		
		pattern = Pattern.compile("[^.]+\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(url);
		if (matcher.find()) {
			String key  = "app.http.reffer." + matcher.group();
			
			referer = SystemConfig.getProperty(key);
		}
		
		if (TextUtils.isEmpty(referer))
			referer = url;
		
		propertys = new HashMap<String, String>();
		propertys.put("Referer",  referer);
		
		req = new WebRequest();
		try {
			resp = req.create(url, null, propertys);
		} catch (Exception ex) {
			throw new ApplicationException(ex.getMessage());
		}
		data = resp.getContent();	
		return data;
	}
	/**
	 * Resizes an image
	 * 
	 * @param imgName The image name to resize. Must be the complet path to the file
	 * @param type int
	 * @param maxWidth The image's max width
	 * @param maxHeight The image's max height
	 * @return A resized <code>BufferedImage</code>
	 */
	public static BufferedImage resizeImage(String imgName, int type, int maxWidth, int maxHeight)
	{
		try {
			return resizeImage(ImageIO.read(new File(imgName)), type, maxWidth, maxHeight);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Resizes an image.
	 * 
	 * @param image
	 *            The image to resize
	 * @param maxWidth
	 *            The image's max width
	 * @param maxHeight
	 *            The image's max height
	 * @return A resized <code>BufferedImage</code>
	 * @param type
	 *            int
	 */
	public static BufferedImage resizeImage(BufferedImage image, int type, int maxWidth, int maxHeight)
	{
		Dimension largestDimension = new Dimension(maxWidth, maxHeight);

		// Original size
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		float aspectRatio = (float) imageWidth / imageHeight;

		if (imageWidth > maxWidth || imageHeight > maxHeight) {
			if ((float) largestDimension.width / largestDimension.height > aspectRatio) {
				largestDimension.width = (int) Math.ceil(largestDimension.height * aspectRatio);
			}
			else {
				largestDimension.height = (int) Math.ceil(largestDimension.width / aspectRatio);
			}			
			imageWidth = largestDimension.width;
			imageHeight = largestDimension.height;
		}

		return createHeadlessSmoothBufferedImage(image, type, imageWidth, imageHeight);
	}

	/**
	 * Saves an image to the disk.
	 * 
	 * @param image  The image to save
	 * @param toFileName The filename to use
	 * @param type The image type. Use <code>ImageUtils.IMAGE_JPEG</code> to save as JPEG images,
	 *  or <code>ImageUtils.IMAGE_PNG</code> to save as PNG.
	 * @return <code>false</code> if no appropriate writer is found
	 */
	public static boolean saveImage(BufferedImage image, int type, String toFileName)
	{
		try {
			return ImageIO.write(image, type == IMAGE_JPEG ? "jpg" : "png", new File(toFileName));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean saveImage(BufferedImage image, int type, OutputStream os)
	{
		try {
			return ImageIO.write(image, type == IMAGE_JPEG ? "jpg" : "png", os);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Compress and save an image to the disk. Currently this method only supports JPEG images.
	 * 
	 * @param image The image to save
	 * @param toFileName The filename to use
	 * @param type The image type. Use <code>ImageUtils.IMAGE_JPEG</code> to save as JPEG images,
	 * or <code>ImageUtils.IMAGE_PNG</code> to save as PNG.
	 */
	public static void saveCompressedImage(BufferedImage image, String toFileName, int type)
	{
		try {
			if (type == IMAGE_PNG) {
				throw new UnsupportedOperationException("PNG compression not implemented");
			}

			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer;
			writer = iter.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(new File(toFileName));
			writer.setOutput(ios);

			ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());

			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwparam.setCompressionQuality(0.7F);

			writer.write(null, new IIOImage(image, null, null), iwparam);

			ios.flush();
			writer.dispose();
			ios.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a <code>BufferedImage</code> from an <code>Image</code>. This method can
	 * function on a completely headless system. This especially includes Linux and Unix systems
	 * that do not have the X11 libraries installed, which are required for the AWT subsystem to
	 * operate. This method uses nearest neighbor approximation, so it's quite fast. Unfortunately,
	 * the result is nowhere near as nice looking as the createHeadlessSmoothBufferedImage method.
	 * 
	 * @param image  The image to convert
	 * @param w The desired image width
	 * @param h The desired image height
	 * @return The converted image
	 * @param type int
	 */
	public static BufferedImage createHeadlessBufferedImage(BufferedImage image, int type, int width, int height) {
		if (type == ImageUtils.IMAGE_PNG && hasAlpha(image)) {
			type = BufferedImage.TYPE_INT_ARGB;
		}
		else {
			type = BufferedImage.TYPE_INT_RGB;
		}

		BufferedImage bi = new BufferedImage(width, height, type);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				bi.setRGB(x, y, image.getRGB(x * image.getWidth() / width, y * image.getHeight() / height));
			}
		}

		return bi;
	}

	/**
	 * Creates a <code>BufferedImage</code> from an <code>Image</code>. This method can
	 * function on a completely headless system. This especially includes Linux and Unix systems
	 * that do not have the X11 libraries installed, which are required for the AWT subsystem to
	 * operate. The resulting image will be smoothly scaled using bilinear filtering.
	 * 
	 * @param source The image to convert
	 * @param w The desired image width
	 * @param h The desired image height
	 * @return The converted image
	 * @param type  int
	 */
	public static BufferedImage createHeadlessSmoothBufferedImage(BufferedImage source, int type, int width, int height) {
		if (type == ImageUtils.IMAGE_PNG && hasAlpha(source)) {
			type = BufferedImage.TYPE_INT_ARGB;
		}
		else {
			type = BufferedImage.TYPE_INT_RGB;
		}

		BufferedImage dest = new BufferedImage(width, height, type);

		int sourcex;
		int sourcey;

		double scalex = (double) width / source.getWidth();
		double scaley = (double) height / source.getHeight();

		int x1;
		int y1;

		double xdiff;
		double ydiff;

		int rgb;
		int rgb1;
		int rgb2;

		for (int y = 0; y < height; y++) {
			sourcey = y * source.getHeight() / dest.getHeight();
			ydiff = scale(y, scaley) - sourcey;

			for (int x = 0; x < width; x++) {
				sourcex = x * source.getWidth() / dest.getWidth();
				xdiff = scale(x, scalex) - sourcex;

				x1 = Math.min(source.getWidth() - 1, sourcex + 1);
				y1 = Math.min(source.getHeight() - 1, sourcey + 1);

				rgb1 = getRGBInterpolation(source.getRGB(sourcex, sourcey), source.getRGB(x1, sourcey), xdiff);
				rgb2 = getRGBInterpolation(source.getRGB(sourcex, y1), source.getRGB(x1, y1), xdiff);

				rgb = getRGBInterpolation(rgb1, rgb2, ydiff);

				dest.setRGB(x, y, rgb);
			}
		}

		return dest;
	}

	private static double scale(int point, double scale) {
		return point / scale;
	}

	private static int getRGBInterpolation(int value1, int value2, double distance) {
		int alpha1 = (value1 & 0xFF000000) >>> 24;
		int red1 = (value1 & 0x00FF0000) >> 16;
		int green1 = (value1 & 0x0000FF00) >> 8;
		int blue1 = (value1 & 0x000000FF);

		int alpha2 = (value2 & 0xFF000000) >>> 24;
		int red2 = (value2 & 0x00FF0000) >> 16;
		int green2 = (value2 & 0x0000FF00) >> 8;
		int blue2 = (value2 & 0x000000FF);

		int rgb = ((int) (alpha1 * (1.0 - distance) + alpha2 * distance) << 24)
			| ((int) (red1 * (1.0 - distance) + red2 * distance) << 16)
			| ((int) (green1 * (1.0 - distance) + green2 * distance) << 8)
			| (int) (blue1 * (1.0 - distance) + blue2 * distance);

		return rgb;
	}

	/**
	 * Determines if the image has transparent pixels.
	 * 
	 * @param image The image to check for transparent pixel.s
	 * @return <code>true</code> of <code>false</code>, according to the result
	 */
	public static boolean hasAlpha(Image image)
	{
		try {
			PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
			pg.grabPixels();

			return pg.getColorModel().hasAlpha();
		}
		catch (InterruptedException e) {
			return false;
		}
	}
}