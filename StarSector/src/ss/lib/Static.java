package ss.lib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import ss.StarSector;
import ss.type.MTYPE;
import ss.type.STYPE;

public class Static extends Entity {
	private STYPE type;
	private String Name;
	
	private long TSLD = 800000;
	private long TOLD = 0;
	
	private Coords Arrival;
	private Coords Departure;
	
	private Vector<Mobile> Queue;
	
	public Static(STYPE type){
		this.type = type;
		this.Name = "TestStation";
		this.loc = new Coords(StarSector.WIDTH / 2, StarSector.HEIGHT / 2, 120);
		this.Queue = new Vector<Mobile>();
		this.canSelect = false;
		Tracon.addStatic(this);
	}
	
	public Static(String type, String name, int x, int y, int z){
//		System.out.println("Static constructor called with settings " + type + ", " + name + ", " + x + ", " + y + ", " + z);
		switch(type){
		case "STATION":
			this.type = STYPE.STATION;
			break;
		case "FIX":
			this.type = STYPE.FIX;
			break;
		case "GATE":
			this.type = STYPE.GATE;
			break;
		default:
			break;
		}
		this.Name = name;
		this.loc = new Coords(x,y,z);
		this.canSelect = false;
		this.Queue = new Vector<Mobile>();
//		System.out.println("DBG: " + this + " of type " + this.type + " created!");
	}

	@Override
	public void deselect() {
		// TODO Auto-generated method stub
	}

	@Override
	public Coords getLoc() {
		return this.loc;
	}

	@Override
	public void select() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setLoc(double x, double y, double z) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(Graphics g, boolean p){
		if(p) Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetY());
		else Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetZ());
		if(Departure != null){
			Draw.line(g, loc, Departure, Color.pink, p);
		}
	}

	@Override
	public void tick() {
		updateTime();
	}
	

	public STYPE getSTYPE(){
		return type;
	}
	
	public boolean isAvailableForDepart(){
		if(type == STYPE.STATION){
			return TSLD >= 600000 / Tracon.getDepartRate();
		}
		if(type == STYPE.GATE){
			return TSLD >= 600000 / Tracon.getArrivalRate();
		}
		return false;
	}

	public void releaseDeparture(){
		new Mobile(MTYPE.M3, this, null);
		TOLD = System.currentTimeMillis();
	}
	
	public void setDepartCoords(Coords depart){
		Departure = depart;
	}
	
	private void updateTime(){
		TSLD = System.currentTimeMillis() - TOLD;
	}
}
