package ss.engine;

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

import ss.entity.Entity;
import ss.entity.Mobile;
import ss.lib.Draw;
import ss.lib.Fonts;
import ss.lib.HudElement;
import ss.lib.Text;
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
	
	private Mobile selectedMobile;
	
	private static boolean hudPerspective = true;				// true = x/y, false = x/z
	private static boolean drawRuler = false;
	private static int hudTimeProjection = 1;					// The number of minutes calculated for course projection.
	private HUDMODE hudMode = HUDMODE.MENU;
	
	private Xmit xmit;
	
	private SimpleDateFormat dateF = new SimpleDateFormat("HH:mm:ss zzz");
	
	/**
	 * Vector containing all of the HudElements that comprise the Hud Overview
	 */
	private Vector<HudElement> ovwElements = new Vector<HudElement>();
	/**
	 * Vector containing all of the HudElements that comprise the numeric Hud input display.
	 */
	private Vector<HudElement> inputElements = new Vector<HudElement>();
	private Vector<HudElement> listElements = new Vector<HudElement>();
	private Vector<HudElement> menuElements = new Vector<HudElement>();
	/**
	 * Vector containing all of the HudElements that comprise the Hud operations display.
	 */
	private Vector<HudElement> opsElements = new Vector<HudElement>();
	private Vector<HudElement> staElements = new Vector<HudElement>();
	private Vector<Integer> rulerElementsY = new Vector<Integer>();
	private Vector<Integer> rulerElementsX = new Vector<Integer>();
	
	//	Stores 
	private ELEMENT active;
	
	public Hud(){
		for(ELEMENT e : ELEMENT.values()){
			HudElement temp = new HudElement(e);
			switch(e.getMode()){
			case LIST:
				listElements.addElement(temp);
				break;
			case INPUT:
				inputElements.addElement(temp);
				break;
			case MENU:
				menuElements.addElement(temp);
				break;
			case OPS:
				opsElements.addElement(temp);
				break;
			case OVERVIEW:
				ovwElements.addElement(temp);
				break;
			case STA:
				staElements.addElement(temp);
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
	
	private void modeSelect(HUDMODE mode){
		hudMode = mode;
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
			case LIST:
				for(int i = 1; i < listElements.size(); i++){
					if(listElements.get(i).wasClicked(arg0.getX(), arg0.getY())){
						hudProcess(listElements.get(i));
					}
				}
				break;
			case MENU:
				for(int i = 0; i < menuElements.size(); i++){
					if(menuElements.get(i).wasClicked(arg0.getX(), arg0.getY())){
						hudProcess(menuElements.get(i));
						break;
					}
				}
				break;
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
		switch(hudMode){
		case LIST:
			Tracon.Render(G, hudPerspective);
			renderBaseHud(G);
			renderOps(G);
			renderList(G);
			break;
		case INPUT:
			Tracon.Render(G, hudPerspective);
			renderBaseHud(G);
			renderOps(G);
			renderInput(G);
			break;
		case MENU:
			renderMainMenu(G);
			break;
		case OPS:
			Tracon.Render(G, hudPerspective);
			renderBaseHud(G);
			renderOps(G);
			break;
		case OVERVIEW:
			Tracon.Render(G, hudPerspective);
			renderBaseHud(G);
			renderChatter(G);
			break;
		default:
			break;
		}
	}
	
	public void tick(){
		Tracon.tick();
	}
	
	private void hudProcess(HudElement element){
		switch(element.getElementResponse()){
		case ACPT:
			xmit.setWaypoint();
			modeSelect(HUDMODE.OPS);
			break;
		case APRCLR:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_APR.getIndex()))
				xmit.setOpsActive(ELEMENT.HUD_OPS_APR.getIndex(), !xmit.getOpsActive(ELEMENT.HUD_OPS_APR.getIndex()));
			break;
		case CNCL:
			if(hudMode == HUDMODE.INPUT){
				if(inputElements.get(ELEMENT.HUD_INP_CAN.getIndex()).isActive()){
//					xmit.input(-2);
					xmit.input(active, -2);
					modeSelect(HUDMODE.OPS);
				}
			}
			else if(hudMode == HUDMODE.LIST){
				modeSelect(HUDMODE.OPS);
			}
			break;
		case CONT:
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
			if(xmit.getOpsActive(ELEMENT.HUD_OPS_DCT.getIndex())){
				xmit.setOpsActive(ELEMENT.HUD_OPS_DCT.getIndex(), false);
				xmit.clrWaypt();
			}
			else
				hudMode = HUDMODE.LIST;
//			if(selectedMobile.getOpsActive(ELEMENT.HUD_OPS_DCT.getIndex()))
//				xmit.setOpsActive(ELEMENT.HUD_OPS_DCT.getIndex(), false);
//			else
//				hudMode = HUDMODE.LIST;
			break;
		case EXIT:
			StarSector.exit();
			break;
		case HDG:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_HDG.getIndex())){
				active = ELEMENT.HUD_OPS_HDG;
				xmit.input(active, -2);
//				xmit.setInputDest(ELEMENT.HUD_OPS_HDG);
				refreshInputElements();
				hudMode = HUDMODE.INPUT;
			}
			break;
		case HOLD:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_HLD.getIndex()))
				xmit.setOpsActive(ELEMENT.HUD_OPS_HLD.getIndex(), !xmit.getOpsActive(ELEMENT.HUD_OPS_HLD.getIndex()));
			break;
		case MARK:
			if(inputElements.get(ELEMENT.HUD_INP_MRK.getIndex()).isActive()){
				xmit.input(active, -1);
//				xmit.input(-1);
				refreshInputElements();
			}
			break;
		case MVS:
			break;
		case NEWG:
			if(Tracon.loadSector("Terra Sector")){
				hudMode = HUDMODE.OVERVIEW;
				StarSector.setPause(false);
			}
			break;
		case NEXT:
			xmit.nextWaypt();
			break;
		case NULL:
			System.out.println("WARN: NULL PROCESSED");
			break;
		case PREV:
			xmit.prevWaypt();
			break;
		case SPD:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_SPD.getIndex())){
				active = ELEMENT.HUD_OPS_SPD;
//				xmit.setInputDest(ELEMENT.HUD_OPS_SPD);
				xmit.input(active, -2);
				refreshInputElements();
				hudMode = HUDMODE.INPUT;
			}
			break;
		case ZER:
			if(inputElements.get(ELEMENT.HUD_INP_ZER.getIndex()).isActive()){
//				xmit.input(0);
				xmit.input(active, 0);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case ONE:
			if(inputElements.get(ELEMENT.HUD_INP_ONE.getIndex()).isActive()){
//				xmit.input(1);
				xmit.input(active, 1);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case TWO:
			if(inputElements.get(ELEMENT.HUD_INP_TWO.getIndex()).isActive()){
//				xmit.input(2);
				xmit.input(active, 2);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case THREE:
			if(inputElements.get(ELEMENT.HUD_INP_THR.getIndex()).isActive()){
//				xmit.input(3);
				xmit.input(active, 3);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case FOUR:
			if(inputElements.get(ELEMENT.HUD_INP_FOU.getIndex()).isActive()){
//				xmit.input(4);
				xmit.input(active, 4);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case FIVE:
			if(inputElements.get(ELEMENT.HUD_INP_FIV.getIndex()).isActive()){
//				xmit.input(5);
				xmit.input(active, 5);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case SIX:
			if(inputElements.get(ELEMENT.HUD_INP_SIX.getIndex()).isActive()){
//				xmit.input(6);
				xmit.input(active, 6);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case SEVEN:
			if(inputElements.get(ELEMENT.HUD_INP_SEV.getIndex()).isActive()){
//				xmit.input(7);
				xmit.input(active, 7);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case EIGHT:
			if(inputElements.get(ELEMENT.HUD_INP_EIG.getIndex()).isActive()){
//				xmit.input(8);
				xmit.input(active, 8);
				if(xmit.inputComplete())
					hudMode = HUDMODE.OPS;
				else
					refreshInputElements();
			}
			break;
		case NINE:
			if(inputElements.get(ELEMENT.HUD_INP_NIN.getIndex()).isActive()){
//				xmit.input(9);
				xmit.input(active, 9);
				if(xmit.inputComplete())
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
		case XMIT:
			if(selectedMobile.getOps(ELEMENT.HUD_OPS_XMT.getIndex())){
//				selectedMobile.call(xmit);
				xmit.transmit();
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
	
	/**
	 * Updates the availability of {@link Hud#inputElements} based upon the status of the current pending transmission.
	 */
	private void refreshInputElements(){
		for(int i = 1; i < inputElements.size(); i++){
			inputElements.get(i).setActive(xmit.getInputAvail(i));
		}
	}
	
	private void renderChatter(Graphics G){
		if(Tracon.currentChatter() != null)
			Tracon.currentChatter().render(G);
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
			Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), ovwElements.get(i).getElementResponse().getText());
			if(ovwElements.get(i).getElementResponse() == RESPONSE.CLOCK)
				Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), dateF.format(td));
			if(ovwElements.get(i).getElementResponse() == RESPONSE.ENTITY && selectedMobile != null)
				Text.BoxText(G, Fonts.RadarText, ovwElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), selectedMobile.getSerial());
		}
		G2D.setColor(PrevC);
	}

	private void renderInput(Graphics G){
		Color PrevC = G.getColor();
		Draw.shape(G, inputElements.get(0).getElementArea(), 1, Color.darkGray, Color.black);
		for(int i = 1; i < inputElements.size(); i++){
			if(inputElements.get(i).isActive()){
				Draw.shape(G, inputElements.get(i).getElementArea(), 1, Color.green, Color.darkGray);
				G.setColor(Color.green);
				Text.BoxText(G, Fonts.HudText, inputElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), inputElements.get(i).getElementResponse().getText());
			}
			else{
				Draw.shape(G, inputElements.get(i).getElementArea(), 1, Color.red, Color.darkGray);
				G.setColor(Color.black);
				Text.BoxText(G, Fonts.HudText, inputElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), inputElements.get(i).getElementResponse().getText());
			}
		}
		G.setColor(PrevC);
	}
	
	private void renderList(Graphics G){
		Color PrevC = G.getColor();
		Draw.shape(G, listElements.get(0).getElementArea(), 1, Color.darkGray, Color.black);
		for(int i = 1; i < listElements.size(); i++){
			Draw.shape(G, listElements.get(i).getElementArea(), 1, Color.green, Color.darkGray);
			G.setColor(Color.green);
			Text.BoxText(G, Fonts.HudText, listElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), listElements.get(i).getElementResponse().getText());
		}
		G.setColor(PrevC);
	}
	
	private void renderMainMenu(Graphics G){
		Color prevC = G.getColor();
		Font prevF = G.getFont();
		Graphics2D G2D = (Graphics2D)G;
		for(int i = 0; i < menuElements.size(); i++){
			G2D.setColor(Color.darkGray);
			G2D.fill(menuElements.get(i).getElementArea());
			G2D.setColor(Color.green);
			G2D.draw(menuElements.get(i).getElementArea());
			Text.BoxText(G, Fonts.RadarText, menuElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), menuElements.get(i).getElementResponse().getText());
		}
		G.setColor(prevC);
		G.setFont(prevF);
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
				if(xmit.getOpsActive(i)){
					// Selected Operation is Available and Active
					Draw.shape(G, opsElements.get(i).getElementArea(), 1, Color.green, Color.green);
					G2D.setColor(Color.white);
					Text.BoxText(G, Fonts.RadarText, opsElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), opsElements.get(i).getElementResponse().getText());
				}
				else{
					// Selected Operation is Available and Inactive
					Draw.shape(G, opsElements.get(i).getElementArea(), 1, opsElements.get(i).getColorS(), opsElements.get(i).getColorP());
					G2D.setColor(Color.green);
					Text.BoxText(G, Fonts.HudText, opsElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), opsElements.get(i).getElementResponse().getText());
				}
			}
			else{
				// Selected Operation is Unavailable
				Draw.shape(G, opsElements.get(i).getElementArea(), 1, Color.red, Color.darkGray);
				G2D.setColor(Color.black);
				Text.BoxText(G, Fonts.HudText, opsElements.get(i).getElementArea(), Text.alignCenter(), Text.alignMiddle(), opsElements.get(i).getElementResponse().getText());
			}
		}
		renderStatus(G);
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
		if(selectedMobile != null){
			G.setColor(Color.cyan);
			if(hudPerspective)
				G.drawLine(16, (int)selectedMobile.getLoc().GetY(), 20, (int)selectedMobile.getLoc().GetY());
			else
				G.drawLine(16, (int)selectedMobile.getLoc().GetZ(), 20, (int)selectedMobile.getLoc().GetZ());
			G.drawLine((int)selectedMobile.getLoc().GetX(), StarSector.HEIGHT - 16, (int)selectedMobile.getLoc().GetX(), StarSector.HEIGHT - 20);
		}
		G.setColor(prevC);
		G.setFont(prevF);
	}
	
	private void renderStatus(Graphics G){
		selectedMobile.renderStatus(G);
	}
	
	private void toggleRuler(){
		drawRuler = !drawRuler;
	}
	
}
