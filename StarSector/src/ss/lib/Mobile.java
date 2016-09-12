package ss.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Vector;

import ss.StarSector;
import ss.type.ELEMENT;
import ss.type.FSTATUS;
import ss.type.MTYPE;

public class Mobile extends Entity{
	
	private FSTATUS status;
	private MTYPE type;
	
	private Coords dir;								// The directional vector of this Mobile.

	private String name;
//	private String owner;
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
	
	public Mobile(MTYPE type, Static origin, Static destination){
		status = FSTATUS.HNDOFF;
		canSelect = status.canSelect();
		this.type = type;
		this.serial = Text.genSerial(this);
		this.name = type.getType();
		this.origin = origin;
		this.destination = destination;
		this.loc = new Coords(origin.getLoc().GetX(), origin.getLoc().GetY(), origin.getLoc().GetZ());
		hdgCurrent = Calc.convertCoordsToHdg(loc, this.origin.getDepartCoords());
		mkCurrent = Calc.convertCoordsToMk(loc, this.origin.getDepartCoords());
		hdgDesired = hdgCurrent;
		mkDesired = mkCurrent;
		accelMax = 5;
		speedCurrent = 150;
		speedDesired = speedCurrent;
		speedMax = 150;
		this.dir = Calc.dVector(this, Hud.getTimeProject());
		blinkTimeLast = System.currentTimeMillis();
		Tracon.addMobile(this);
	}
	
	@Override
	public void deselect(){
		this.isSelected = false;
	}
	
	@Override
	public Coords getLoc(){
		return this.loc;
	}

	@Override
	public void render(Graphics g, boolean p){
		if(isSelected)Draw.history(g, history);
		if(p) Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetY());
		else Draw.sprite_centered(g, StarSector.Sprites.grabImage(type.getSpriteC(), type.getSpriteR(), 16, 16), this.loc.GetX(), this.loc.GetZ());
		if(isSelected)Draw.square_centered(g, this.loc, (int)(2 * StarSector.PPKM), Color.cyan, Hud.getP());
		Draw.line(g, loc, dir, Color.green, p);
		renderFlightInfo(g, p);
	}
	
	@Override
	public void select(){
		if(canSelect) isSelected = true;
	}
	
	@Override
	public void setLoc(double x, double y, double z){
		this.loc.SetCoords(x, y, z);
	}
	
	@Override
	public void tick(){
		updateHistory();
		throttle();
		if(hdgCurrent != hdgDesired || mkCurrent != mkDesired){
			turn();
		}
		if(mobOpsAct[ELEMENT.HUD_OPS_APR.getIndex()]){
			if(Calc.doesIntersect(loc, dir, destination.getArriveCoords(), destination.getLoc())){
				System.out.println("Calc.doesIntersect() returned true.");
				Coords intcpt = Calc.intersection(loc, dir, destination.getLoc(), destination.getArriveCoords());
				if(intcpt != null){
					System.out.println("Mobile intercepts localizer at Coords: " + intcpt.GetX() + ", " + intcpt.GetY() + ", " + intcpt.GetZ());
					int angle = (int)Calc.approachAngle(destination.getLoc(), intcpt, loc);
					if(angle <= 225 && angle >= 135){
						System.out.println("Established on localizer.");
						setFlightStatus(FSTATUS.ONAPR);
					}
				}
			}
		}
		loc.add(Calc.mVector(this));
		dir = Calc.dVector(this, Hud.getTimeProject());
	}
	
	public void call(Xmit xmit){
		hdgDesired = xmit.getHeading();
		mkDesired = xmit.getMark();
		speedDesired = xmit.getSpd();
		for(int i = 0; i < mobOpsAct.length; i++){
			mobOpsAct[i] = xmit.getOpsActive(i);
		}
		waypoint = xmit.getWaypoint();
	}
	
	public void contact(){
		setFlightStatus(FSTATUS.NORMAL);
		for(int i = 1; i < mobOps.length; i++){
			mobOps[i] = true;
		}
		mobOps[ELEMENT.HUD_OPS_CON.getIndex()] = false;
	}
	
	public Static getOrigin(){
		return origin;
	}
	
	public double getHdg(){
		return (double)hdgCurrent;
	}
	
	public double getMK(){
		return (double)mkCurrent;
	}
	
	@Deprecated
	public String getName(){
		return name;
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
	
	public String getSerial(){
		return this.serial;
	}
	
	/**
	 * Obtains the current speed of this Mobile.
	 * @return Current speed in m/s.
	 */
	public double getSpd(){
		return this.speedCurrent;
	}
	
	public MTYPE getMType(){
		return this.type;
	}
	
	/**
	 * Obtains the maximum speed of this Mobile.
	 * @return Maximum speed in m/s.
	 */
	public double getSpdMax(){
		return speedMax;
	}
	
	public Static getWaypoint(){
		if(waypoint == null) return null;
		else return waypoint;
	}
	
	public void setFlightStatus(FSTATUS status){
		this.status = status;
		canSelect = status.canSelect();
		if(this.isSelected) this.deselect();
	}
	
	public void setOps(int i, boolean value){
		if(i > mobOps.length - 1){
			System.out.println("WARN: setOps() index value out of range.");
			return;
		}
		else mobOps[i] = value;
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
		System.out.println("DEBUG: " + hdgCurrent + "." + mkCurrent);
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
		case CNFLCT:
			break;
		case EMGNCY:
			break;
		case HNDOFF:
			G.drawString(serial.substring(3, serial.length()), x, y);
			y+=fm.getAscent();
			G.drawString((int)this.getSpd()+"", x, y);
			break;
		case MISAPR:
			break;
		case NORMAL:
			G.drawString(name, x, y);
			y+=fm.getAscent();
			G.drawString((int)this.getHdg()+"."+(int)this.getMK(), x, y);
			y+=fm.getAscent();
			G.drawString((int)this.getSpd()+"", x, y);
			break;
		case ONAPR:
			break;
		default:
			break;		
		}
		G.setColor(PrevC);
		G.setFont(PrevF);
	}
	
	private void setFlightInfoColor(Graphics G){
		switch(status){
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
	
	private void updateHistory(){
		Coords temp = new Coords(loc.GetX(), loc.GetY(), loc.GetZ());
		history.add(0, temp);
		if(history.size() == 20)history.remove(19);
	}
	
}
