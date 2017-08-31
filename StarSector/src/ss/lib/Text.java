package ss.lib;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import ss.engine.Hud;
import ss.engine.StarSector;
import ss.entity.Mobile;


public class Text {
	
	private static enum ALIGNH{
		LEFT,
		CENTER,
		RIGHT
	};
	
	private static enum ALIGNV{
		TOP,
		MIDDLE,
		BOTTOM
	};
	
	public static ALIGNH alignCenter(){
		return ALIGNH.CENTER;
	}
	
	public static ALIGNH alignLeft(){
		return ALIGNH.LEFT;
	}
	
	public static ALIGNH alignRight(){
		return ALIGNH.RIGHT;
	}
	
	public static ALIGNV alignBottom(){
		return ALIGNV.BOTTOM;
	}
	
	public static ALIGNV alignMiddle(){
		return ALIGNV.MIDDLE;
	}
	
	public static ALIGNV alignTop(){
		return ALIGNV.TOP;
	}
	
	private static final String[] Races = {
										"Ar", "Au", "Be",
										"Cu", "Ga", "Ir",
										"Mo", "Pt", "Xe"
										};
	
	private static final String[] Corps = {
	};
	
	private static final String[] StationSubject = {
										"Bio", "Plasma", "Energy"
	};
	
	private static final String[] StationFocus = {
										"Weapons", "Research", "Production"
	};
	
	private static final String[] StationTypes = {
										"Outpost", "Lab", "Station",
										"Forge", "Factory", "Complex"
	};
	
	public static enum CHAT {DEPART, CONTACT, DIRECT, FOUR};
	
	public static final String chatResponse(CHAT CS, String source, String target, String origin, String dest){
		switch(CS){
		case DEPART:
			return String.format("%s, %s with you departing %s for %s.", target, source, origin, dest);
		case CONTACT:
			return String.format("%s, %s roger radar contact.", source, target);
		case DIRECT:
			return String.format("%s cleared direct to %s.", source, dest);
		default:
			return "\0";
		}
	}

	public static Rectangle genTextArea(Graphics g, Coords src, Font font, String text){
		FontMetrics fm = g.getFontMetrics(font);
		int x = (int)src.GetX() - (fm.stringWidth(text)/2);
		int y = 0;
		if(Hud.getP()) y = (int)src.GetY();
		else y = (int)src.GetZ();
		int w = (int)fm.stringWidth(text);
		int h = (int)fm.getHeight();
		return new Rectangle(x,y,w,h);
	}
	
	public static String genName(){
		Random rand = new Random();
		rand.setSeed(System.nanoTime());
		return StationSubject[rand.nextInt(StationSubject.length)] + " " + StationFocus[rand.nextInt(StationFocus.length)] + " " + StationTypes[rand.nextInt(StationTypes.length)];
	}
	
	public static String genSerial(Mobile m){
		Random r = new Random();
		r.setSeed(System.nanoTime());
		int serial = (r.nextInt(9) + 1) * 1000;
		serial += r.nextInt(9) * 100;
		serial += r.nextInt(9) * 10;
		serial += r.nextInt(9);
		return m.getMType().getType() + "-" + serial + Races[r.nextInt(Races.length)];
	}

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
	
	public static void BoxText(Graphics g, Font f, Rectangle r, Text.ALIGNH h, Text.ALIGNV v, String text){
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

	/**
	 * For the love of god, do not use this.
	 * @param g	Graphics to manipulate
	 * @param f	Font to use
	 * @param x	X coordinate[anchor?]
	 * @param y Y coordinate[anchor?]
	 * @param text Text string to rotate.
	 */
	public static void RotateText(Graphics g, Font f, int x, int y, String text){
		Font prevF = g.getFont();
		g.setFont(f);
		Graphics2D G2D = (Graphics2D)g;
		G2D.drawString("Test", x, y);
		AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 2,x,y);	
		G2D.transform(at);	
		G2D.transform(new AffineTransform());
		G2D.setFont(prevF);
	}
}
