package ss.lib;

import java.awt.Rectangle;

import ss.type.HUDMODE;
import ss.type.RESPONSE;

public class HudElement {
	private static Hud elementHud;
	Rectangle elementArea;
	String elementText;
	HUDMODE elementMode;
	RESPONSE elementResponse;
	
	public HudElement(Hud hud, Rectangle area, HUDMODE mode, RESPONSE response){
		if(elementHud == null) elementHud = hud;
		this.elementArea = area;
		this.elementMode = mode;
		this.elementResponse = response;
	}
	
	public boolean wasClicked(int x, int y){
		if(x >= elementArea.x && x <= elementArea.x + elementArea.width &&
				y >= elementArea.y && y <= elementArea.y + elementArea.y + elementArea.height) return true;
		return false;
	}
	
	public HUDMODE getElementMode(){
		return elementMode;
	}
	
	public RESPONSE getElementResponse(){
		return elementResponse;
	}
	
	public Rectangle getElementArea(){
		return elementArea;
	}
	
	public int getX(){
		return elementArea.x;
	}
	
	public int getY(){
		return elementArea.y;
	}
	
	public int getHeight(){
		return elementArea.height;
	}
	
	public int getWidth(){
		return elementArea.width;
	}
	
	public void setElementResponse(RESPONSE response){
		elementResponse = response;
	}
	
}
