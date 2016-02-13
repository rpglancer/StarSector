package ss.type;

/**
 * Static Types.<br>
 * An enumerator which contains [most] of the relevant data for Static Entities.<br>
 * <b>EnumID</b><br>
 * <b>(String)</b>TypeName<br>
 * <b>(int)</b>SpriteRow<br>
 * <b>(int)</b>SpriteColumn
 * @author Matt Bangert
 *
 */
public enum STYPE {
//	enumID	Name	   sR  sC
	FIX 	("Fix", 	2, 1),
	GATE 	("Gate", 	2, 4),
	STATION ("Station", 3, 1);
	
	private String type;
	private int sR;
	private int sC;
	
	STYPE(String type, int sR, int sC){
		this.type = type;
		this.sR = sR;
		this.sC = sC;
	}
	
	public int getSpriteR(){
		return sR;
	}
	
	public int getSpriteC(){
		return sC;
	}
	
	public String getType(){
		return type;
	}
	
}
