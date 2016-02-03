package ss.lib;

import ss.type.ELEMENT;

/**
 * Xmit contains the temporary data waiting to be transmitted as an order to a Mobile.
 * @author Matt Bangert
 *
 */
public class Xmit {
	private int heading;
	private int mark;
	private boolean[] opsAct = {false, false, false, false, false, false, false, false, false}; 
	private Static waypoint;
	
	/**
	 * Create a new transmission order to be broadcast to a Mobile.
	 * @param m The mobile for which the order applies.
	 */
	public Xmit(Mobile m){
		heading = (int)m.getHdg();
		mark = (int)m.getMK();
		for(int i = 0; i < opsAct.length; i++){
			opsAct[i] = m.getOpsActive(i);
		}
		waypoint = m.getWaypoint();
		System.out.println("New pending Xmit created with:\nHeading: " + heading + "." + mark);
		System.out.println("isHolding: " + opsAct[ELEMENT.HUD_OPS_HLD.getIndex()]);
		System.out.println("isDirect: " + opsAct[ELEMENT.HUD_OPS_DCT.getIndex()]);
		System.out.println("isApproach: " + opsAct[ELEMENT.HUD_OPS_APR.getIndex()]);
		System.out.println("Waypoint: " + waypoint);
	}
	
	public int getHeading(){
		return heading;
	}
	
	public int getMark(){
		return mark;
	}
	
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
	
	public Static getWaypoint(){
		return waypoint;
	}
	
	public void setHeading(int heading){
		this.heading = heading;
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
	
}
