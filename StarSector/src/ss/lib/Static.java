package ss.lib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import ss.StarSector;
import ss.type.MTYPE;
import ss.type.STYPE;

public class Static extends Entity {
	
	private STYPE type;
	private String Name;
	
	private boolean drawName = true;
	
	private long TSLR = 800000;			// Time Since Last Release
	private long TOLR = 0;				// Time Of Last Release
	
	private Coords Arrival;
	private Coords Departure;
	
	private Vector<Mobile> Queue;
	
	public Static(STYPE type){
		this.type = type;
		this.Name = "UntitledStation";
		this.loc = new Coords(StarSector.WIDTH / 2, StarSector.HEIGHT / 2, 120);
		this.Queue = new Vector<Mobile>();
		this.canSelect = false;
		Tracon.addStatic(this);
	}
	
	public Static(String type, String name, int x, int y, int z){
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
		if(Arrival != null){
			Draw.line(g, loc, Arrival, Color.green, p);
		}
		if(drawName){
			Text.BoxText(g, Fonts.RadarText, Text.genTextArea(g, loc, Fonts.RadarText, Name), Text.alignCenter(), Text.alignBottom(), Name);
		}
	}

	@Override
	public void tick() {
		updateTime();
	}
	
	public Coords getArriveCoords(){
		return this.Arrival;
	}
	
	public Coords getDepartCoords(){
		return this.Departure;
	}
	
	public String getName(){
		return Name;
	}

	public STYPE getSTYPE(){
		return type;
	}
	
	public boolean availableForRelease(int rate){
		return TSLR >= 600000 / rate;
	}
	
	public void releaseCraft(){
		new Mobile(MTYPE.M3, this, Tracon.getDestination(this));
		TOLR = System.currentTimeMillis();
	}

	public void setArriveCoords(Coords arrive){
		Arrival = arrive;
	}
	
	public void setDepartCoords(Coords depart){
		Departure = depart;
	}
	
	private void updateTime(){
		TSLR = System.currentTimeMillis() - TOLR;
	}
}
