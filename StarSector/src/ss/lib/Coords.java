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
	
	public double GetX(){
		return x;
	}
	
	public double GetY(){
		return y;
	}
	
	public double GetZ(){
		return z;
	}
	
	public double[] GetCoords(){
		double temp[] = {x,y,z};
		return temp;
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
}
