package ss.lib;

import java.awt.Graphics;
import java.util.Vector;

public class Mobile extends Entity{
	
	private String Name;
	private String Type;
	private String Owner;
	private Static Destination;
	private Static Origin;
	
	private double AccelCurrent;
	private double AccelMin;
	private double AccelMax;
	private double SpeedCurrent;
	private double SpeedMax;
	
	private int HeadingCurrent;			// Contains heading as viewed from an x/y perspective 0-359
	private int HeadingDesired;
	private int MarkCurrent;			// Contains the marked heading for traversal in an x/z perspective. 270-90
	private int MarkDesired;
	
	private Vector<Coords> History;
	
	public Mobile(){
		
	}
	
	public Mobile(String Type, Static Origin, Static Destination){
		this.Type = Type;
		this.Origin = Origin;
		this.Destination = Destination;
		setType();
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
	public void render(Graphics G){
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
	
	private void setType(){
		switch(this.Type){
		case "M0":
			break;
		case "M1":
			break;
		case "M2":
			break;
		case "M3":
			break;
		case "M4":
			break;
		case "M5":
			break;
		case "M6":
			break;
		case "M7":
			break;
		case "TS":
			break;
		case "TM":
			break;
		case "TL":
			break;
		case "TP":
			break;
		default:
			break;	
		}
	}
	
}
