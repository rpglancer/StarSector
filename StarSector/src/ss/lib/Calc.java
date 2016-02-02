package ss.lib;

import ss.StarSector;

public class Calc {
	
	public static int reciprocal(int hdg){
		if(hdg >=180)
			return hdg - 200 + 20;
		else
			return hdg + 200 - 20;
	}
	
	/**
	 * Converts a mark value [0 - 180] into an effective pitch value [-90 - 90].
	 * @param mk	The mark value of a given heading.
	 * @return	<b>double</b> The converted pitch value.
	 */
	private static double convertMark(double mk){
		return 90 - mk;
	}
	
	/**
	 * Create a movement vector for a mobile.
	 * @param m	The mobile having a movement vector created.
	 * @return	The movement vector.
	 */
	public static Coords mVector(Mobile m){
		double spd = KMPS(m.getSpd());
		double vv[] = getP(m.getHdg(), m.getMK());
		double ex = spd * vv[0];
		double ey = spd * vv[1];
		double ez = spd * vv[2];
		if(m.getHdg() > 180) ex = -ex;
		if(m.getHdg() > 270 || m.getHdg() < 90) ey = -ey;
		if(m.getMK() < 90) ez = -ez;
		ex *= StarSector.PPKM;
		ey *= StarSector.PPKM;
		ez *= StarSector.PPKM;
		return new Coords(ex, ey, ez);
	}
	
	/**
	 * Create a directional vector for projecting a course visually.
	 * @param m		The mobile having a directional vector created.
	 * @param mins	The number of minutes for which to calculate the vector.
	 * @return		<b>Coords</b> The projected coordinates of a mobile.
	 */
	public static Coords dVector(Mobile m, int mins){
		double spd = KMPS(m.getSpd());
		spd *= 10;
		spd *= mins;
		double vv[] = getP(m.getHdg(), m.getMK());
		double ex = spd * vv[0];
		double ey = spd * vv[1];
		double ez = spd * vv[2];
		if(m.getHdg() > 180) ex = -ex;
		if(m.getHdg() > 270 || m.getHdg() < 90) ey = -ey;
		if(m.getMK() < 90) ez = -ez;
		ex *= StarSector.PPKM;
		ey *= StarSector.PPKM;
		ez *= StarSector.PPKM;
		ex += m.loc.GetX();
		ey += m.loc.GetY();
		ez += m.loc.GetZ();
		return new Coords(ex, ey, ez);
	}
	
	/**
	 * Calculate the Kilometers Moved Per Sweep of the radar.
	 * @param spd	The speed of the object (in m/s).
	 * @return	<b>Double</b> Kilometers per Sweep.
	 */
	public static double KMPS(double spd){
//		System.out.println("Calculated KMPS: " + (spd / 1000 * StarSector.SweepLength));
		return (spd / 1000) * StarSector.SweepLength;
	}
	
	/**
	 * Get percentages of thrust applied to each axis based upon a marked heading.
	 * @param hdg	The heading of the object in question [0-359]
	 * @param mk	The inclination of the object's heading [0-180]
	 * @return	<b>double[]</b> Array containing the percentages of trust applied to each axis.
	 */
	private static double[] getP(double hdg, double mk){
		double pct[] = {0,0,0};
		double i = 0;
		double zpct = Math.abs(convertMark(mk) / 90);
		if(zpct == 1){
			pct[0] = pct[1] = 0;
			pct[2] = 1;
//			System.out.println("Calculated Pcts: " + pct[0] + ", " + pct[1] + ", " + pct[2]);
//			System.out.println("Calculated PctTot: " + (pct[0]+pct[1]+pct[2]));
			return pct;
		}
		else if(hdg == 360 || hdg == 180 || hdg == 0){
			pct[0] = 0;
			pct[1] = 1 - zpct;
			pct[2] = zpct;
//			System.out.println("Calculated Pcts: " + pct[0] + ", " + pct[1] + ", " + pct[2]);
//			System.out.println("Calculated PctTot: " + (pct[0]+pct[1]+pct[2]));
			return pct;
		}
		else if(hdg == 90 || hdg == 270){
			pct[0] = 1 - zpct;
			pct[1] = 0;
			pct[2] = zpct;
//			System.out.println("Calculated Pcts: " + pct[0] + ", " + pct[1] + ", " + pct[2]);
//			System.out.println("Calculated PctTot: " + (pct[0]+pct[1]+pct[2]));
			return pct;
		}
		else{
			for(i = hdg + 360; i > 90; i -= 90);
			zpct *= 0.666666666666;
			pct[0] = i / 90;
			pct[1] = 1 - i / 90;
			pct[2] = zpct;
			pct[0] -= (pct[2] / 2);
			pct[1] -= (pct[2] / 2);
//			System.out.println("Calculated Pcts: " + pct[0] + ", " + pct[1] + ", " + pct[2]);
//			System.out.println("Calculated PctTot: " + (pct[0]+pct[1]+pct[2]));
			return pct;
		}
	}
}
