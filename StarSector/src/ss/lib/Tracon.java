package ss.lib;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import ss.StarSector;
import ss.type.STYPE;
import ss.util.XMLParser;

/**
 * The underlying manager for the sector of space under the control of the player. Tracon keeps
 * the lists of all active Mobile and Static entities and culls those which are no longer relevant.
 * @author Matt Bangert
 *
 */
public class Tracon {

	private static LinkedList<Entity> Mobiles = new LinkedList<Entity>();
	private static LinkedList<Entity> Statics = new LinkedList<Entity>();
	
	private static int arrivalRate = 1;
	private static int departRate = 1;
	
	private static long TOLA	= 0;	//	Time Of Last Arrival
	private static long TOLD	= 0;	//	Time Of Last Departure
	private static long TSLA	= 0;	//	Time Since Last Arrival
	private static long TSLD 	= 0;	//	Time Since Lase Departure
	
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
	}
	
	public static void addMobile(Mobile m){
		Mobiles.push(m);
	}
	
	public static void addStatic(Static s){
		Statics.push(s);
	}
	
	public static int getArrivalRate(){
		return arrivalRate;
	}
	
	public static int getDepartRate(){
		return departRate;
	}
	
	public static boolean loadSave(){
		initTracon();
		return true;
	}
	
	public static boolean loadSector(String sname){
		initTracon();
		XMLParser xmlp = new XMLParser();
		xmlp.parseXmlFile();
		xmlp.parseDocument();
		return true;
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
	
	public static void setDepartRate(int rate){
		departRate = rate;
	}
	
	public static void setArrivalRate(int rate){
		arrivalRate = rate;
	}
	
	/**
	 * Causes all Entity currently being managed by Tracon to update.
	 */
	public static void tick(){
		updateTime();
		if(newArrival()){
			Vector<Static> as = new Vector<Static>();
			for(int i = 0; i < Statics.size(); i++){
				Static s = (Static)Statics.get(i);
				System.out.println(s.getSTYPE() + " " + s.isAvailableForDepart());
				if(s.getSTYPE() == STYPE.GATE && s.isAvailableForDepart()){
					as.add(s);
				}
			}
			if(as.size() > 0){
				Random rand = new Random(System.currentTimeMillis());
				int index = rand.nextInt(as.size());
				as.get(index).releaseDeparture();
			}
			else{
				System.out.println("WARN: NewArrival unable to spawn due to null available Static set.");
			}
		}
		if(newDeparture()){
			
		}
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).tick();
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).tick();
		}
	}
	
	private static boolean newArrival(){
		if(TSLA >= 600000 / arrivalRate){
			TOLA = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	private static boolean newDeparture(){
		if(TSLD >= 600000 / departRate){
			TOLD = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	private static void updateTime(){
		TSLA = System.currentTimeMillis() - TOLA;
		TSLD = System.currentTimeMillis() - TOLD;
	}
	
}
