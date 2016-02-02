package ss.type;

//	ID	Txt	 sRow sCol
public enum MTYPE {
	M0 ("M0", 1, 1),
	M1 ("M1", 1, 2),
	M2 ("M2", 1, 3),
	M3 ("M3", 1, 4),
	M4 ("M4", 1, 5),
	M5 ("M5", 1, 6),
	M6 ("M6", 1, 7),
	M7 ("M7", 1, 8),
	TS ("TS", 1, 1),
	TM ("TM", 1, 1),
	TL ("TL", 1, 1),
	TP ("TP", 1, 1);
	
	private String type;
	private int spriteR;
	private int spriteC;
	
	MTYPE(String type, int spriteR, int spriteC){
		this.type = type;
		this.spriteR = spriteR;
		this.spriteC = spriteC;
	}
	
	public int getSpriteR(){
		return this.spriteR;
	}
	
	public int getSpriteC(){
		return this.spriteC;
	}
	
	public String getType(){
		return this.type;
	}
	

}
