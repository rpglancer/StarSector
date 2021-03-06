package ss.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import ss.engine.Hud;
import ss.engine.StarSector;
import ss.engine.Tracon;
import ss.engine.Xmit;
import ss.lib.Calc;
import ss.lib.Coords;
import ss.lib.Draw;
import ss.lib.Fonts;
import ss.lib.Text;
import ss.lib.Text.CHAT;
import ss.type.ELEMENT;
import ss.type.FSTATUS;
import ss.type.MTYPE;

public class Mobile extends Entity{
	
	private FSTATUS status;
	private MTYPE type;
	
	private Coords dir;								// The directional vector of this Mobile.

	private String name;
	private String serial;
	
	private Static destination		= null;			// The Static to which this Mobile is destined.
	private Static origin			= null;			// The Static from which this Mobile originated.
	private Static waypoint			= null;			// The Static waypoint to which this mobile is currently traveling.
	
	private double accelMax			= 0;			// The maximum acceleration rate of the Mobile [in m/s]
	private double speedCurrent		= 0;			// The current speed of the Mobile [in m/s]
	private double speedDesired		= 0;			// The desired speed of the Mobile [in m/s]
	private double speedMax			= 0;			// The maximum speed of the Mobile [in m/s]
	private double turnRateMax		= 5;			// The maximum turn rate of the Mobile [in deg/sec]
	
	private long blinkTimeLast;
	
	private int hdgCurrent			= 0;			// The current heading of the Mobile as viewed from an x/y perspective [0-359]
	private int hdgDesired			= 0;			// The desired heading of the Mobile as viewed from an x/y perspective [0-359]
	private int mkCurrent			= 0;			// The current mark of the Mobile for traversal in an x/z perspective. [0-180]
	private int mkDesired			= 0;			// The desired mark of the Mobile for traversal in an x/z perspective. [0-180]

	/**
	 * Boolean values determining which operations are currently being actively applied to this mobile.<br>
	 * They can be checked publicly by using {@link Mobile#getOpsActive(int)}.<br>
	 * <br>
	 * Current Index Values:<br>
	 * [0] unused<br>
	 * [1] unused<br>
	 * [2] unused<br>
	 * [3] HLD<br>
	 * [4] DCT<br>
	 * [5] APR<br>
	 * [6] unused<br>
	 * [7] unused<br>
	 * [8] unused<br>
	 */
	private boolean[] mobOpsAct = {false, false, false, false, false, false, false, false, false}; 
	/**
	 * Boolean values determining which options are available for user manipulation via the HUD.<br>
	 * They can be checked publicly by using {@link Mobile#getOps(int)}.<br>
	 * <br>
	 * Current Index Values:<br>
	 * [0] unused<br>
	 * [1] heading<br>
	 * [2] speed<br>
	 * [3] HLD<br>
	 * [4] DCT<br>
	 * [5] APR<br>
	 * [6] XMIT<br>
	 * [7] CNCL<br>
	 * [8] Contact<br>
	 */
	private boolean[] mobOps = {false, false, false, false, false, false, false, true, true}; 
	
	private Vector<Coords> history = new Vector<Coords>();
		
	public Mobile(MTYPE type, Static origin, Static destination, int[] info){
		status = FSTATUS.ARIDEP;
		available = status.canSelect();
		this.type = type;
		this.serial = genSerial();
//		this.serial = Text.genSerial(this);
		this.name = this.type.getType();
		this.origin = origin;
		this.destination = destination;
		this.loc = new Coords(info[0], info[1], info[2]);
		hdgDesired = hdgCurrent = info[3];
		mkDesired = mkCurrent = info[4];
		accelMax = 5;
		speedMax = 150;
		speedCurrent = speedMax * 2;
		speedDesired = speedMax;
		dir = dVector(Hud.getTimeProject());
		blinkTimeLast = System.currentTimeMillis();
		Tracon.addMobile(this);
		Tracon.pushChatter(Text.chatResponse(CHAT.DEPART, serial, "Tracon", origin.getName(), destination.getName()));
	}
	
	public Xmit openComms(){
		return new Xmit(this, hdgCurrent, mkCurrent, (int)speedCurrent, (int)speedMax, waypoint);
	}
	
