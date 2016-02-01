package ss.lib;

@Deprecated
/**
 * Euclidean Vector
 * @author Matt Bangert
 *
 */
public class EVector {
	private double x;
	private double y;
	private double z;
	
	/**
	 * Creates a new Euclidean vector with the specified parameters.
	 * @param x	Speed/location across/on the X axis.
	 * @param y	Speed/location across/on the Y axis.
	 * @param z	Speed/location across/on the Z axis.
	 */
	EVector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void add(EVector v){
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void sub(EVector v){
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public void set(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
}
