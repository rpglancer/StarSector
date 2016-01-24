package ss.lib;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
	protected Coords loc;
	
	protected boolean canSelect = false;
	protected boolean isSelected = false;
	
	protected int SpriteCol;
	protected int SpriteRow;
	
	protected Rectangle Area;
	
	public abstract void deselect();
	public abstract Coords getLoc();
	public abstract void select();
	public abstract void setLoc(double x, double y, double z);
	public abstract void render(Graphics G);
	public abstract void tick();
}
