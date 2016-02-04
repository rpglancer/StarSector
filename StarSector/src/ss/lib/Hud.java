package ss.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
	
	@Deprecated
	private Entity selectedEntity;

	private Mobile selectedMobile;
	
	private static boolean hudPerspective = true;				// true = x/y, false = x/z
	private static boolean drawRuler = false;
	private static int hudTimeProjection = 1;					// The number of minutes calculated for course projection.
	private HUDMODE hudMode = HUDMODE.OVERVIEW;
	
	private Xmit xmit;
	
	private SimpleDateFormat dateF = new SimpleDateFormat("HH:mm:ss zzz");
	
	private Vector<HudElement> ovwElements = new Vector<HudElement>();
	private Vector<HudElement> inputElements = new Vector<HudElement>();
	private Vector<HudElement> opsElements = new Vector<HudElement>();
	private Vector<Integer> rulerElementsY = new Vector<Integer>();
	private Vector<Integer> rulerElementsX = new Vector<Integer>();
	
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
		initRuler();
	}
		
	public static boolean getP(){
		return hudPerspective;
	}

	public static int getTimeProject(){
		return hudTimeProjection;
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
				for(int i = 1; i < inputElements.size(); i++){
					if(inputElements.get(i).wasClicked(arg0.getX(), arg0.getY())){
						hudProcess(inputElements.get(i));
						break;
					}
				}
				break;
			case OPS:
				for(int i = 1; i < opsElements.size(); i++){
					if(opsElements.get(i).wasClicked(arg0.getX(), arg0.getY())){
						hudProcess(opsElements.get(i));
						break;
					}
				}
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
				else selectedMobile = Tracon.selectMobileAt(arg0);
				if(selectedMobile != null) ovwElements.get(ELEMENT.HUD_OVW_OPS.getIndex()).setActive(true);
				else ovwElements.get(ELEMENT.HUD_OVW_OPS.getIndex()).setActive(false);
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
	
	// Render Tracon Objects
	// Render Base UI
	// Render Contextual UI (if applicable)
	public void Render(Graphics G){
		Tracon.Render(G, hudPerspective);
		renderBaseHud(G);
		if(hudMode == HUDMODE.OPS)renderOps(G);
		if(hudMode == HUDMODE.INPUT){
			renderOps(G);
			renderInput(G);
		}
	}
	
	public void tick(){
		Tracon.tick();
	}
	
	private void hudProcess(HudElement element){
		switch(element.getElementResponse()){
		case APRCLR:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_APR.getIndex()))
				xmit.setApproach(!xmit.getApproach());
			break;
		case CNCL:
			if(inputElements.get(ELEMENT.HUD_INP_CAN.getIndex()).isActive()){
				xmit.initHdg();
				hudMode = HUDMODE.OPS;
			}
			break;
		case CTCT:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_CON.getIndex())){
				selectedMobile.contact();
			}
			break;
		case DISR:
			hudMode = HUDMODE.OVERVIEW;
			break;
		case DCT:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_DCT.getIndex()))
				xmit.setDirect(!xmit.getDirect());
			break;
		case EIGHT:
			break;
		case FIVE:
			break;
		case FOUR:
			break;
		case HDG:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_HDG.getIndex())){
				xmit.initHdg();
				refreshInputElements();
				hudMode = HUDMODE.INPUT;
			}
			break;
		case HOLD:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_HLD.getIndex()))
				xmit.setHold(!xmit.getHold());
			break;
		case MARK:
			if(inputElements.get(ELEMENT.HUD_INP_MRK.getIndex()).isActive()){
				xmit.setHeading(-1);
				refreshInputElements();
			}
			break;
		case MVS:
			break;
		case NINE:
			break;
		case NULL:
			System.out.println("WARN: NULL PROCESSED");
			break;
		case ONE:
			if(inputElements.get(ELEMENT.HUD_INP_ONE.getIndex()).isActive()){
				xmit.setHeading(1);
				if(xmit.getHdgStatus() == 0)
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case OPS:
			if(hudMode == HUDMODE.OVERVIEW && ovwElements.get(ELEMENT.HUD_OVW_OPS.getIndex()).isActive()){
				for(int i = 1; i < opsElements.size(); i++){
					opsElements.get(i).setActive(selectedMobile.getOps(i));
				}
				xmit = new Xmit(selectedMobile);
				hudMode = HUDMODE.OPS;
			}
			break;
		case RULER:
			element.toggle();
			toggleRuler();
			break;
		case SEVEN:
			break;
		case SIX:
			break;
		case THREE:
			break;
		case TWO:
			break;
		case XMIT:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_XMT.getIndex())){
				selectedMobile.call(xmit);
				hudMode = HUDMODE.OVERVIEW;
			}	
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
	
	private void initRuler(){
		for(int i = 0; i < StarSector.HEIGHT; i++){
			if(i % (10 * StarSector.PPKM) == 0){
				rulerElementsY.add(i);
			}
		}
		for(int i = 0; i < StarSector.WIDTH; i++){
			if(i % (10 * StarSector.PPKM) == 0){
				rulerElementsX.add(i);
			}
		}
	}
	
	private void refreshInputElements(){
		for(int i = 1; i < inputElements.size(); i++){
			inputElements.get(i).setActive(xmit.getInputAvail(i));
		}
	}
	
	private void renderBaseHud(Graphics G){
		Date td = new Date();			// << should probably be moved somewhere that it's not being updated hundreds of times per second.
		Graphics2D G2D = (Graphics2D)G;
		Color PrevC = G2D.getColor();
		G2D.setColor(Color.DARK_GRAY);
		G2D.drawRect(0, 0, StarSector.WIDTH, StarSector.HEIGHT);
		G2D.setColor(Color.BLACK);
		G2D.fillRect(0, 0, StarSector.WIDTH, 16);
		G2D.setColor(Color.DARK_GRAY);
		G2D.drawRect(0, 0, StarSector.WIDTH, 16);
		if(drawRuler)renderRuler(G);
		for(int i = 0; i < ovwElements.size(); i++){
			G2D.setColor(Color.black);
			G2D.fill(ovwElements.get(i).getElementArea());
			G2D.setColor(ovwElements.get(i).getColor());
			G2D.draw(ovwElements.get(i).getElementArea());
			G2D.setColor(ovwElements.get(i).getColorResponse());
			Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, ovwElements.get(i).getElementResponse().getText());
			if(ovwElements.get(i).getElementResponse() == RESPONSE.CLOCK)
				Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, dateF.format(td));
			if(ovwElements.get(i).getElementResponse() == RESPONSE.ENTITY && selectedMobile != null)
				Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, selectedMobile.getName());	
		}
		G2D.setColor(PrevC);
	}

	private void renderInput(Graphics G){
		Graphics2D G2D = (Graphics2D)G;
		Color PrevC = G2D.getColor();
		G2D.setColor(Color.black);
		G2D.fill(inputElements.get(0).getElementArea());
		G2D.setColor(Color.darkGray);
		G2D.draw(inputElements.get(0).getElementArea());
		for(int i = 1; i < inputElements.size(); i++){
			if(inputElements.get(i).isActive()){
				G2D.setColor(Color.darkGray);
				G2D.fill(inputElements.get(i).getElementArea());
				G2D.setColor(Color.green);
				G2D.draw(inputElements.get(i).getElementArea());
				Text.BoxText(G, Fonts.HudText, inputElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, inputElements.get(i).getElementResponse().getText());
			}
			else{
				G2D.setColor(Color.darkGray);
				G2D.fill(inputElements.get(i).getElementArea());
				G2D.setColor(Color.red);
				G2D.draw(inputElements.get(i).getElementArea());
				G2D.setColor(Color.black);
				Text.BoxText(G, Fonts.HudText, inputElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, inputElements.get(i).getElementResponse().getText());
			}
		}
		G2D.setColor(PrevC);
	}
	
	private void renderOps(Graphics G){
		Color PrevC = G.getColor();
		Font PrevF = G.getFont();
		Graphics2D G2D = (Graphics2D)G;
		G2D.setColor(opsElements.get(0).getColorP());
		G2D.fill(opsElements.get(0).getElementArea());
		G2D.setColor(opsElements.get(0).getColorS());
		G2D.draw(opsElements.get(0).getElementArea());
		for(int i = 1; i < opsElements.size(); i++){
			if(selectedMobile.getOps(i)){
//				if(selectedMobile.getOpsActive(i)){
				if(xmit.getOpsActive(i)){
					// Selected Operation is Available and Active
					G2D.setColor(Color.green);
					G2D.fill(opsElements.get(i).getElementArea());
					G2D.setColor(Color.green);
					G2D.draw(opsElements.get(i).getElementArea());
					G2D.setColor(Color.white);
					Text.BoxText(G, Fonts.RadarText, opsElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, opsElements.get(i).getElementResponse().getText());
				}
				else{
					// Selected Operation is Available and Inactive
					G2D.setColor(opsElements.get(i).getColorP());
					G2D.fill(opsElements.get(i).getElementArea());
					G2D.setColor(opsElements.get(i).getColorS());
					G2D.draw(opsElements.get(i).getElementArea());
					G2D.setColor(Color.green);
					Text.BoxText(G, Fonts.HudText, opsElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, opsElements.get(i).getElementResponse().getText());
				}
			}
			else{
				// Selected Operation is Unavailable
				G2D.setColor(Color.darkGray);
				G2D.fill(opsElements.get(i).getElementArea());
				G2D.setColor(Color.red);
				G2D.draw(opsElements.get(i).getElementArea());
				G2D.setColor(Color.black);
				Text.BoxText(G, Fonts.HudText, opsElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, opsElements.get(i).getElementResponse().getText());
			}
//			G2D.setColor(opsElements.get(i).getColorP());
//			G2D.fill(opsElements.get(i).getElementArea());
//			G2D.setColor(opsElements.get(i).getColorS());
//			G2D.draw(opsElements.get(i).getElementArea());
//			G2D.setColor(opsElements.get(i).getColorResponse());
//			Text.BoxText(G, Fonts.RadarText, opsElements.get(i).getElementArea(), ALIGNH.CENTER, ALIGNV.MIDDLE, opsElements.get(i).getElementResponse().getText());
		}
		G2D.setFont(PrevF);
		G2D.setColor(PrevC);
	}
	
	private void renderRuler(Graphics G){
		Color prevC = G.getColor();
		Font prevF = G.getFont();
		G.setColor(Color.darkGray);
		G.setFont(Fonts.RadarRuler);
		FontMetrics fm = G.getFontMetrics();
		G.drawLine(16, 0, 16, StarSector.HEIGHT);
		G.drawLine(0, StarSector.HEIGHT - 16, StarSector.WIDTH, StarSector.HEIGHT - 16);
		for(int i = 0; i < rulerElementsY.size(); i++){
			G.drawLine(12, rulerElementsY.get(i), 16, rulerElementsY.get(i));
			G.drawString(""+ (int)(rulerElementsY.get(i) / StarSector.PPKM), 0, rulerElementsY.get(i) + 3);
		}
		for(int i = 0; i < rulerElementsX.size(); i++){
			G.drawLine(rulerElementsX.get(i), StarSector.HEIGHT - 16, rulerElementsX.get(i), StarSector.HEIGHT - 12);
			double offset = fm.stringWidth("" + (int)(rulerElementsX.get(i) / StarSector.PPKM));
			G.drawString("" + (int)(rulerElementsX.get(i) / StarSector.PPKM), (int)(rulerElementsX.get(i) - (offset/2)), StarSector.HEIGHT - 4);
		}
		G.setColor(prevC);
		G.setFont(prevF);
	}
	
	private void toggleRuler(){
		if(drawRuler) drawRuler = false;
		else drawRuler = true;
	}
	
}
