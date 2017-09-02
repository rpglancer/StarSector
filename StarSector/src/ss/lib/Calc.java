package ss.lib;

import java.awt.geom.Line2D;

import ss.engine.StarSector;

public class Calc {
	
	public static int reciprocal(int hdg){
		if(hdg >=180)
			return hdg - 200 + 20;
		else
			return hdg + 200 - 20;
	}
	
	public static double approachAngle(Coords station, Coords intercept, Coords ship){
		double a = distanceKM(intercept, ship);
		double b = distanceKM(station, ship);
		double c = distanceKM(station, intercept);
		
		double B = (a*a + c*c - b*b) / (2*a*c);
		double alpha = (Math.acos(B));
		System.out.println("Current interception angle: " + Math.toDegrees(alpha));
		return Math.toDegrees(alpha);
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
	}
	
	public static int convertCoordsToHdg(Coords src, Coords tgt){
		double angle = (double)Math.toDegrees(Math.atan2(tgt.GetY() - src.GetY(), tgt.GetX() - src.GetX()));
		if(angle < 0)
			angle += 360;
		angle += 90;
		if(angle >= 360)
			angle -= 360;
		return (int)angle;
	}
	
	public static int convertCoordsToMk(Coords src, Coords tgt){
		double angle = (double)Math.toDegrees(Math.atan2(tgt.GetZ() - src.GetZ(), tgt.GetX() - src.GetX()));
		if(angle < 0)
			angle += 360;
		angle += 90;
		if(angle >= 360)
			angle -= 360;
		if(angle > 180){
			double temp = 180 - (angle - 180);
			return (int)temp;
		}
		else return (int)angle;
	}
	
	public static double distanceKM(Coords src, Coords tgt){
		double a = src.GetX() - tgt.GetX();
		double b = src.GetY() - tgt.GetY();
		double c = Math.sqrt(a * a + b * b);
		double z = src.GetZ() - tgt.GetZ();
		return Math.sqrt(c * c + z * z) / StarSector.PPKM;
	}
	
	public static boolean doesIntersect(Coords src1, Coords src2, Coords tgt1, Coords tgt2){
		Line2D srcLine = new Line2D.Double(src1.GetX(), src1.GetY(), src2.GetX(), src2.GetY());
		Line2D tgtLine = new Line2D.Double(tgt1.GetX(), tgt1.GetY(), tgt2.GetX(), tgt2.GetY());
		return srcLine.intersectsLine(tgtLine);
	}
	
	public static Coords interection(Line2D srcXY, Line2D srcXZ, Line2D tgtXY, Line2D tgtXZ){
		if(!srcXY.intersectsLine(tgtXY) || !srcXZ.intersectsLine(tgtXZ))
			return null;
		int x1 = (int)srcXY.getX1();
		int x2 = (int)srcXY.getX2();
		int y1 = (int)srcXY.getY1();
		int y2 = (int)srcXY.getY2();	
		int x3 = (int)tgtXY.getX1();
		int x4 = (int)tgtXY.getX2();
		int y3 = (int)tgtXY.getY1();
		int y4 = (int)tgtXY.getY2();
		
		int d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		System.out.println("d: " + d);
		if(d == 0) return null;
		int xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		int yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		return new Coords(xi,yi,0);
	}
	
//	p13 = new Coords();
//	p43 = new Coords();
//	p21 = new Coords();
//	p13.SetX(src1.GetX() - tgt1.GetX());
//	p13.SetY(src1.GetY() - tgt1.GetY());
//	p13.SetZ(src1.GetZ() - tgt1.GetZ());
//	p43.SetX(tgt2.GetX() - tgt1.GetX());
//	p43.SetY(tgt2.GetY() - tgt1.GetY());
//	p43.SetZ(tgt2.GetZ() - tgt1.GetZ());
//	p21.SetX(src2.GetX() - src1.GetX());
//	p21.SetY(src2.GetY() - src1.GetY());
//	p21.SetZ(src2.GetZ() - src1.GetZ());
//	Coords pa = new Coords();
//	pa.SetX(p1.GetX() + mua * p21.GetX());
//	pa.SetY(p1.GetY() + mua * p21.GetY());
//	pa.SetZ(p1.GetZ() + mua * p21.GetZ());
//	Coords pb = new Coords();
//	pb.SetX(p3.GetX() + mub * p43.GetX());
//	pb.SetY(p3.GetY() + mub * p43.GetY());
//	pb.SetZ(p3.GetZ() + mub * p43.GetZ());

