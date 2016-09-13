package ss.lib;

import java.awt.Color;
import java.awt.Rectangle;

import ss.type.ELEMENT;
import ss.type.HUDMODE;
import ss.type.RESPONSE;

public class HudElement {
//	private static Hud elementHud;				//	The hud element manager [wtf is this for?]
	private boolean isActive = false;			//	Is this element available for interaction?
	private boolean isBlinking = false;			//	Is this element blinking?
	private int blinkRate = 0;					//	For managing the blink rate
	private Rectangle elementArea;				//	This element's area
	private ELEMENT	element;					//	The element information

@Deprecated
	public HudElement(Hud hud, ELEMENT e){
//		if(elementHud == null) elementHud = hud;	[unnecessary?]
		elementArea = new Rectangle(e.getX(), e.getY(), e.getW(), e.getH());
		element = e;
	}
	
	public HudElement(ELEMENT e){
		elementArea = new Rectangle(e.getX(), e.getY(), e.getW(), e.getH());
		element = e;
	}
	
	public boolean wasClicked(int x, int y){
		return (x >= elementArea.x && x <= elementArea.x + elementArea.width &&
				y >= elementArea.y && y <= elementArea.y + elementArea.height);
	}
	
	/**
	 * Get the color to be used for drawing the geometry of this element.
	 * @return The active color for element geometry.
	 */
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
			}
		}
		if(isActive)return element.getColorPrimary();
		else return element.getColorSecondary();
	}
	
	/**
	 * Get the color to be used for drawing the text attributes of this element.
	 * @return The active color for the element's text content.
	 */
	public Color getColorResponse(){
		if(isBlinking){
			if(blinkRate < 1200){
				blinkRate++;
				return element.getResponse().getColorPri();
			}
			else{
				blinkRate++;
				if(blinkRate > 2400) blinkRate = 0;
				return element.getResponse().getColorSec();
			}
		}
		if(isActive)return element.getResponse().getColorPri();
		else return element.getResponse().getColorSec();
	}

	public Color getColorP(){
		return element.getColorPrimary();
	}
	
	public Color getColorS(){
		return element.getColorSecondary();
	}
	
	public ELEMENT getElement(){
		return element;
	}
	
	public HUDMODE getElementMode(){
		return element.getMode();
	}
	
	public RESPONSE getElementResponse(){
		return element.getResponse();
	}
	
	public Rectangle getElementArea(){
		return elementArea;
	}
	
	public void setElementResponse(RESPONSE response){
		element.setResponse(response);
	}
	
	/**
	 * Returns the availability status of this HudElement.<br>
	 * HudElements that are available [true] will be drawn and can be clicked on the hud.<br>
	 * HudElements that are not available [false] will still be drawn but cannot be interacted with via the hud.
	 * @return The current status of this HudElement.
	 */
	public boolean isActive(){
		return isActive;
	}
	
	public void toggle(){
		isActive = !isActive;
	}
	
	/**
	 * Sets the availability status of this HudElement.<br>
	 * HudElements that are available [true] will be drawn and can be clicked on the hud.<br>
	 * HudElements that are not available [false] will still be drawn but cannot be interacted with via the hud.
	 * @param status The desired status of this HudElement.
	 */
	public void setActive(boolean status){
		isActive = status;
	}
}
