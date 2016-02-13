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

	public static Coords convertCourseToCoords(Coords src, int brng, int mk, int dist){
		double vv[] = getP(brng, mk);
		double km = dist * StarSector.PPKM;
		double ex = km * vv[0];
		double ey = km * vv[1];
		double ez = km * vv[2];
		if(brng > 180) ex = -ex;
		if(brng > 270 || brng < 90) ey = -ey;
		if(mk < 90) ez = -ez;
		ex += src.GetX();
		ey += src.GetY();
		ez += src.GetZ();
		return new Coords(ex, ey, ez);
//		double x = src.GetX() + (dist * Math.sin(Math.toRadians(brng)));
//		double y = src.GetY() - (dist * Math.cos(Math.toRadians(brng)));
//		double z = src.GetZ() - (dist * Math.cos(Math.toRadians(mk)));
//		return new Coords(x,y,z);
	}
	
	/**
	 * Create a movement vector for a mobile.
	 * @param m	The mobile having a movement vector created.
	 * @return	The movement vector.
	 */
	public static Coords mVector(Mobile m){
		double spd = KMPS(m.getSpd());
		double vv[] = getP(m.getHdg(), m.getMK());
//		System.out.println("mVector: " + vv[0] + ", " + vv[1] + ", " + vv[2]);
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
			return pct;
		}
		else if(hdg == 360 || hdg == 180 || hdg == 0){
			pct[0] = 0;
			pct[1] = 1 - zpct;
			pct[2] = zpct;
			return pct;
		}
		else if(hdg == 90 || hdg == 270){
			pct[0] = 1 - zpct;
			pct[1] = 0;
			pct[2] = zpct;
			return pct;
		}

		else{
			zpct *= 0.666666666666666667;
			pct[2] = zpct;
			if(hdg > 270) i = hdg - 270;
			else if(hdg > 180) i = hdg - 180;
			else if(hdg > 90) i = hdg - 90;
			else i = hdg;
			if(hdg > 0 && hdg < 90 || hdg > 180 && hdg < 270){
				pct[0] = i/90 - pct[2]/2;
				pct[1] = 1 - i/90 - pct[2]/2;
			}
			else{
				pct[0] = 1 - i/90 - pct[2]/2;
				pct[1] = i/90 - pct[2]/2;
			}
		}
			return pct;
		}
}
