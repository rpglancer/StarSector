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
	
	public Static(){
		
	}

	@Override
	public void deselect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coords getLoc() {
		// TODO Auto-generated method stub
		return null;
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