	/**
	 * Calculate the line segment PaPb that is the shortest route between
	 * two lines P1P2 and P3P4. Calculate also the values of mua
	 * and mub where<br>
	 * Pa = P1 + mua (P2 - P1)<br>
	 * Pb = P3 + mub (P4 - P3)
	 * @param p1 Point 1 (Line 1 start)
	 * @param p2 Point 2 (Line 1 end)
	 * @param p3 Point 3 (Line 2 start)
	 * @param p4 Point 4 (Line 2 end)
	 * @return null for now ;_;
	 */
	public static Coords intersection(Coords p1, Coords p2, Coords p3, Coords p4){
		Coords p13, p43, p21;
		double d1343, d4321, d1321, d4343, d2121;
		double numer, denom;
		double EPS = 2.2204460492503131E-16;

		p13 = new Coords(p1.GetX() - p3.GetX(), p1.GetY() - p3.GetY(), p1.GetZ() - p3.GetZ());
		p43 = new Coords(p4.GetX() - p3.GetX(), p4.GetY() - p3.GetY(), p4.GetZ() - p3.GetZ());

		if(Math.abs(p43.GetX()) < EPS && Math.abs(p43.GetY()) < EPS && Math.abs(p43.GetZ()) < EPS){
			return null;
		}

		p21 = new Coords(p2.GetX() - p1.GetX(), p2.GetY() - p1.GetY(), p2.GetZ() - p1.GetZ());

		if(Math.abs(p21.GetX()) < EPS && Math.abs(p21.GetY()) < EPS && Math.abs(p21.GetZ()) < EPS){
			return null;
		}
		
		d1343 = p13.GetX() * p43.GetX() + p13.GetY() * p43.GetY() + p13.GetZ() * p43.GetZ();
		d4321 = p43.GetX() * p21.GetX() + p43.GetY() * p21.GetY() + p43.GetZ() * p21.GetZ();
		d1321 = p13.GetX() * p21.GetX() + p13.GetY() * p21.GetY() + p13.GetZ() * p21.GetZ();
		d4343 = p43.GetX() * p43.GetX() + p43.GetY() * p43.GetY() + p43.GetZ() * p43.GetZ();
		d2121 = p21.GetX() * p21.GetX() + p21.GetY() * p21.GetY() + p21.GetZ() * p21.GetZ();
		
		denom = d2121 * d4343 - d4321 * d4321;
		if(Math.abs(denom) < EPS)
			return null;
		numer = d1343 * d4321 - d1321 * d4343;
		double mua = numer/denom;
		double mub = (d1343 + d4321 * mua) / d4343;
		Coords pa = new Coords(p1.GetX() + mua * p21.GetX(), p1.GetY() + mua * p21.GetY(), p1.GetZ() + mua * p21.GetZ());
		Coords pb = new Coords(p3.GetX() + mub * p43.GetX(), p3.GetY() + mub * p43.GetY(), p3.GetZ() + mub * p43.GetZ());

		System.out.println("PA: " + pa.GetX() + ", " + pa.GetY() + ", " + pa.GetZ());
		System.out.println("PB: " + pb.GetX() + ", " + pb.GetY() + ", " + pb.GetZ());
		if(pa.GetX() == pb.GetX() && pa.GetY() == pb.GetY() && pa.GetZ() == pb.GetZ()){
			System.out.println("Absolute intersection found.");
			return pa;
		}
		return null;
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
	public static double[] getP(double hdg, double mk){
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
