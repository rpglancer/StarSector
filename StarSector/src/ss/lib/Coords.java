package ss.lib;

public class Coords {
	private double x;
	private double y;
	private double z;
	
	public Coords(double x, double y, double z){
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
	
	public double[] getCoords(){
		double temp[] = {x,y,z};
		return temp;
	}
	
	public void setCoords(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setZ(double z){
		this.z = z;
	}
}
