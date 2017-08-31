package ss.engine;

import java.awt.Color;
import java.awt.Graphics;

import ss.lib.Draw;
import ss.lib.Fonts;
import ss.lib.Text;
import ss.type.ELEMENT;

public class Chatter {

	private long timeDisplayed;
	private String message;
	
	public Chatter(String msg){
		message = msg;
	}
	
	public boolean timeToDie(int ms){
		return System.currentTimeMillis() - timeDisplayed >= ms;
	}
	
	public void makeCurrent(){
		timeDisplayed = System.currentTimeMillis();
	}
	
	public void render(Graphics G){
		Draw.shape(G, ELEMENT.HUD_OVW_CHAT.getArea(), 1, Color.darkGray, Color.black);
		Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_OVW_CHAT.getArea(), Text.alignCenter(), Text.alignMiddle(), message);
	}
}
