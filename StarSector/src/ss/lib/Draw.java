package ss.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ss.StarSector;

public class Draw {
	
	public static void box(Graphics G, Rectangle R, int BSize, Color Border, Color Fill){
		Graphics2D G2D = (Graphics2D) G;
		Color PrevC = G2D.getColor();
		
		if(BSize < 1){
			G2D.setColor(Fill);
			G2D.fill(R);
		}
		else{
			G2D.setColor(Border);
			G2D.fill(R);
			Rectangle Temp = new Rectangle(R);
			Temp.setRect(Temp.getX() + BSize, Temp.getY() + BSize, Temp.getWidth() - (BSize *2), Temp.getHeight() - (BSize * 2));
			G2D.setColor(Fill);
			G2D.fill(Temp);
		}
		G2D.setColor(PrevC);
	}

	public static void hud(Graphics G){
		Graphics2D G2D = (Graphics2D) G;
		Font PrevF = G2D.getFont();
		Color PrevC = G2D.getColor();
		Rectangle Rect = new Rectangle(0,0,StarSector.WIDTH,StarSector.HEIGHT);
		Draw.box(G, Rect, 2, Color.GREEN, Color.BLACK);
		G2D.setFont(PrevF);
		G2D.setColor(PrevC);
	}
	
	public static void sprite(Graphics G, BufferedImage BI, double X, double Y){
		G.drawImage(BI, (int)X, (int)Y, null);
	}
	
	public static void sprite_centered(Graphics G, BufferedImage BI, double X, double Y){
		G.drawImage(BI, (int)(X - BI.getWidth() / 2), (int)(Y - BI.getHeight() / 2), null);
	}
	
}
