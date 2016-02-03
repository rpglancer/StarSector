package ss.lib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import ss.StarSector;
import ss.type.ELEMENT;
import ss.type.MTYPE;

public class Mobile extends Entity{
	
	private String name;
	private MTYPE type;
	private String owner;
	private Static destination		= null;
	private Static origin			= null;
	private Static waypoint			= null;
	
	private double accelCurrent		= 0;
	private double accelMin			= 0;
	private double accelMax			= 0;
	private double speedCurrent		= 0;
	private double speedMax			= 0;
	
	private int hdgCurrent			= 0;			// Contains heading as viewed from an x/y perspective 0-359
	private int hdgDesired			= 0;
	private int mkCurrent			= 0;			// Contains the marked heading for traversal in an x/z perspective. 270-90
	private int mkDesired			= 0;
	
	private boolean[] mobOpsAct = {false, false, false, false, false, false, false, false, false}; 
	
	/**
	 * Boolean values determining which options are available for user manipulation via the HUD.<br>
	 * They can be checked publicly by using {@link Mobile#getOps(int)}.<br>
	 * <br>
	 * Current Index Values:<br>
	 * [0] unused<br>
	 * [1] heading<br>
	 * [2] speed<br>
	 * [3] MVS<br>
	 * [4] DCT<br>
	 * [5] APR<br>
	 * [6] XMIT<br>
	 * [7] CNCL<br>
	 * [8] Contact<br>
	 */
	private boolean[] mobOps = {false, false, false, false, false, false, false, true, true}; 
	
	private boolean[] mobInput = {false, false, false, false, false,
								  false, false, false, false, false,
								  false, false, false };
	
	private Coords dir;
	
	private Vector<Coords> history = new Vector<Coords>();
	
	public Mobile(){
		
	}
	
	public Mobile(MTYPE type, Static origin, Static destination){
		this.canSelect = true;
		this.type = type;
		this.name = "ShiggyDoo";
		this.origin = origin;
		this.destination = destination;
		this.loc = new Coords(origin.getLoc().GetX(), origin.getLoc().GetY(), origin.getLoc().GetZ());
		hdgCurrent = 45;
		hdgDesired = hdgCurrent;
		mkCurrent = 90;
		mkDesired = mkCurrent;
		accelCurrent = 5;
		accelMax = accelCurrent;
		speedCurrent = 150;
		speedMax = 150;
		this.dir = Calc.dVector(this, Hud.getTimeProject());
		Tracon.addMobile(this);
	}
	
	@Override
	public void deselect(){
		this.isSelected = false;
	}
	
	@Override
	public Coords getLoc(){
		return this.loc;
	}
	
	public double getSpd(){
		return this.speedCurrent;
	}
	
	public double getHdg(){
		return (double)hdgCurrent;
	}
	
	public double getMK(){
		return (double)mkCurrent;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public void render(Graphics g, boolean p){
		if(isSelected)Draw.history(g, history);
		if(p) Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetY());
		else Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetZ());
		if(isSelected)Draw.square_centered(g, this.loc, (int)(2 * StarSector.PPKM), Color.cyan, Hud.getP());
		Draw.line(g, loc, dir, Color.green, p);
	}
	
	@Override
	public void select(){
		if(canSelect) isSelected = true;
	}
	
	@Override
	public void setLoc(double x, double y, double z){
		this.loc.SetCoords(x, y, z);
	}
	
	@Override
	public void tick(){
		updateHistory();
		loc.add(Calc.mVector(this));
		dir = Calc.dVector(this, Hud.getTimeProject());
	}
	
	private void updateHistory(){
		Coords temp = new Coords(loc.GetX(), loc.GetY(), loc.GetZ());
		history.add(0, temp);
		if(history.size() == 20)history.remove(19);
	}
	
	public void contact(){
		for(int i = 1; i < mobOps.length; i++){
			mobOps[i] = true;
		}
		mobOps[ELEMENT.HUD_OPS_CON.getIndex()] = false;
	}
	
	/**
	 * Returns the current boolean status for {@link Mobile#mobOps}. <br>
	 * <br>
	 * <b>Note: </b><i>As a substitute for remembering which index value to use,</i><br> {@link ss.type.ELEMENT#getIndex()} <i>may be used.</i>
	 * @param i The index value to be queried.
	 * @return The boolean value at the queried index.
	 */
	public boolean getOps(int i){
		if(i > mobOps.length - 1){
			System.out.println("WARN: getOps() index value out of range");
			return false;
		}
		return mobOps[i];
	}
	
	public boolean getOpsActive(int i){
		if(i > mobOps.length - 1){
			System.out.println("WARN: getOps() index value out of range");
			return false;
		}
		return mobOpsAct[i];
	}
	
	public void setOps(int i, boolean value){
		if(i > mobOps.length - 1){
			System.out.println("WARN: setOps() index value out of range.");
			return;
		}
		else mobOps[i] = value;
	}
	
	public Static getWaypoint(){
		if(waypoint == null) return null;
		else return waypoint;
	}
	
	public void call(Xmit xmit){
		hdgDesired = xmit.getHeading();
		mkDesired = xmit.getMark();
		for(int i = 0; i < mobOpsAct.length; i++){
			mobOpsAct[i] = xmit.getOpsActive(i);
		}
		waypoint = xmit.getWaypoint();
	}
	
}
