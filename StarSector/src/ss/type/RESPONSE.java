package ss.type;

import java.awt.Color;

public enum RESPONSE {
	NULL	("\0",	Color.black, Color.black),
	ZER		("0",	Color.green, Color.darkGray),
	ONE		("1",	Color.green, Color.darkGray),
	TWO		("2",	Color.green, Color.darkGray),
	THREE	("3",	Color.green, Color.darkGray),
	FOUR	("4",	Color.green, Color.darkGray),
	FIVE	("5",	Color.green, Color.darkGray),
	SIX		("6",	Color.green, Color.darkGray),
	SEVEN	("7",	Color.green, Color.darkGray),
	EIGHT	("8",	Color.green, Color.darkGray),
	NINE	("9",	Color.green, Color.darkGray),
	MARK	(".",	Color.green, Color.darkGray),
	MVS		("MVS",	Color.green, Color.darkGray),
	DCT		("DCT",	Color.green, Color.darkGray),
	APRCLR	("APRCLR",	Color.green, Color.darkGray),
	SPD		("SPEED",	Color.green, Color.darkGray),
	HDG		("HEADING",	Color.green, Color.darkGray),
	HOLD	("HOLD",	Color.green, Color.darkGray),
	XMIT	("XMIT",	Color.green, Color.darkGray),
	DISR	("DISREG",	Color.green, Color.darkGray),
	ACPT	("ACPT",	Color.green, Color.darkGray),
	CNCL	("CNCL",	Color.green, Color.darkGray),
	XY		("x/y",		Color.cyan, Color.cyan),
	XZ		("x/z",		Color.cyan, Color.cyan),
	CLOCK	("\0",		Color.cyan, Color.cyan);
	
	private String text;
	private Color primary;
	private Color secondary;
	
	RESPONSE(String text, Color primary, Color secondary){
		this.text = text;
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public String getText(){
		return text;
	}
	
	public Color getColorPri(){
		return primary;
	}
	
	public Color getColorSec(){
		return secondary;
	}
	
}
