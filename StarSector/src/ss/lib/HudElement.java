package ss.lib;

import java.awt.Color;
import java.awt.Rectangle;

import ss.type.ELEMENT;
import ss.type.HUDMODE;
import ss.type.RESPONSE;

public class HudElement {
	private static Hud elementHud;				//	The hud element manager
	private boolean isBlinking = false;			//	Is this element blinking?
	private int blinkRate = 0;					//	For managing the blink rate
	private Rectangle elementArea;				//	This element's area
	private ELEMENT	element;					//	The element information
	
	@Deprecated
	public HudElement(Hud hud, Rectangle area, HUDMODE mode, RESPONSE response){
		if(elementHud == null) elementHud = hud;
		this.elementArea = area;
//		this.elementMode = mode;
//		this.elementResponse = response;
//		this.elementColor = Color.DARK_GRAY;
//		this.elementBlinkColor = Color.CYAN;
	}
	
	public HudElement(Hud hud, ELEMENT e){
		if(elementHud == null) elementHud = hud;
		elementArea = new Rectangle(e.getX(), e.getY(), e.getW(), e.getH());
		element = e;
	}
	
	public boolean wasClicked(int x, int y){
		if(x >= elementArea.x && x <= elementArea.x + elementArea.width &&
				y >= elementArea.y && y <= elementArea.y + elementArea.y + elementArea.height) return true;
		return false;
	}
	
	public Color getColor(){
		if(isBlinking){
			if(blinkRate < 1200){
				blinkRate++;
				return element.getColorPrimary();
//				return elementColor;
			}
			else{
				blinkRate++;
				if(blinkRate > 2400) blinkRate = 0;
				return element.getColorSecondary();
//				return elementBlinkColor;
			}
		}
		return element.getColorPrimary();
//		return elementColor;
	}

	public Color getColorP(){
		return element.getColorPrimary();
	}
	
	public Color getColorS(){
		return element.getColorSecondary();
	}
	
	public HUDMODE getElementMode(){
		return element.getMode();
//		return elementMode;
	}
	
	public RESPONSE getElementResponse(){
		return element.getResponse();
//		return elementResponse;
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
		element.setResponse(response);
//		elementResponse = response;
	}
	
}
