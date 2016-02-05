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
	private int speed;
	private int speedInit;
	
	private int inputStatus;
	
	private boolean[] opsAct = {false, false, false, false, false, false, false, false, false};
	private boolean[] inputAvail = {false, false, false, false, false,
									false, false, false, false, false,
									false, false, false };
	/**
	 * The destination for numeric input to be applied.<br>
	 * <b>true</b>: speed<br>
	 * <b>false</b>: heading<br>
	 */
	private boolean inputDest = false;
	private Static waypoint;
	
	/**
	 * Create a new transmission order to be broadcast to a Mobile.
	 * @param m The mobile for which the order applies.
	 */
	public Xmit(Mobile m){
		headingInit = (int)m.getHdg();
		markInit = (int)m.getMK();
		speedInit = (int)m.getSpd();
		heading = headingInit;
		mark = markInit;
		speed = speedInit;
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
	
	@Deprecated
	public int getHdgStatus(){
//		return headingInputStatus;
		return inputStatus;
	}
	
	@Deprecated
	public int getSpdStatus(){
//		return speedInputStatus;
		return inputStatus;
	}
	
	public int getInputStatus(){
		return inputStatus;
	}
	
	public Static getWaypoint(){
		return waypoint;
	}
	
	private void initHdg(){
		inputStatus = 100;
		heading = headingInit;
		mark = markInit;
		updateInpStatus();
	}
	
	private void initSpd(){
		inputStatus = 100;
		speed = speedInit;
		for(int i = 0; i < inputAvail.length; i++){
			inputAvail[i] = false;
		}
		inputAvail[ELEMENT.HUD_INP_ZER.getIndex()] = true;
		inputAvail[ELEMENT.HUD_INP_CAN.getIndex()] = true;
	}
	
	private void setHeading(int heading){
		switch(heading){
		case -1:
			if(inputStatus == 100){
				this.heading = headingInit;
				inputStatus = -100;
			}
			else if(inputStatus == 10){
				if(this.heading >= 100)
					this.heading /= 100;
				else
					this.heading /= 10;
				inputStatus = -100;
			}
			else if(inputStatus == 1){
				this.heading /= 10;
				inputStatus = -100;
			}
			break;
		case 0:
			if(inputStatus == 100){
				this.heading = heading;
				inputStatus = 10;
			}
			else if(inputStatus == 10){
				this.heading += heading;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = heading;
				inputStatus = -10;
			}
			else if(inputStatus == -10){
				this.mark += heading;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 1:
			if(inputStatus == 100){
				this.heading = heading * 100;
				inputStatus = 10;
			}
			else if(inputStatus == 10){
				this.heading += (heading * 10);
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = heading * 100;
				inputStatus = -10;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 2:
			if(inputStatus == 100){
				this.heading = heading * 100;
				inputStatus = 10;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark +=  heading;
				inputStatus = 0;
			}
			break;
		case 3:
			if(inputStatus == 100){
				this.heading = heading * 100;
				inputStatus = 10;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 4:
			if(inputStatus == 100){
				this.heading = 0;
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 5:
			if(inputStatus == 100){
				this.heading = 0;
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 6:
			if(inputStatus == 100){
				this.heading = 0;
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = - 100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 7:
			if(inputStatus == 100){
				this.heading = 0;
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading * 1;
				inputStatus = 0;
			}
			break;
		case 8:
			if(inputStatus == 100){
				this.heading = 0;
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		case 9:
			if(inputStatus == 100){
				this.heading = 0;
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 10){
				this.heading += heading * 10;
				inputStatus = 1;
			}
			else if(inputStatus == 1){
				this.heading += heading;
				inputStatus = -100;
			}
			else if(inputStatus == -100){
				this.mark = 0;
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -10){
				this.mark += heading * 10;
				inputStatus = -1;
			}
			else if(inputStatus == -1){
				this.mark += heading;
				inputStatus = 0;
			}
			break;
		default:
			break;		
		}
		System.out.println("Processed Input: " + this.heading + "." + this.mark);
	}
	
	@Deprecated
	/*
	 * This is going to be replaced with a unified method for processing input; 
	 * most likely an overhauled and renamed setHeading-like method.
	 */
	public void processInput(int input){
		if(inputDest){
			setSpeed(input);
		}
		else{
			setHeading(input);
		}
		updateInpStatus();
	}

	@Deprecated
	private void setSpeed(int speed){
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
	
	/**
	 * Sets the destination for input.<br>
	 * false = heading<br>
	 * true = speed<br>
	 * @param category destination for input
	 */
	public void setInputDest(boolean dest){
		inputDest = dest;
		if(inputDest)
			initSpd();
		else
			initHdg();
	}
	
	public void setWaypoint(Static waypoint){
		this.waypoint = waypoint;
	}
	
	private void updateInpStatus(){
		switch(inputStatus){
		case 100:
			for(int i = 1; i < inputAvail.length; i++){
				inputAvail[i] = true;
			}
			break;
		case 10:
			if(inputDest){
				
			}
			else{
				if(this.heading >= 300){
					for(int i = 7; i < 10; i++){
						inputAvail[i] = false;
					}
				}
			}
			break;
		case 1:
			if(inputDest){
				
			}
			else{
				if(this.heading >= 360){
					for(int i = 1; i < 10; i++){
						inputAvail[i] = false;
					}
				}
				else{
					for(int i = 1; i < 10; i++){
						inputAvail[i] = true;
					}
				}
			}
			break;
		case -100:
			for(int i = 1; i < inputAvail.length; i++){
				inputAvail[i] = true;
			}
			inputAvail[ELEMENT.HUD_INP_MRK.getIndex()] = false;
			break;
		case -10:
			if(this.mark >= 100){
				inputAvail[ELEMENT.HUD_INP_NIN.getIndex()] = false;
			}
			break;
		case -1:
			inputAvail[ELEMENT.HUD_INP_NIN.getIndex()] = true;
			if(this.mark >= 180){
				for(int i = 1; i < 10; i++){
					inputAvail[i] = false;
				}
			}
			break;
		}
	}
	
}
