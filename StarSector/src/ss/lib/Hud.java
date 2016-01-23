package ss.lib;

import java.awt.Graphics;

public class Hud {
	
	public Hud(){
		
	}
	
	public void addMobile(Mobile m){
		Tracon.addMobile(m);
	}
	
	public void addStatic(Static s){
		Tracon.addStatic(s);
	}
	
	public void tick(){
		Tracon.tick();
	}
	
	public void render(Graphics G){
		Draw.hud(G);
//		Tracon.render(G);
	}
	
}
