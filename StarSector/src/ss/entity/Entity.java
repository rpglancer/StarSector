package ss.entity;

import java.awt.Graphics;

import ss.lib.Coords;

/**
 * Anything and everything that walketh, crawleth, crasheth or flyeth in this game.
 * @author Matt Bangert
 *
 */
public abstract class Entity {
	protected Coords loc;						// Location of the Entity
	
	protected boolean available = false;		// Entity is available for selection
	protected boolean selected = false;			// Entity is selected
	
	public abstract boolean canSelect();
	public abstract boolean isSelected();
	public abstract void deselect();
	public abstract Coords getLoc();
	@Deprecated
	public abstract void select();
	public abstract Entity querySelect(int x, int y);
	public abstract void render(Graphics G, boolean p);
	public abstract void tick();
}
