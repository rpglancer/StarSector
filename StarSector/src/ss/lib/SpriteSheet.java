package ss.lib;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage Image;

	public SpriteSheet(BufferedImage image){
		this.Image = image;
	}
	
	public BufferedImage grabImage(int c, int r, int w, int h){
		if(c < 1 || r < 1){
			System.out.println("ERR: grabImage column or row is 0 vaue. Null returned.");
			return null;
		}
		return Image.getSubimage(c * w - w, r * h - h, w, h);
	}
}
