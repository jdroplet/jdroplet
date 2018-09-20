package jdroplet.app.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import jdroplet.core.Page;
import jdroplet.exceptions.ApplicationException;


public abstract class ImagePage extends MasterPage {

	protected abstract BufferedImage drawImage();

	@Override
	public void show() {
		OutputStream os = null;
		BufferedImage image = null;

		isCustomContent = true;
		setContentType("image/jpg");
		response.setHeader("Pargma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		image = drawImage();
		try {
			os = response.getOutputStream();
			ImageIO.write(image, "JPEG", os);
		} catch (IOException ex) {
			throw new ApplicationException(ex.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