	public void xmit(byte actions, int hdg, int mk, int spd, Static wpt, boolean ops[]){
		for(int i = 0; i < ops.length; i++){
			mobOpsAct[i] = ops[i];
		}
		if(actions >= 11){
			waypoint = wpt;
			actions -= 11;
		}
		if(actions >= 5){
			speedDesired = spd;
			actions -= 5;
		}
		if(actions >= 3){
			mkDesired = mk;
			actions -= 3;
		}
		if(actions >= 1){
			hdgDesired = hdg;
			actions -= 1;
		}
		if(actions > 0){
			System.out.println("WARN: Malformed or invalid Mobile.call actions.");
		}
	}
	
	public boolean canSelect(){
		return available;
	}
	
	public void contact(){
		setFlightStatus(FSTATUS.NORMAL);
		for(int i = 1; i < mobOps.length; i++){
			mobOps[i] = true;
		}
		mobOps[ELEMENT.HUD_OPS_CON.getIndex()] = false;
		Tracon.pushChatter(Text.chatResponse(CHAT.CONTACT, serial, "Tracon", null, null));
	}
	
	@Override
	public void deselect(){
		this.selected = false;
	}

	public Static getDestination(){
		return destination;
	}
		
	@Override
	public Coords getLoc(){
		return this.loc;
	}
	
	@Deprecated
	public MTYPE getMType(){
		return this.type;
	}
	
	/**
	 * Returns the current boolean status for {@link Mobile#mobOps}. <br>
	 * <br>
	 * <b>Note: </b><i>As a substitute for remembering which index value to use,</i><br> {@link ss.type.ELEMENT#getIndex()} <i>may be used.</i>
	 * @param i The index value to be queried.
	 * @return The boolean value at the queried index.
	 */
	public boolean getOps(int i){
		if(i > mobOps.length - 1){
			System.out.println("WARN: getOps() index value out of range");
			return false;
		}
		return mobOps[i];
	}
	
	/**
	 * Returns the current boolean status for {@link Mobile#mobOpsAct}. <br>
	 * <br>
	 * <b>Note: </b><i>As a substitute for remembering which index value to use,</i><br> {@link ss.type.ELEMENT#getIndex()} <i>may be used.</i>
	 * @param i The index value to be queried.
	 * @return The boolean value at the queried index.
	 */
	public boolean getOpsActive(int i){
		if(i > mobOpsAct.length - 1){
			System.out.println("WARN: getOpsActive() index value out of range");
			return false;
		}
		return mobOpsAct[i];
	}
		
