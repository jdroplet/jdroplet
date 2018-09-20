package jdroplet.app.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import jdroplet.core.Page;
import jdroplet.util.GifEncoder;


public abstract class GifPage extends MasterPage {

	protected int getFrames() {
		return 10;
	}
	
	protected abstract BufferedImage drawImage(int frame);
		
	@Override
	public void show() {
		GifEncoder encoder = null;
		BufferedImage frame = null;
		OutputStream os = null;
		int frames = this.getFrames();

		isCustomContent = true;
		setContentType("image/gif");
		response.setHeader("Pargma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		try {
			os = response.getOutputStream();
		} catch (IOException e) {
		}
		
		encoder = new GifEncoder();
		encoder.start(os);
		encoder.setQuality(180);
		encoder.setDelay(100);
		encoder.setRepeat(0);

		for (int i = 0; i < frames; i++) {
			frame = drawImage(i);
			encoder.addFrame(frame);
			frame.flush();
		}
		encoder.finish();
	}

}
