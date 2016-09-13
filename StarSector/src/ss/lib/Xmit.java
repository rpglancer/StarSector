package ss.lib;

import java.util.Vector;

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
	private byte[] speedFactor = {0,0,0};
	
	private static Vector<Static> waypointsAvailable;
	private static int waypointsIndex = 0;
	
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
	
	private boolean[] opsAct = 		{false, false, false,
									 false, false, false,
									 false, false, false};
	/**
	 * Array for managing the availability of input HudElements as they pertain to the Mobile for
	 * which this Xmit will apply.<br><br>
	 * [index]:[value]<br>
	 * 0:unused<br>
	 * 1:1 2:2 3:3<br>
	 * 4:4 5:5 6:6<br>
	 * 7:7 8:8 9:9<br>
	 * 10:. 11:0 12:CNCL<br>
	 */
	private boolean[] inputAvail = 	{false, false, false,
									 false, false, false,
									 false, false, false,
									 false, false, false,
									 false };
	/**
	 * The destination for numeric input to be applied.<br>
	 * <b>true</b>: speed<br>
	 * <b>false</b>: heading<br>
	 */
	private boolean inputDest = false;
	
	private ELEMENT eleDest;
	
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
		waypointsAvailable = Tracon.getWaypoints();
		if(waypointsAvailable.size() > 0)
			ELEMENT.HUD_LST_WPT.getResponse().setText(waypointsAvailable.get(waypointsIndex).getName());
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
	
	private void factorSpeed(){
		int s = 0;
		byte c = 0;
		for(s = this.speedMax, c = 0; s >= 100; s-=100, c++);
		speedFactor[0] = c;
		for(c = 0; s >= 10; s-=10, c++);
		speedFactor[1] = c;
		for(c = 0; s > 0; s-=1, c++);
		speedFactor[2] = c;
//		System.out.println("Factored Speed: " + speedFactor[0] + "," +speedFactor[1] + "," + speedFactor[2]);
	}
	
	public Static getWaypoint(){
		return waypoint;
	}

	public void input(int val){
		if(val == -2){
			if(eleDest == ELEMENT.HUD_OPS_SPD)
				initSpd();
			if(eleDest == ELEMENT.HUD_OPS_HDG)
				initHdg();
			return;
		}
		if(eleDest == ELEMENT.HUD_OPS_SPD){
			adjustSpeed(val);
		}
		if(eleDest == ELEMENT.HUD_OPS_HDG){
			adjustHdg(val);
		}
		updateInpStatus();
	}
	
	public void adjustSpeed(int val){
		switch(inputStatus){
		case 100:
			this.speed = val * 100;
			inputStatus = 10;
			break;
		case 10:
			this.speed += val * 10;
			inputStatus = 1;
			break;
		case 1:
			this.speed += val;
			inputStatus = 0;
			break;
		}
	}

	public void adjustHdg(int val){
		switch(inputStatus){
		case 100:
			if(val == -1){
				this.heading = headingInit;
				inputStatus = -100;
			}
			else{
				this.heading = val * 100;
				inputStatus = 10;
			}
			break;
		case 10:
			if(val == -1) inputStatus = -100;
			else{
				this.heading += val * 10;
				inputStatus = 1;
			}
			break;
		case 1:
			if(val == -1) inputStatus = -100;
			else{
				this.heading += val;
				inputStatus = -100;
			}
			break;
		case -100:
			this.mark = val * 100;
			inputStatus = -10;
			break;
		case -10:
			this.mark += val * 10;
			inputStatus = -1;
			break;
		case -1:
			this.mark += val;
			inputStatus = 0;
			break;
		}
	}
	
@Deprecated	
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

	public void nextWaypt(){
		if(waypointsIndex == waypointsAvailable.size() - 1)
			waypointsIndex = 0;
		else
			waypointsIndex++;
	}
	
	public void prevWaypt(){
		if(waypointsIndex == 0)
			waypointsIndex = waypointsAvailable.size() - 1;
		else
			waypointsIndex--;
	}

@Deprecated
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
	
	public void setInputDest(ELEMENT dest){
		eleDest = dest;
		if(eleDest == ELEMENT.HUD_OPS_HDG) initHdg();
		if(eleDest == ELEMENT.HUD_OPS_SPD) initSpd();
		System.out.println("Element Destination: " + eleDest);
	}

@Deprecated
	public void setWaypoint(Static waypoint){
		this.waypoint = waypoint;
	}

	public void setWaypoint(){
		this.waypoint = waypointsAvailable.get(waypointsIndex);
		opsAct[ELEMENT.HUD_OPS_DCT.getIndex()] = true;
	}
	
	private void initHdg(){
		inputStatus = 100;
		heading = headingInit;
		mark = markInit;
		for(int i = 0; i < inputAvail.length; i++){
			inputAvail[i] = false;
		}
		updateInpStatus();
	}
	
	private void initSpd(){
		inputStatus = 100;
		speed = speedInit;
		for(int i = 0; i < inputAvail.length; i++){
			inputAvail[i] = false;
		}
		factorSpeed();
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
		if(eleDest == ELEMENT.HUD_OPS_HDG){
			switch(inputStatus){
			case 100:
				inputAvail[1] = true;
				inputAvail[2] = true;
				inputAvail[3] = true;
				inputAvail[10] = true;
				inputAvail[11] = true;
				inputAvail[12] = true;
				break;
			case 10:
				inputAvail[4] = true;
				inputAvail[5] = true;
				inputAvail[6] = true;
				inputAvail[7] = true;
				inputAvail[8] = true;
				inputAvail[9] = true;
				if(this.heading >= 300){
					inputAvail[6] = false;
					inputAvail[7] = false;
					inputAvail[8] = false;
					inputAvail[9] = false;
				}
				break;
			case 1:
				inputAvail[6] = true;
				inputAvail[7] = true;
				inputAvail[8] = true;
				inputAvail[9] = true;
				break;
			case -100:
				for(int i = 0; i < inputAvail.length; i++){
					inputAvail[i] = false;
				}
				inputAvail[1] = true;
				inputAvail[11] = true;
				inputAvail[12] = true;
				break;
			case -10:
				for(int i = 0; i < 10; i++){
					inputAvail[i] = true;
				}
				if(this.mark >= 100){
					inputAvail[9] = false;
				}
				inputAvail[10] = false;
				break;
			case -1:
				for(int i = 0; i < inputAvail.length; i++){
					inputAvail[i] = true;
				}
				inputAvail[10] = false;
				if(this.mark >= 180){
					for(int i = 0; i < 10; i++){
						inputAvail[i] = false;
					}
				}
				break;
			}
		}
		if(eleDest == ELEMENT.HUD_OPS_SPD){
			inputAvail[11] = true;
			inputAvail[12] = true;
			switch(inputStatus){
			case 100:
				for(byte i = 1; i <= speedFactor[0]; i++){
					inputAvail[i] = true;
				}
				break;
			case 10:
				for(byte i = 1; i < 10; i++){
					inputAvail[i] = true;
				}
				if(this.speed == speedFactor[0] * 100){
					for(byte i = 9; i > speedFactor[1]; i--){
						inputAvail[i] = false;
					}
				}
				break;
			case 1:
				for(byte i = 1; i < 10; i++){
					inputAvail[i] = true;
				}
				if(this.speed == (speedFactor[0] * 100) + (speedFactor[1] * 10)){
					for(byte i = 9; i > speedFactor[2]; i--){
						inputAvail[i] = false;
					}
				}
				break;
			}
		}
	}
	
}
