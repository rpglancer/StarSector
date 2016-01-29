package ss.lib;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import ss.StarSector;
import ss.type.ALIGNH;
import ss.type.ALIGNV;

public class Text {

	private static void alignText(Graphics g, Font f, int x, int y, int w, ALIGNH ah, String text){
		FontMetrics fm = g.getFontMetrics(f);
		int strlen = fm.stringWidth(text);
		if(ah == ALIGNH.CENTER) x = (x + w) - (w/2) - (strlen / 2);
		if(ah == ALIGNH.RIGHT) x = x + w - strlen;
		Font tempF = g.getFont();
		g.setFont(f);
		g.drawString(text, x, y);
		g.setFont(tempF);
	}
	
	private static int lineCount(Graphics g, Font f, int width, String text){
		int count = 1;
		FontMetrics m = g.getFontMetrics(f);
		if(m.stringWidth(text) < width) return count;
		else{
			String[] words = text.split(" ");
			String line = words[0];
			for(int i = 1; i < words.length; i++){
				if(m.stringWidth(line + " " + words[i]) < width){
					line += " " + words[i];
				}
				else{
					count++;
					line = words[i];
				}
			}
			if(line.trim().length() > 0){
				
			}
		}
		return count;
	}
	
	private static int textHeight(Graphics g, Font f, String text){
		Graphics temp = g.create(0, 0, StarSector.WIDTH, StarSector.HEIGHT);
		FontMetrics m = g.getFontMetrics(f);
		temp.clipRect(0, 0, m.stringWidth(text), m.getHeight());
		Rectangle2D Bounds = m.getStringBounds(text, temp);
		int th = (int)Bounds.getHeight();
		temp.dispose();
		return th;
	}
	
	private static int textWidth(Graphics g, Font f, int width, String text){
		Graphics temp = g.create(0, 0, StarSector.WIDTH, StarSector.HEIGHT);
		FontMetrics m = g.getFontMetrics(f);
		temp.clipRect(0, 0, m.stringWidth(text), m.getHeight());
		Rectangle2D Bounds = m.getStringBounds(text, temp);
		int tw = (int)Bounds.getWidth();
		temp.dispose();
		return tw;
	}
	
	public static void BoxText(Graphics g, Font f, Rectangle r, ALIGNH h, ALIGNV v, String text){
		int c = lineCount(g, f, r.x + r.width - r.x, text);
		FontMetrics m = g.getFontMetrics(f);
		int y = r.y;
		int w = r.width;
		if(v == ALIGNV.TOP) y += m.getAscent();
		if(v == ALIGNV.MIDDLE){
			if(c == 1) y += (r.height - textHeight(g,f,text)) / 2 + m.getAscent();
			else y = r.y + r.height/2 - (textHeight(g,f,text)/2) * c + (m.getDescent() / 2 * c);
		}
		if(v == ALIGNV.BOTTOM) y = (int)(r.getMaxY() - (textHeight(g,f,text) * (c - 1)) - m.getDescent());
		String[] words = text.split(" ");
		String current = words[0];
		for(int i = 1; i < words.length; i++){
			if(m.stringWidth(current + " " + words[i]) < r.x + r.width - r.x){
				current += " " + words[i];
			}
			else{
				alignText(g, f, r.x, y, r.width, h, current);
				y += m.getAscent();
				current = words[i];
			}
		}
		if(current.trim().length() > 0){
			alignText(g, f, r.x, y, r.width, h, current);
		}
	}
	
}
