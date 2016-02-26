package ss.type;

import java.awt.Color;

import ss.lib.Mobile;

//	EnumID	Text	ColorAvail	 ColorNotAvail
public enum RESPONSE {
	NEWG	("NEW",		Color.green, Color.red),
	CONT	("CONTINUE",Color.green, Color.red),
	EXIT	("EXIT", 	Color.green, Color.red),
	MAPSEL	("\0", 		Color.green, Color.red),
	NULL	("\0",		Color.black, Color.black),
	ZER		("0",		Color.green, Color.darkGray),
	ONE		("1",		Color.green, Color.darkGray),
	TWO		("2",		Color.green, Color.darkGray),
	THREE	("3",		Color.green, Color.darkGray),
	FOUR	("4",		Color.green, Color.darkGray),
	FIVE	("5",		Color.green, Color.darkGray),
	SIX		("6",		Color.green, Color.darkGray),
	SEVEN	("7",		Color.green, Color.darkGray),
	EIGHT	("8",		Color.green, Color.darkGray),
	NINE	("9",		Color.green, Color.darkGray),
	MARK	(".",		Color.green, Color.darkGray),
	MVS 	("MVS",		Color.green, Color.red),
	DCT		("DCT",		Color.green, Color.red),
	APRCLR	("APR",		Color.green, Color.red),
	SPD		("SPEED",	Color.green, Color.red),
	HDG		("HEADING",	Color.green, Color.red),
	HOLD	("HOLD",	Color.green, Color.red),
	XMIT	("XMIT",	Color.green, Color.red),
	DISR	("DISREG",	Color.green, Color.red),
	CTCT	("CONTACT",	Color.green, Color.red),
	ACPT	("ACPT",	Color.green, Color.darkGray),
	CNCL	("CNCL",	Color.green, Color.darkGray),
	RULER	("ruler",	Color.green, Color.red),
	XY		("x/y",		Color.cyan, Color.cyan),
	XZ		("x/z",		Color.cyan, Color.cyan),
	CLOCK	("\0",		Color.cyan, Color.cyan),
	ENTITY	("\0",		Color.cyan,	Color.cyan),
	OPS		("ops",		Color.green, Color.yellow),
	ORIGIN	("Origin:",	Color.green, Color.green),
	DEST	("Dest.:",	Color.green, Color.green),
	CURRENT	("Current", Color.green, Color.green),
	DESIRED ("Desired", Color.green, Color.green);
	
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
