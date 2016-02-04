package ss.lib;

import ss.type.ELEMENT;

/**
 * Xmit contains the temporary data waiting to be transmitted as an order to a Mobile.
 * @author Matt Bangert
 *
 */
public class Xmit {
	private int heading;
	private int headingInit;
	private int mark;
	private int markInit;
	
	private int speedInputStatus;
	private int headingInputStatus;
	
	private boolean[] opsAct = {false, false, false, false, false, false, false, false, false};
	private boolean[] inputAvail = {false, false, false, false, false,
									false, false, false, false, false,
									false, false, false };
	private Static waypoint;
	
	/**
	 * Create a new transmission order to be broadcast to a Mobile.
	 * @param m The mobile for which the order applies.
	 */
	public Xmit(Mobile m){
		headingInit = (int)m.getHdg();
		markInit = (int)m.getMK();
		for(int i = 0; i < opsAct.length; i++){
			opsAct[i] = m.getOpsActive(i);
		}
		waypoint = m.getWaypoint();
	}
	
	public int getHeading(){
		return heading;
	}
	
	public int getMark(){
		return mark;
	}
	
	public boolean getInputAvail(int index){
		return inputAvail[index];
	}
	
	/**
	 * Determines the active OPS for being drawn on the hud.
	 * @param index The index of the active OP
	 * @return True/False whether the OP is active.
	 */
	public boolean getOpsActive(int index){
		return opsAct[index];
	}
	
	public boolean getHold(){
		return opsAct[ELEMENT.HUD_OPS_HLD.getIndex()];
	}
	
	public boolean getDirect(){
		return opsAct[ELEMENT.HUD_OPS_DCT.getIndex()];
	}
	
	public boolean getApproach(){
		return opsAct[ELEMENT.HUD_OPS_APR.getIndex()];
	}
	
	public int getHdgStatus(){
		return headingInputStatus;
	}
	
	public int getSpdStatus(){
		return speedInputStatus;
	}
	
	public Static getWaypoint(){
		return waypoint;
	}
	
	public void initHdg(){
		headingInputStatus = 100;
		heading = headingInit;
		mark = markInit;
		for(int i = 0; i < inputAvail.length; i++){
			inputAvail[i] = false;
		}
		for(int i = ELEMENT.HUD_INP_ONE.getIndex(); i < ELEMENT.HUD_INP_FOU.getIndex(); i++){
			inputAvail[i] = true;
		}
		inputAvail[ELEMENT.HUD_INP_ZER.getIndex()] = true;
		inputAvail[ELEMENT.HUD_INP_CAN.getIndex()] = true;
		inputAvail[ELEMENT.HUD_INP_MRK.getIndex()] = true;
	}
	
	public void initSpd(){
		
	}
	
	public void setHeading(int heading){
		if(heading == -1){
			if(headingInputStatus == 10) this.heading /= 100;
			else if(headingInputStatus == 1) this.heading /= 10;
			else this.heading /= 1;
			headingInputStatus = -100;
			updateHdgInpStatus();
			return;
		}
		switch(headingInputStatus){
		case 100:
			this.heading = heading * 100;
			headingInputStatus = 10;
			break;
		case 10:
			this.heading += (heading * 10);
			headingInputStatus = 1;
			break;
		case 1:
			this.heading += heading;
			headingInputStatus = -100;
			break;
		case - 100:
			this.mark = heading * 100;
			headingInputStatus = -10;
			break;
		case -10:
			this.mark += (heading * 10);
			headingInputStatus = -1;
			break;
		case -1:
			this.mark += heading;
			headingInputStatus = 0;
			break;
		}
		System.out.println("Current Hdg Entry: " + this.heading + "." + this.mark);
		updateHdgInpStatus();
	}
	
	public void setMark(int mark){
		this.mark = mark;
	}
	
	public void setHold(boolean status){
		opsAct[ELEMENT.HUD_OPS_HLD.getIndex()] = status;
	}
	
	public void setDirect(boolean status){
		opsAct[ELEMENT.HUD_OPS_DCT.getIndex()] = status;
	}
	
	public void setApproach(boolean status){
		opsAct[ELEMENT.HUD_OPS_APR.getIndex()] = status;
	}
	
	public void setWaypoint(Static waypoint){
		this.waypoint = waypoint;
	}
	
	/**
	 * Updates the available options for heading input to be displayed on the hud.
	 */
	private void updateHdgInpStatus(){
		switch(headingInputStatus){
		case 10:
			inputAvail[ELEMENT.HUD_INP_FOU.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_FIV.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_SIX.getIndex()] = true;
			if(heading < 300){
				inputAvail[ELEMENT.HUD_INP_SEV.getIndex()] = true;
				inputAvail[ELEMENT.HUD_INP_EIG.getIndex()] = true;
				inputAvail[ELEMENT.HUD_INP_NIN.getIndex()] = true;
			}
			break;
		case 1:
			break;
		case -100:
			for(int i = 1; i < inputAvail.length; i++){
				inputAvail[i] = false;
			}
			inputAvail[ELEMENT.HUD_INP_ONE.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_ZER.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_CAN.getIndex()] = true;
			break;
		case -10:
			inputAvail[ELEMENT.HUD_INP_TWO.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_THR.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_FOU.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_FIV.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_SIX.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_SEV.getIndex()] = true;
			inputAvail[ELEMENT.HUD_INP_EIG.getIndex()] = true;
			if(mark < 1) inputAvail[ELEMENT.HUD_INP_NIN.getIndex()] = true;
			break;
		case -1:
			break;
		}
	}
	
}