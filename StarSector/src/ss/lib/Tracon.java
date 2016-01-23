package ss.lib;

import java.awt.Graphics;
import java.util.LinkedList;

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
	}
	
	public static void addMobile(Mobile m){
		Mobiles.push(m);
	}
	
	public static void addStatic(Static s){
		Statics.push(s);
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
