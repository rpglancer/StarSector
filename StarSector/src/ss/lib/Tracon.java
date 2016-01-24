package ss.lib;

import java.awt.Graphics;
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
	
	public static void selectEntityAt(Coords c){
		for(int i = 0; i < Statics.size(); i++){
			Entity Temp = Statics.get(i);
			if(Temp.loc.getX() - (StarSector.SPRITEWIDTH / 2) <= c.getX() && Temp.loc.getX() + (StarSector.SPRITEWIDTH / 2) >= c.getX() &&
				Temp.loc.getY() - (StarSector.SPRITEHEIGHT / 2) <= c.getY() && Temp.loc.getY() + (StarSector.SPRITEHEIGHT / 2) >= c.getY()){
				if(Temp.canSelect){
					Temp.select();
					return;
				}
			}
			else{
				Temp.deselect();
			}
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Entity Temp = Mobiles.get(i);
			if(Temp.loc.getX() - (StarSector.SPRITEWIDTH / 2) <= c.getX() && Temp.loc.getX() + (StarSector.SPRITEWIDTH / 2) >= c.getX() &&
					Temp.loc.getY() - (StarSector.SPRITEHEIGHT / 2) <= c.getY() && Temp.loc.getY() + (StarSector.SPRITEHEIGHT / 2) >= c.getY()){
				if(Temp.canSelect){
					Temp.select();
					return;
				}
			}
			else{
				Mobiles.get(i).deselect();
			}
		}
	}
	
	public static void render(Graphics G){
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).render(G);
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).render(G);
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
