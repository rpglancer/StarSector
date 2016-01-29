package ss.lib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import ss.StarSector;
import ss.type.ALIGNH;
import ss.type.ALIGNV;
import ss.type.HUDMODE;
import ss.type.RESPONSE;

public class Hud implements MouseMotionListener, MouseListener {
	
	private Entity selectedEntity;
	private final int hudButtonWidth = 32;
	private final int hudButtonHeight = 16;
	
	private final int keypadWidth = 128;
	private final int keypadHeight = 160;
	private final int keypadButtonSize = 32;
	private final int keypadSpacing = 8;
	
	private boolean hudPerspective = true;				// true = x/y, false = x/z
	private HUDMODE hudMode = HUDMODE.OVERVIEW;
	
	private Vector<HudElement> hudElements = new Vector<HudElement>();
	private Vector<HudElement> keypadElements = new Vector<HudElement>();
	
	HudElement xySwap = new HudElement(this, new Rectangle(StarSector.WIDTH - hudButtonWidth, 0, hudButtonWidth, hudButtonHeight), HUDMODE.OVERVIEW, RESPONSE.XY);
	HudElement utcTime = new HudElement(this, new Rectangle(0,0,80,16),HUDMODE.OVERVIEW, RESPONSE.NULL);
	
	HudElement kpOutline = new HudElement(this, new Rectangle(StarSector.WIDTH / 2 - keypadWidth / 2, StarSector.HEIGHT / 2 - keypadHeight / 2, keypadWidth, keypadHeight), HUDMODE.INPUT, RESPONSE.NULL);
	HudElement kpOne = new HudElement(this, new Rectangle(kpOutline.getX() + keypadSpacing, kpOutline.getY() + keypadSpacing, keypadButtonSize, keypadButtonSize), HUDMODE.INPUT, RESPONSE.ONE);
	HudElement kpTwo = new HudElement(this, new Rectangle(kpOne.getX() + kpOne.getWidth() + keypadSpacing, kpOutline.getY() + keypadSpacing, keypadButtonSize, keypadButtonSize), HUDMODE.INPUT, RESPONSE.TWO);
	HudElement kpThree = new HudElement(this, new Rectangle(kpTwo.getX() + kpTwo.getWidth() + keypadSpacing, kpOutline.getY() + keypadSpacing, keypadButtonSize, keypadButtonSize), HUDMODE.INPUT, RESPONSE.THREE);
	
	
	public Hud(){
		hudElements.addElement(utcTime);
		hudElements.addElement(xySwap);
		
		hudElements.addElement(kpOutline);
		hudElements.addElement(kpOne);
		hudElements.addElement(kpTwo);
		hudElements.addElement(kpThree);
	}
	
	// Not needed given the static nature of Tracon
	public void AddMobile(Mobile m){
		Tracon.addMobile(m);
	}
	
	// Not needed given the static nature of Tracon
	public void AddStatic(Static s){
		Tracon.addStatic(s);
	}
	
	public void tick(){
		Tracon.tick();
	}
	
	// Render Base UI
	// Render Tracon Objects
	// Render Contextual UI (if applicable)
	
	public void Render(Graphics G){
		Tracon.Render(G, hudPerspective);
		renderBaseHud(G);
		if(hudMode == HUDMODE.OPS)renderOps(G);
		if(hudMode == HUDMODE.INPUT)renderInput(G);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		switch(arg0.getButton()){
		case MouseEvent.BUTTON1:
			switch(hudMode){
			case INPUT:
				break;
			case OPS:
				break;
			case OVERVIEW:
				if(arg0.getY() <= 16){
					for(int i = 0; i < hudElements.size(); i++){
						if(hudElements.get(i).wasClicked(arg0.getX(), arg0.getY())){
							hudProcess(hudElements.get(i));
							break;
						}
					}
				}
				else selectedEntity = Tracon.selectEntityAt(arg0);
				break;
			default:
				break;
			}
			break;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void hudProcess(HudElement element){
		switch(element.getElementResponse()){
		case APRCLR:
			break;
		case CANCEL:
			break;
		case CONFIRM:
			break;
		case DCT:
			break;
		case EIGHT:
			break;
		case FIVE:
			break;
		case FOUR:
			break;
		case HOLD:
			break;
		case MARK:
			break;
		case MVS:
			break;
		case NINE:
			break;
		case NULL:
			break;
		case ONE:
			break;
		case SEVEN:
			break;
		case SIX:
			break;
		case THREE:
			break;
		case TWO:
			break;
		case XY:
			hudPerspective = false;
			element.setElementResponse(RESPONSE.XZ);
			break;
		case XZ:
			hudPerspective = true;
			element.setElementResponse(RESPONSE.XY);
			break;
		case ZER:
			break;
		default:
			break;
		
		}
	}
	
	private void renderBaseHud(Graphics G){
		Graphics2D G2D = (Graphics2D)G;
		Color PrevC = G2D.getColor();
		G2D.setColor(Color.DARK_GRAY);
		G2D.drawRect(0, 0, StarSector.WIDTH, StarSector.HEIGHT);
		G2D.setColor(Color.BLACK);
		G2D.fillRect(0, 0, StarSector.WIDTH, 16);
		G2D.setColor(Color.DARK_GRAY);
		G2D.drawRect(0, 0, StarSector.WIDTH, 16);
		for(int i = 0; i < hudElements.size(); i++){
			G2D.draw(hudElements.get(i).getElementArea());
			Text.BoxText(G, Fonts.RadarText, hudElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, hudElements.get(i).getElementResponse().getText());
		}
		G2D.setColor(PrevC);
	}
	
	private void renderOps(Graphics G){
		
	}
	
	private void renderInput(Graphics G){
		
	}
	
}
