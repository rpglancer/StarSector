package ss.lib;

import java.awt.Graphics;
import java.util.Vector;

public class Mobile extends Entity{
	
	private String Class;
	private String Owner;
	private Static Destination;
	private Static Origin;
	
	private double AccelCurrent;
	private double AccelMin;
	private double AccelMax;
	private double SpeedCurrent;
	private double SpeedMax;
	
	private Vector<Coords> History;
	
	
	@Override
	public void deselect(){
	}
	
	@Override
	public Coords getLoc(){
		return this.loc;
	}
	
	@Override
	public void render(Graphics G){	
	}
	
	@Override
	public void select(){
	}
	
	@Override
	public void setLoc(double x, double y, double z){	
	}
	
	@Override
	public void tick(){
	}
	
}
