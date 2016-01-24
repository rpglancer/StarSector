package ss.lib;

import java.awt.Graphics;
import java.util.Vector;

import ss.StarSector;

public class Static extends Entity {
	private String Type;
	private String Name;
	
	private Coords Arrival;
	private Coords Departure;
	
	private Vector<Mobile> Queue;
	
	public Static(String Type){
		this.Type = Type;
		this.Name = "TestStation";
		this.loc = new Coords(StarSector.WIDTH / 2, StarSector.HEIGHT / 2, 0);
		this.Queue = new Vector<Mobile>();
		this.SpriteCol = 1;
		this.SpriteRow = 1;
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
	public void render(Graphics G) {
		Draw.sprite_centered(G, StarSector.Sprites.grabImage(SpriteCol, SpriteRow, 16, 16), this.loc.getX(), this.loc.getY());
		// TODO Auto-generated method stub
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
