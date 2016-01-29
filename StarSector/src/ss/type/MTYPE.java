package ss.type;

public enum MTYPE {
	M0 ("M0", 0, 0),
	M1 ("M0", 0, 0),
	M2 ("M0", 0, 0),
	M3 ("M0", 0, 0),
	M4 ("M0", 0, 0),
	M5 ("M0", 0, 0),
	M6 ("M0", 0, 0),
	M7 ("M0", 0, 0),
	TS ("M0", 0, 0),
	TM ("M0", 0, 0),
	TL ("M0", 0, 0),
	TP ("M0", 0, 0);
	
	private String type;
	private int spriteR;
	private int spriteC;
	
	MTYPE(String type, int spriteR, int spriteC){
		this.type = type;
		this.spriteR = spriteR;
		this.spriteC = spriteC;
	}
	
	int getSpriteR(){
		return this.spriteR;
	}
	
	int getSpriteC(){
		return this.spriteC;
	}
	
	String getType(){
		return this.type;
	}
	

}
