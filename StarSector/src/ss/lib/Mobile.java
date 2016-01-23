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
	
	private int Heading;			// Contains heading as viewed from an x/y perspective 0-359
	private int Mark;				// Contains the marked heading for traversal in an x/z perspective. 270-90
	
	private Vector<Coords> History;
	
	
	@Override
	public void deselect(){
		this.isSelected = false;
	}
	
	@Override
	public Coords getLoc(){
		return this.loc;
	}
	
	@Override
	public void render(Graphics G){
		switch(this.Class){
		}
	}
	
	@Override
	public void select(){
		this.isSelected = true;
	}
	
	@Override
	public void setLoc(double x, double y, double z){
		this.loc.setCoords(x, y, z);
	}
	
	@Override
	public void tick(){
	}
	
}
