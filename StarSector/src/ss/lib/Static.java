package ss.lib;

import java.awt.Graphics;
import java.util.Vector;

import ss.StarSector;
import ss.type.STYPE;

public class Static extends Entity {
	private STYPE type;
	private String Name;
	
	private Coords Arrival;
	private Coords Departure;
	
	private Vector<Mobile> Queue;
	
	public Static(STYPE type){
		this.type = type;
		this.Name = "TestStation";
		this.loc = new Coords(StarSector.WIDTH / 2, StarSector.HEIGHT / 2, 120);
		this.Queue = new Vector<Mobile>();
		this.canSelect = true;
		Tracon.addStatic(this);
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
	}

	@Override
	public void tick() {
		if(Queue.size() > 0){
			ss.lib.Tracon.addMobile(Queue.elementAt(0));
			Queue.removeElementAt(0);
		}
		// TODO Auto-generated method stub
	}

}
