package ss.lib;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import ss.StarSector;

/**
 * The underlying manager for the sector of space under the control of the player. Tracon keeps<br>
 * the lists of all active Mobile and Static entities and culls those which are no longer relevant.
 * @author Matt Bangert
 *
 */
public class Tracon {

	private static LinkedList<Entity> Mobiles = new LinkedList<Entity>();
	private static LinkedList<Entity> Statics = new LinkedList<Entity>();
	@Deprecated
	public Tracon(){
	}
	
	public static void initTracon(){
		if(Mobiles.size() > 0){
			for(int i = 0; i < Mobiles.size(); i++){
				Mobiles.pop();
			}
		}
		if(Statics.size() > 0){
			for(int i = 0; i < Statics.size(); i++){
				Statics.pop();
			}
		}
		// gen gates
		// gen stations
		// gen fixes
	}
	
	public static void addMobile(Mobile m){
		Mobiles.push(m);
	}
	
	public static void addStatic(Static s){
		Statics.push(s);
	}
	
	@Deprecated
	/**
	 * Selects and returns an entity at the specified MouseEvent coordinates.
	 * @param arg0	MouseEvent
	 * @return
	 */
	public static Entity selectEntityAt(MouseEvent arg0){
		double x,y;
		for(int i = 0; i < Statics.size(); i++){
			Entity Temp = Statics.get(i);
			x = Temp.loc.GetX();
			if(Hud.getP()) y = Temp.loc.GetY();
			else y = Temp.loc.GetZ();
			if(x - (StarSector.SPRITEWIDTH / 2) <= arg0.getX() && x + (StarSector.SPRITEWIDTH / 2) >= arg0.getX() &&
				y - (StarSector.SPRITEHEIGHT / 2) <= arg0.getY() && y + (StarSector.SPRITEHEIGHT / 2) >= arg0.getY()){
				if(Temp.canSelect){
					Temp.select();
					return Temp;
				}
			}
			else{
				Temp.deselect();
			}
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Entity Temp = Mobiles.get(i);
			x = Temp.loc.GetX();
			if(Hud.getP()) y = Temp.loc.GetY();
			else y = Temp.loc.GetZ();
			if(x - (StarSector.SPRITEWIDTH / 2) <= arg0.getX() && x + (StarSector.SPRITEWIDTH / 2) >= arg0.getX() &&
					y - (StarSector.SPRITEHEIGHT / 2) <= arg0.getY() && y + (StarSector.SPRITEHEIGHT / 2) >= arg0.getY()){
				if(Temp.canSelect){
					Temp.select();
					return Temp;
				}
			}
			else{
				Temp.deselect();
			}
		}
		return null;
	}
	
	/**
	 * Selects and returns a {@link Mobile} at the specified MouseEvent coordinates.
	 * @param arg0	MouseEvent
	 * @return
	 */
	public static Mobile selectMobileAt(MouseEvent arg0){
		double x,y;
		for(int i = 0; i < Mobiles.size(); i++){
			Mobile Temp = (Mobile)Mobiles.get(i);
			x = Temp.loc.GetX();
			if(Hud.getP()) y = Temp.loc.GetY();
			else y = Temp.loc.GetZ();
			if(x - (StarSector.SPRITEWIDTH / 2) <= arg0.getX() && x + (StarSector.SPRITEWIDTH / 2) >= arg0.getX() &&
					y - (StarSector.SPRITEHEIGHT / 2) <= arg0.getY() && y + (StarSector.SPRITEHEIGHT / 2) >= arg0.getY()){
				if(Temp.canSelect){
					Temp.select();
					return Temp;
				}
			}
			else{
				Temp.deselect();
			}
		}
		return null;
	}
	
	/**
	 * Draws all Entity currently being managed by Tracon to the screen.
	 * @param G	Graphics to which the entities are rendered.
	 * @param p Perspective in which the entities are rendered.
	 */
	public static void Render(Graphics G, boolean p){
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).render(G,p);
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).render(G,p);
		}
	}
	
	/**
	 * Causes all Entity currently being managed by Tracon to update.
	 */
	public static void tick(){
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).tick();
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).tick();
		}
	}
	
}
