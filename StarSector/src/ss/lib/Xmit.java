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
	private int speedMax;
	
	/**
	 * Tracks and manages the status of input submitted via the Hud input.<br>
	 * <b>100:</b> Input is a 100s value<br>
	 * <b>-100:</b> Input is a 100s value for mark<br>
	 * <b>10:</b> Input is a 10s value<br>
	 * <b>-10:</b> Input is a 10s value for mark<br>
	 * <b>1:</b> Input is a 1s value<br>
	 * <b>-1:</b> Input is a 1s value for mark<br>
	 * <b>0:</b> The input process has been completed<br>
	 */
	private int inputStatus;
	
	private boolean[] opsAct = {false, false, false, false, false, false, false, false, false};
	/**
	 * Array for managing the availability of input HudElements as they pertain to the Mobile for
	 * which this Xmit will apply.<br>
	 */
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
		speedMax = (int)m.getSpdMax();
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
	
	/**
	 * Allows the array of inputAvail to be queried by external methods [ie: the hud].
	 * @param index The component of the inputAvail array to be queried.
	 * @return The availability of the queried component.
	 */
	public boolean getInputAvail(int index){
		return inputAvail[index];
	}
	
	/**
	 * Allows the current inputStatus to be queried by external methods [ie: the hud].
	 * @return The current {@link Xmit#inputStatus}
	 */
	public int getInputStatus(){
		return inputStatus;
	}
	
	/**
	 * Determines the active OPS for being drawn on the hud.
	 * @param index The index of the active OP
	 * @return True/False whether the OP is active.
	 */
	public boolean getOpsActive(int index){
		return opsAct[index];
	}
	
	public int getMark(){
		return mark;
	}
	
	public int getSpd(){
		return speed;
	}
	
	public Static getWaypoint(){
		return waypoint;
	}

	public void process(int input){
		if(input == -2){
			if(inputDest)initSpd();
			else initHdg();
			updateInpStatus();
			return;
		}
		if(inputDest){
			switch(inputStatus){
			case 100:
				if(input > speedMax / 100){
					this.speed = 0;
					if(input > speedMax / 10){
						this.speed += input;
						inputStatus = 0;
					}
					else{
						this.speed += input * 10;
						inputStatus = 1;
					}
				}
				else{
					this.speed = input * 100;
					inputStatus = 10;
				}
				break;
			case 10:
				this.speed += input * 10;
				inputStatus = 1;
				break;
			case 1:
				this.speed += input;
				inputStatus = 0;
				break;
			}
		}
		else{
			switch(inputStatus){
			case 100:
				if(input >= 0 && input <= 3){
					this.heading = input * 100;
					inputStatus = 10;
				}
				else if(input != -1){
					this.heading = 0;
					this.heading += input * 10;
					inputStatus = 1;
				}
				else{
					this.heading = headingInit;
					inputStatus = -100;
				}
				break;
			case 10:
				if(input != -1){
					this.heading += input * 10;
					inputStatus = 1;	
				}
				else{
					this.heading /= 100;
					inputStatus = -100;
				}
				break;
			case 1:
				if(input != -1){
					this.heading += input;
					inputStatus = -100;
				}
				else{
					this.heading /= 10;
					inputStatus = -100;
				}
				break;
			case -100:
				if(input >= 0 && input <= 1){
					this.mark = input * 100;
					inputStatus = -10;
				}
				else{
					this.mark = 0;
					this.mark += input * 10;
					inputStatus = -1;
				}
				break;
			case -10:
				this.mark += input * 10;
				inputStatus = -1;
				break;
			case -1:
				this.mark += input;
				inputStatus = 0;
			}
		}
		updateInpStatus();
		if(inputDest)System.out.println("Speed: " + this.speed);
		else System.out.println("Heading: " + this.heading + "." + this.mark);
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
	
	private void initHdg(){
		inputStatus = 100;
		heading = headingInit;
		mark = markInit;
		updateInpStatus();
	}
	
	private void initSpd(){
		inputStatus = 100;
		speed = speedInit;
		updateInpStatus();
	}
		
	public void setOpsActive(int index, boolean status){
		if(index > opsAct.length - 1){
			System.out.println("WARN: Index out of bounds for Xmit.setOpsActive.");
			return;
		}
		else{
			opsAct[index] = status;
		}
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
