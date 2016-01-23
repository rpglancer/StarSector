package util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {
	private BufferedImage Image;
	
	public BufferedImage loadImage(String path) throws IOException{
		Image = ImageIO.read(getClass().getResource(path));
		return Image;
	}
}
