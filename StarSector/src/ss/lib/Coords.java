package ss.lib;

/**
 * Class containing x,y,z coordinate values as well as some methods to modify
 * those values and fish out their contents. Also very useful as a movement vector.
 * Try to keep this class limited in size so as to make it cheap when used.
 * @author Matt Bangert
 *
 */
public class Coords {
	private double x;
	private double y;
	private double z;
	
	public Coords(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double GetX(){
		return x;
	}
	
	public double GetY(){
		return y;
	}
	
	public double GetZ(){
		return z;
	}

	@Deprecated
	public double[] GetCoords(){
		double temp[] = {x,y,z};
		return temp;
	}
	
	public void SetCoords(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Deprecated
	public void SetX(double x){
		this.x = x;
	}
	
	@Deprecated
	public void SetY(double y){
		this.y = y;
	}
	
	@Deprecated
	public void SetZ(double z){
		this.z = z;
	}
	
	/**
	 * Modify the value of these Coords values by the Coords values specified.
	 * @param v	Coords values to be added.
	 */
	public void add(Coords v){
		x += v.x;
		y += v.y;
		z += v.z;
	}
}
