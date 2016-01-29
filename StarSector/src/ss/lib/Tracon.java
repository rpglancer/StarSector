package ss.lib;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import ss.StarSector;

public class Tracon {

	private static LinkedList<Entity> Mobiles = new LinkedList<Entity>();
	private static LinkedList<Entity> Statics = new LinkedList<Entity>();
	
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
	
	public static Entity selectEntityAt(MouseEvent arg0){
		for(int i = 0; i < Statics.size(); i++){
			Entity Temp = Statics.get(i);
			if(Temp.loc.GetX() - (StarSector.SPRITEWIDTH / 2) <= arg0.getX() && Temp.loc.GetX() + (StarSector.SPRITEWIDTH / 2) >= arg0.getX() &&
				Temp.loc.GetY() - (StarSector.SPRITEHEIGHT / 2) <= arg0.getY() && Temp.loc.GetY() + (StarSector.SPRITEHEIGHT / 2) >= arg0.getY()){
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
			if(Temp.loc.GetX() - (StarSector.SPRITEWIDTH / 2) <= arg0.getX() && Temp.loc.GetX() + (StarSector.SPRITEWIDTH / 2) >= arg0.getX() &&
					Temp.loc.GetY() - (StarSector.SPRITEHEIGHT / 2) <= arg0.getY() && Temp.loc.GetY() + (StarSector.SPRITEHEIGHT / 2) >= arg0.getY()){
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
	
	public static void Render(Graphics G, boolean p){
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).render(G,p);
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).render(G,p);
		}
	}
	
	public static void tick(){
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).tick();
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).tick();
		}
	}
	
}
