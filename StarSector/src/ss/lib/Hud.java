package ss.lib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import ss.StarSector;
import ss.type.ALIGNH;
import ss.type.ALIGNV;
import ss.type.ELEMENT;
import ss.type.HUDMODE;
import ss.type.RESPONSE;

/**
 * The Hud class manages all the input for the game as well as<br>
 * rendering the game {@link Entity} as pulled from {@link Tracon}.
 * @author Matt Bangert
 *
 */
public class Hud implements MouseMotionListener, MouseListener {
	
	private Entity selectedEntity;
	
	private boolean hudPerspective = true;				// true = x/y, false = x/z
	private HUDMODE hudMode = HUDMODE.OVERVIEW;
	
	private SimpleDateFormat dateF = new SimpleDateFormat("HH:mm:ss zzz");
	
	private Vector<HudElement> ovwElements = new Vector<HudElement>();
	private Vector<HudElement> inputElements = new Vector<HudElement>();
	private Vector<HudElement> opsElements = new Vector<HudElement>();
	
	public Hud(){
		for(ELEMENT e : ELEMENT.values()){
			HudElement temp = new HudElement(this, e);
			switch(e.getMode()){
			case INPUT:
				inputElements.addElement(temp);
				break;
			case OPS:
				opsElements.addElement(temp);
				break;
			case OVERVIEW:
				ovwElements.addElement(temp);
				break;
			default:
				break;
			}
		}
	}
	
	public void tick(){
		Tracon.tick();
	}
	
	// Render Tracon Objects
	// Render Base UI
	// Render Contextual UI (if applicable)
	public void Render(Graphics G){
		Tracon.Render(G, hudPerspective);
		renderBaseHud(G);
		if(hudMode == HUDMODE.OPS)renderOps(G);
		if(hudMode == HUDMODE.INPUT)renderInput(G);
		renderInput(G);
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
					for(int i = 0; i < ovwElements.size(); i++){
						if(ovwElements.get(i).wasClicked(arg0.getX(), arg0.getY())){
							hudProcess(ovwElements.get(i));
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
		case CNCL:
			break;
		case XMIT:
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
			System.out.println("WARN: NULL PROCESSED");
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
		Date td = new Date();
		Graphics2D G2D = (Graphics2D)G;
		Color PrevC = G2D.getColor();
		G2D.setColor(Color.DARK_GRAY);
		G2D.drawRect(0, 0, StarSector.WIDTH, StarSector.HEIGHT);
		G2D.setColor(Color.BLACK);
		G2D.fillRect(0, 0, StarSector.WIDTH, 16);
		G2D.setColor(Color.DARK_GRAY);
		G2D.drawRect(0, 0, StarSector.WIDTH, 16);
		for(int i = 0; i < ovwElements.size(); i++){
			G2D.setColor(ovwElements.get(i).getColor());
			G2D.draw(ovwElements.get(i).getElementArea());
			G2D.setColor(ovwElements.get(i).getElementResponse().getColorPri());
			Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, ovwElements.get(i).getElementResponse().getText());
			if(ovwElements.get(i).getElementResponse() == RESPONSE.CLOCK)
				Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, dateF.format(td));
			}
		G2D.setColor(PrevC);
	}
	
	private void renderOps(Graphics G){
	}
	
	private void renderInput(Graphics G){
		Graphics2D G2D = (Graphics2D)G;
		Color PrevC = G2D.getColor();
		for(int i = 0; i < inputElements.size(); i++){
			G2D.setColor(inputElements.get(i).getColorP());
			G2D.fill(inputElements.get(i).getElementArea());
			G2D.setColor(inputElements.get(i).getColorS());
			G2D.draw(inputElements.get(i).getElementArea());
			G2D.setColor(inputElements.get(i).getElementResponse().getColorPri());
			Text.BoxText(G, Fonts.RadarText, inputElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, inputElements.get(i).getElementResponse().getText());
		}
		G2D.setColor(PrevC);
	}
}
