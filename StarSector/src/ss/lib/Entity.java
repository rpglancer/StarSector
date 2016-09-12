package ss.lib;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Anything and everything that walketh, crawleth, crasheth or flyeth in this game.
 * @author Matt Bangert
 *
 */
public abstract class Entity {
	protected Coords loc;
	
	protected boolean canSelect = false;
	protected boolean isSelected = false;
	
	@Deprecated
	protected Rectangle area;
	
	public abstract void deselect();
	public abstract Coords getLoc();
	public abstract void select();
	@Deprecated
	public abstract void setLoc(double x, double y, double z);
	public abstract void render(Graphics G, boolean p);
	public abstract void tick();
}
