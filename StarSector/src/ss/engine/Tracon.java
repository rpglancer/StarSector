package ss.engine;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import ss.entity.Entity;
import ss.entity.Mobile;
import ss.entity.Static;
import ss.lib.Calc;
import ss.lib.Coords;
import ss.type.MTYPE;
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
	
	private static Vector<Chatter> chatterQueue = new Vector<Chatter>();
	
	private static int arrivalRate = 1;
	private static int departRate = 1;
	
	private static long TOLA	= 0;	//	Time Of Last Arrival		[in millis]
	private static long TOLD	= 0;	//	Time Of Last Departure		[in millis]
	private static long TSLA	= 0;	//	Time Since Last Arrival		[in millis]
	private static long TSLD 	= 0;	//	Time Since Last Departure	[in millis]
	
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
		TOLA = TOLD = TSLA = TSLD = 0;
	}
	
	public static void addMobile(Mobile m){
		Mobiles.push(m);
	}
	
	public static void addStatic(Static s){
		Statics.push(s);
	}
		
//	@Deprecated
//	public static Static getDestination(Static origin){
//		if(Statics.size() < 2) return null;
//		Random rand = new Random();
//		rand.setSeed(System.currentTimeMillis());
//		Static dest;
//		do{
//			dest = (Static)Statics.get(rand.nextInt(Statics.size()));
//		}while(dest == origin);
//		return dest;
//	}
	public static Static assignDestination(Static origin){
		if(Statics.size() < 2){
			System.out.println("WARN: Cannot assign destination due to isufficient pool");
			return null;
		}
		Vector<Static> avail = new Vector<Static>();
		for(int i = 0; i < Statics.size(); i++){
			Static temp = (Static)Statics.get(i);
			if(temp.getSTYPE() == STYPE.GATE || temp.getSTYPE() == STYPE.STATION)
				avail.add(temp);
		}
		Static dest;
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		do{
			dest = (Static)avail.get(rand.nextInt(avail.size()));
		}while(dest == origin);
		return dest;
	}
	
	/**
	 * Creates a vector of the specified type of Mobiles.
	 * A
	 * @param ofType MTYPE of Mobile of null for all.
	 * @return Vector of Mobile of MTYPE
	 */
	public static Vector<Mobile> getMobiles(MTYPE ofType){
		Vector<Mobile> m = new Vector<Mobile>();
		if(ofType == null){
			for(int i = 0; i < Mobiles.size(); i++){
				m.add((Mobile)Mobiles.get(i));
			}
			return m;
		}
		else{
			for(int i = 0; i < Mobiles.size(); i++){
				Mobile temp = (Mobile)Mobiles.get(i);
				if(temp.getMType() == ofType)
					m.add(temp);
			}
			return m;
		}
	}
	
	public static Vector<Static> getStatics(STYPE ofType){
		Vector<Static> s = new Vector<Static>();
		if(ofType == null){
			for(int i = 0; i < Statics.size(); i++){
				s.add((Static)Statics.get(i));
			}
			return s;
		}
		else{
			for(int i = 0; i < Statics.size(); i++){
				Static temp = (Static)Statics.get(i);
				if(temp.getSTYPE() == ofType)
					s.add(temp);
			}
			return s;
		}
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
	
	/**
	 * Selects and returns a {@link Mobile} at the specified MouseEvent coordinates.
	 * @param arg0	MouseEvent
	 * @return
	 */
	public static Mobile selectMobileAt(MouseEvent arg0){
		double x = arg0.getX();
		double y = arg0.getY();
		Mobile temp = null;
		for(int i = 0; i < Mobiles.size(); i++){
			if((Mobile)Mobiles.get(i).querySelect((int)x, (int)y) != null) temp = (Mobile)Mobiles.get(i).querySelect((int)x, (int)y);
		}
		return temp;
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
	
	public static void pushChatter(String msg){
		Chatter chat = new Chatter(msg);
		if(chatterQueue.size() < 1) chat.makeCurrent();
		chatterQueue.addElement(chat);
	}
	
	public static boolean queryApproach(Coords src){
		for(int i = 0; i < Statics.size(); i++){
			if(Calc.distanceKM(src, Statics.get(i).getLoc()) <= 20){
				Static temp = (Static)Statics.get(i);
				if(temp.getSTYPE() == STYPE.GATE || temp.getSTYPE() == STYPE.STATION)
					return true;
			}
		}
		return false;
	}
	
	public static Chatter currentChatter(){
		if(chatterQueue.size() > 0)
			return chatterQueue.get(0);
		else
			return null;
	}
	
	/**
	 * Causes all Entity currently being managed by Tracon to update.
	 */
	public static void tick(){
		updateTime();
		if(checkArrival()){
			TOLA = System.currentTimeMillis();
			addArrival();
		}
		if(checkDeparture()){
			TOLD = System.currentTimeMillis();
		}
		for(int i = 0; i < Statics.size(); i++){
			Statics.get(i).tick();
		}
		for(int i = 0; i < Mobiles.size(); i++){
			Mobiles.get(i).tick();
		}
		if(chatterQueue.size() > 0){
			if(chatterQueue.get(0).timeToDie(5000)){
//				System.out.println("CHATTER REMOVED\n");
				chatterQueue.remove(0);
				if(chatterQueue.size() > 0) chatterQueue.get(0).makeCurrent();
			}
		}
	}
	
	/**
	 * Add a mobile arriving at the sector via a GATE.
	 */
	private static void addArrival(){
		Vector<Static> as = new Vector<Static>();
		for(int i = 0; i < Statics.size(); i++){
			Static s = (Static)Statics.get(i);
			if(s.getSTYPE() == STYPE.GATE && s.availableForRelease(arrivalRate)){
				as.add(s);
			}
		}
		if(as.size() > 0){
			Random rand = new Random(System.currentTimeMillis());
			int index = rand.nextInt(as.size());
			as.get(index).releaseCraft();
		}
		else{
			System.out.println("WARN: NewArrival unable to spawn due to null available Static set.");
		}
	}
	
	/**
	 * Add a mobile departing a station for a GATE or other STATION.
	 */
	private static void addDeparture(){
	}
	
	private static boolean checkArrival(){
		return TSLA >= 600000 / arrivalRate;
	}
	
	private static boolean checkDeparture(){
		return TSLD >= 600000 / departRate;
	}
	
	private static void updateTime(){
		TSLA = System.currentTimeMillis() - TOLA;
		TSLD = System.currentTimeMillis() - TOLD;
	}
	
}
