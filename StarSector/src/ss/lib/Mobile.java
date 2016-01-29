package ss.lib;

import java.awt.Graphics;
import java.util.Vector;

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
	
	private Vector<Coords> history;
	
	public Mobile(){
		
	}
	
	public Mobile(MTYPE type, Static origin, Static destination){
		this.type = type;
		this.origin = origin;
		this.destination = destination;
	}
	
	
	@Override
	public void deselect(){
		this.isSelected = false;
	}
	
	@Override
	public Coords getLoc(){
		return this.loc;
	}
	
	@Override
	public void render(Graphics G, boolean p){
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
	}
	
}
