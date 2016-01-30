package ss.type;

import java.awt.Color;

import ss.StarSector;

public enum ELEMENT {
	
	HUD_OVW_XYSWP	(HUDMODE.OVERVIEW, RESPONSE.XY, 	Color.darkGray, Color.cyan,		StarSector.WIDTH - 32, 0, 32, 16),
	HUD_OVW_CLOCK 	(HUDMODE.OVERVIEW, RESPONSE.CLOCK,	Color.darkGray, Color.cyan,		0, 0, 80, 16),
	HUD_OPS 		(HUDMODE.OPS, RESPONSE.NULL,		Color.black, Color.darkGray,	0, 0, 128, 128),
	HUD_OPS_XMIT 	(HUDMODE.OPS, RESPONSE.XMIT,		Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_OPS_DISR 	(HUDMODE.OPS, RESPONSE.DISR,		Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_OPS_HDG 	(HUDMODE.OPS, RESPONSE.HDG, 		Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_OPS_SPD 	(HUDMODE.OPS, RESPONSE.SPD,			Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_OPS_MVS 	(HUDMODE.OPS, RESPONSE.MVS,			Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_OPS_DCT 	(HUDMODE.OPS, RESPONSE.DCT,			Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_OPS_APR 	(HUDMODE.OPS, RESPONSE.APRCLR,		Color.darkGray, Color.green,	0, 0, 128, 128),
	HUD_INP			(HUDMODE.INPUT, RESPONSE.NULL,		Color.black, Color.darkGray,	StarSector.WIDTH / 2 - 128 / 2, StarSector.HEIGHT / 2 - 160 / 2, 128,168),
	HUD_INP_ONE		(HUDMODE.INPUT, RESPONSE.ONE,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP.getY() + 8, 32, 32),
	HUD_INP_TWO		(HUDMODE.INPUT, RESPONSE.TWO,		Color.darkGray, Color.green,	HUD_INP_ONE.getX() + 40, HUD_INP_ONE.getY(), 32, 32),
	HUD_INP_THR		(HUDMODE.INPUT, RESPONSE.THREE,		Color.darkGray, Color.green,	HUD_INP_TWO.getX() + 40, HUD_INP_ONE.getY(), 32, 32),
	HUD_INP_FOU		(HUDMODE.INPUT, RESPONSE.FOUR,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP_ONE.getY() + 40, 32, 32),
	HUD_INP_FIV		(HUDMODE.INPUT, RESPONSE.FIVE,		Color.darkGray, Color.green,	HUD_INP_FOU.getX() + 40, HUD_INP_FOU.getY(), 32, 32),
	HUD_INP_SIX		(HUDMODE.INPUT, RESPONSE.SIX,		Color.darkGray, Color.green,	HUD_INP_FIV.getX() + 40, HUD_INP_FIV.getY(), 32, 32),
	HUD_INP_SEV		(HUDMODE.INPUT, RESPONSE.SEVEN,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP_FOU.getY() + 40, 32, 32),
	HUD_INP_EIG		(HUDMODE.INPUT, RESPONSE.EIGHT,		Color.darkGray, Color.green,	HUD_INP_SEV.getX() + 40, HUD_INP_SEV.getY(), 32, 32),
	HUD_INP_NIN		(HUDMODE.INPUT, RESPONSE.NINE,		Color.darkGray, Color.green,	HUD_INP_EIG.getX() + 40, HUD_INP_EIG.getY(), 32, 32),
	HUD_INP_ACP		(HUDMODE.INPUT, RESPONSE.ACPT,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP_SEV.getY() + 40, 32, 32),
	HUD_INP_MRK		(HUDMODE.INPUT, RESPONSE.MARK,		Color.darkGray, Color.green,	HUD_INP_ACP.getX() + 40, HUD_INP_ACP.getY(), 32, 32),
	HUD_INP_CAN		(HUDMODE.INPUT, RESPONSE.CNCL,		Color.darkGray, Color.green,	HUD_INP_MRK.getX() + 40, HUD_INP_MRK.getY(), 32, 32);
	
	private int x;
	private int y;
	private int w;
	private int h;
	Color primary;
	Color secondary;
	HUDMODE mode;
	RESPONSE response;
	
	ELEMENT(HUDMODE mode, RESPONSE response, Color primary, Color secondary, int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.mode = mode;
		this.response = response;
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public void setResponse(RESPONSE r){
		this.response = r;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getW(){
		return w;
	}
	
	public int getH(){
		return h;
	}
	
	public Color getColorPrimary(){
		return primary;
	}
	
	public Color getColorSecondary(){
		return secondary;
	}
	
	public HUDMODE getMode(){
		return mode;
	}
	
	public RESPONSE getResponse(){
		return response;
	}
}