	public Static getWaypoint(){
		return waypoint;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	@Override
	public Mobile querySelect(int x, int y){
		int py;
		if(Hud.getP()) py = (int)loc.GetY();
		else py = (int)loc.GetZ();
		if(x >= loc.GetX() - (StarSector.SPRITEWIDTH / 2) && x <= loc.GetX() + (StarSector.SPRITEWIDTH / 2)){
			if(y >= py - (StarSector.SPRITEHEIGHT / 2) && y <= py + (StarSector.SPRITEHEIGHT / 2)){
				if(available){
					selected = true;
					return this;
				} 
			}
		}
		selected = false;
		return null;
	}
	
	@Override
	public void render(Graphics g, boolean p){
		if(selected)Draw.history(g, history);
		if(p) Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetY());
		else Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetZ());
		if(selected)Draw.square_centered(g, this.loc, (int)(2 * StarSector.PPKM), Color.cyan, Hud.getP());
		Draw.line(g, loc, dir, Color.green, p);
		renderFlightInfo(g, p);
	}
	
	public void renderName(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.cyan);
		Text.BoxText(g, Fonts.RadarText, ELEMENT.HUD_OVW_ENTITY.getArea(), Text.alignCenter(), Text.alignMiddle(), serial);
		g.setColor(c);
	}
	
	public void setFlightStatus(FSTATUS status){
		this.status = status;
		available = status.canSelect();
		if(selected) this.deselect();
	}
	
	public void setOps(int i, boolean value){
		if(i > mobOps.length - 1){
			System.out.println("WARN: setOps() index value out of range.");
			return;
		}
		else mobOps[i] = value;
	}
	
	@Override
	public void tick(){
		updateHistory();
		throttle();
		turn();
		if(status == FSTATUS.ARIDEP && speedCurrent == speedMax){
			status = FSTATUS.HNDOFF;
			available = FSTATUS.HNDOFF.canSelect();
		}
		if(mobOpsAct[ELEMENT.HUD_OPS_DCT.getIndex()] && waypoint != null){
			this.hdgDesired = Calc.convertCoordsToHdg(loc, waypoint.loc);
			this.mkDesired = Calc.convertCoordsToMk(loc, waypoint.loc);
		}
		if(status != FSTATUS.HNDOFF)
			mobOps[ELEMENT.HUD_OPS_APR.getIndex()] = Tracon.queryApproach(loc);
		loc.add(mVector());
		dir = dVector(Hud.getTimeProject());
	}
		
	private int calcDegOffset(int cur, int des){
		int a = (Math.abs(cur - des));
		if(a > 180){
			a = 360 - a;
			return a + des;
		}
		else
			return a;
	}
	
	/**
	 * Create a directional vector for projecting a course visually.
	 * @param mins	The number of minutes for which to calculate the vector.
	 * @return		<b>Coords</b> The projected coordinates of a mobile.
	 */
	private Coords dVector(int mins){
		double spd = Calc.KMPS(speedCurrent);
		spd *= 10;
		spd *= mins;
		double vv[] = Calc.getP(hdgCurrent, mkCurrent);
		double ex = spd * vv[0];
		double ey = spd * vv[1];
		double ez = spd * vv[2];
		if(hdgCurrent > 180) ex = -ex;
		if(hdgCurrent > 270 || hdgCurrent < 90) ey = -ey;
		if(mkCurrent < 90) ez = -ez;
		ex *= StarSector.PPKM;
		ey *= StarSector.PPKM;
		ez *= StarSector.PPKM;
		ex += loc.GetX();
		ey += loc.GetY();
		ez += loc.GetZ();
		return new Coords(ex, ey, ez);
	}
	
	private String genSerial(){
		Random r = new Random();
		r.setSeed(System.nanoTime());
		int serial = (r.nextInt(9) + 1) * 1000;
		serial += r.nextInt(9) * 100;
		serial += r.nextInt(9) * 10;
		serial += r.nextInt(9);
		return type + "-" + serial + Text.Races[r.nextInt(Text.Races.length)];
	}
	
	// this should probably go elsewhere, Text perhaps?
	private Color getBlinkColor(Color c1, Color c2, int rate){
		if(System.currentTimeMillis() - blinkTimeLast < rate){
			return c1;
		}
		else{
			if(System.currentTimeMillis() - blinkTimeLast > rate * 2)
				blinkTimeLast = System.currentTimeMillis();
			return c2;
		}
	}
	
	/**
	 * Creates a movement vector for this mobile.
	 * @return The coordinates of the resulting movement vector.
	 */
	private Coords mVector(){
		double spd = Calc.KMPS(speedCurrent);
		double vv[] = Calc.getP(hdgCurrent, mkCurrent);
//		System.out.println("mVector: " + vv[0] + ", " + vv[1] + ", " + vv[2]);
		double ex = spd * vv[0];
		double ey = spd * vv[1];
		double ez = spd * vv[2];
		if(hdgCurrent > 180) ex = -ex;
		if(hdgCurrent > 270 || hdgCurrent < 90) ey = -ey;
		if(mkCurrent < 90) ez = -ez;
		ex *= StarSector.PPKM;
		ey *= StarSector.PPKM;
		ez *= StarSector.PPKM;
		return new Coords(ex, ey, ez);
	}
	
	private void renderFlightInfo(Graphics G, boolean p){
		Color PrevC = G.getColor();
		Font PrevF = G.getFont();
		setFlightInfoColor(G);
		G.setFont(Fonts.FlightInfo);
		FontMetrics fm = G.getFontMetrics(Fonts.RadarText);
		int y = (int)loc.GetY();
		if(!p) y = (int)loc.GetZ();
		int x = (int)(loc.GetX() + 8);
		switch(status){
		case ARIDEP:
			G.drawString((int)speedCurrent+"", x, y);
			break;
		case CNFLCT:
			break;
		case EMGNCY:
			break;
		case HNDOFF:
			G.drawString(serial.substring(3, serial.length()), x, y);
			y+=fm.getAscent();
			G.drawString((int)speedCurrent+"", x, y);
			break;
		case MISAPR:
			break;
		case NORMAL:
			G.drawString(name, x, y);
			y+=fm.getAscent();
			if(waypoint != null)
				G.drawString(waypoint.getName(), x, y);
			else
				G.drawString(hdgCurrent + "." + mkCurrent, x, y);
			y+=fm.getAscent();
			G.drawString((int)speedCurrent + "", x, y);
			break;
		case ONAPR:
			break;
		default:
			break;		
		}
		G.setColor(PrevC);
		G.setFont(PrevF);
	}
	
	public void renderStatus(Graphics G){
		Draw.shape(G, ELEMENT.HUD_STA.getArea(), 1, Color.darkGray, Color.black);
		G.setColor(Color.cyan);
		Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_STA_FROM.getArea(), Text.alignLeft(), Text.alignMiddle(), "FROM:");
		Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_STA_FROM.getArea(), Text.alignRight(), Text.alignMiddle(), origin.getName());
		Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_STA_TO.getArea(), Text.alignLeft(), Text.alignMiddle(), "TO:");
		Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_STA_TO.getArea(), Text.alignRight(), Text.alignMiddle(), destination.getName());
		Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_STA_WAY.getArea(), Text.alignLeft(), Text.alignMiddle(), "WAY:");
		if(waypoint != null)
			Text.BoxText(G, Fonts.HudText, ELEMENT.HUD_STA_WAY.getArea(), Text.alignRight(), Text.alignMiddle(), waypoint.getName());
	}
	
	private void setFlightInfoColor(Graphics G){
		switch(status){
		case ARIDEP:
			G.setColor(Color.magenta);
			break;
		case CNFLCT:
			G.setColor(Color.yellow);
			break;
		case EMGNCY:
			G.setColor(Color.red);
			break;
		case HNDOFF:
			G.setColor(getBlinkColor(Color.lightGray, Color.gray, 250));
			break;
		case MISAPR:
			G.setColor(Color.green);
			break;
		case NORMAL:
			G.setColor(Color.green);
			break;
		case ONAPR:
			G.setColor(Color.green);
			break;
		default:
			break;	
		}
	}
	
	private void throttle(){
		if(speedCurrent == speedDesired) return;
		else{
			if(speedCurrent > speedDesired){
				if(accelMax * StarSector.SweepLength > speedCurrent - speedDesired){
					speedCurrent = speedDesired;
				}
				else{
					speedCurrent -= accelMax * StarSector.SweepLength;
				}
			}
			else{
				if(accelMax * StarSector.SweepLength > speedDesired - speedCurrent){
					speedCurrent = speedDesired;
				}
				else{
					speedCurrent += accelMax * StarSector.SweepLength;
				}
			}
		}
	}
	
	private void turn(){
		// Adjust Heading
		if(hdgCurrent != hdgDesired){
			int recipOne = 360 - Calc.reciprocal(hdgCurrent) + hdgDesired;
			int recipTwo = 0 + Calc.reciprocal(hdgCurrent) - hdgDesired;
			if(recipOne >= 360) recipOne -= 360;
			if(recipOne < 0) recipOne += 360;
			if(recipTwo >= 360) recipTwo -= 360;
			if(recipTwo < 0) recipTwo += 360;
			// Turn left
			if(recipOne <= recipTwo){
				if(calcDegOffset(hdgCurrent, hdgDesired) < turnRateMax * StarSector.SweepLength)
					hdgCurrent -= calcDegOffset(hdgCurrent, hdgDesired);
				else
					hdgCurrent -= turnRateMax * StarSector.SweepLength;
				if(hdgCurrent < 0) hdgCurrent += 360;
			}
			// Turn right
			else{
				if(calcDegOffset(hdgCurrent, hdgDesired) < turnRateMax * StarSector.SweepLength)
					hdgCurrent += calcDegOffset(hdgCurrent, hdgDesired);
				else
					hdgCurrent += turnRateMax * StarSector.SweepLength;
				if(hdgCurrent >= 360) hdgCurrent -= 360;
			}
		}

		// Adjust Mk
		if(mkCurrent != mkDesired){
			if(mkCurrent > mkDesired){
				if(calcDegOffset(mkCurrent, mkDesired) < turnRateMax * StarSector.SweepLength)
					mkCurrent -= calcDegOffset(mkCurrent, mkDesired);
				else
					mkCurrent -= turnRateMax * StarSector.SweepLength;
			}
			else{
				if(calcDegOffset(mkCurrent, mkDesired) < turnRateMax * StarSector.SweepLength)
					mkCurrent += calcDegOffset(mkCurrent, mkDesired);
				else
					mkCurrent += turnRateMax * StarSector.SweepLength;
			}
		}
//		System.out.println("DEBUG: " + hdgCurrent + "." + mkCurrent);
	}
	
	private void updateHistory(){
		Coords temp = new Coords(loc.GetX(), loc.GetY(), loc.GetZ());
		history.add(0, temp);
		if(history.size() == 20)history.remove(19);
	}
	
}
