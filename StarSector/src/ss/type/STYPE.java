package ss.type;

// Type, Sprite row, Sprite Column
public enum STYPE {
	FIX ("Fix", 0, 0),
	GATE ("Gate", 0, 0),
	STATION ("Station", 1, 1);
	
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
