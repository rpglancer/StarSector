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
	
	public Coords(){
		
	}
	
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
	
	public void SetCoords(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void SetX(double x){
		this.x = x;
	}
	
	public void SetY(double y){
		this.y = y;
	}
	
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
	
	public void sub(Coords v){
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public void multiply(Coords v){
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}
	
	public void divide(Coords v){
		x /= v.x;
		y /= v.y;
		z /= v.z;
	}
}
