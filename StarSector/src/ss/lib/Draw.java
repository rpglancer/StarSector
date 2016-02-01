package ss.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;

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
			ey = (int)begin.GetZ();
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
		int x,y,w;
		x = (int)coords.GetX();
		if(p) y = (int)coords.GetY();
		else y = (int)coords.GetZ();
		Color prevC = G.getColor();
		G.setColor(color);
		G.drawRect(x - size, y - size, size * 2, size * 2);
		G.setColor(prevC);
	}
	
}
