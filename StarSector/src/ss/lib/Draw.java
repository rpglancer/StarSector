package ss.lib;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.Vector;

import ss.engine.Hud;
import ss.engine.StarSector;

public class Draw {
/**
 * @param g
 * @param s	The shape to draw
 * @param bSize	The border [outline] thickness of the shape
 * @param border The color of the border
 * @param fill The color to fill the shape with
 */
	public static void shape(Graphics g, Shape s, int bSize, Color border, Color fill){
		Graphics2D g2d = (Graphics2D)g;
		Color prevC = g2d.getColor();
		if(fill != null){
			g2d.setColor(fill);
			g2d.fill(s);
		}
		if(bSize > 0){
			g2d.setColor(border);
			BasicStroke bs = new BasicStroke(bSize);
			g2d.setStroke(bs);
			g2d.draw(s);
		}
		g2d.setColor(prevC);
	}
	
	public static void circle_centered(Graphics G, Coords coords, int rad, Color color, boolean p){
		int x,y;
		x = (int)coords.GetX();
		if(p) y = (int)coords.GetY();
		else y = (int)coords.GetZ();
		Color prevC = G.getColor();
		G.setColor(color);
		G.drawOval(x - rad, y - rad, rad * 2, rad * 2);
		G.setColor(prevC);
	}
	
	public static void history(Graphics G, Vector<Coords> history){
		if(history == null) return;
		Color colorP = G.getColor();
		Font fontP = G.getFont();
		G.setColor(Color.yellow);
		for(int i = 0; i < history.size(); i++){
			if(i % 3 == 0) G.setColor(G.getColor().darker());
			circle_centered(G, history.get(i), (int)(StarSector.PPKM * 1), G.getColor(), Hud.getP());
		}
		G.setColor(colorP);
		G.setFont(fontP);
	}
	
	public static void line(Graphics G, Coords begin, Coords end, Color color, boolean p){
		Color prevC = G.getColor();
		G.setColor(color);
		int sx,ex,sy,ey;
		sx = (int)begin.GetX();
		ex = (int)end.GetX();
		if(p){
			sy = (int)begin.GetY();
			ey = (int)end.GetY();
		}
		else{
			sy = (int)begin.GetZ();
			ey = (int)end.GetZ();
		}
		G.drawLine(sx, sy, ex, ey);
		G.setColor(prevC);
	}
	
	public static void sprite(Graphics G, BufferedImage BI, double X, double Y){
		G.drawImage(BI, (int)X, (int)Y, null);
	}
	
	public static void sprite_centered(Graphics G, BufferedImage BI, double X, double Y){
		G.drawImage(BI, (int)(X - BI.getWidth() / 2), (int)(Y - BI.getHeight() / 2), null);
	}
	
	public static void square_centered(Graphics G, Coords coords, int size, Color color, boolean p){
		int x,y;
		x = (int)coords.GetX();
		if(p) y = (int)coords.GetY();
		else y = (int)coords.GetZ();
		Color prevC = G.getColor();
		G.setColor(color);
		G.drawRect(x - size, y - size, size * 2, size * 2);
		G.setColor(prevC);
	}
	
}
