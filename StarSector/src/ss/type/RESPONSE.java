package ss.type;

public enum RESPONSE {
	NULL	("\0"),
	ZER		("0"),
	ONE		("1"),
	TWO		("2"),
	THREE	("3"),
	FOUR	("4"),
	FIVE	("5"),
	SIX		("6"),
	SEVEN	("7"),
	EIGHT	("8"),
	NINE	("9"),
	MARK	("."),
	MVS		("MVS"),
	DCT		("DCT"),
	APRCLR	("APRCLR"),
	HOLD	("HOLD"),
	CONFIRM	("CONFIRM"),
	CANCEL	("CANCEL"),
	XY		("x/y"),
	XZ		("x/z");
	
	private String text;
	
	RESPONSE(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
}
