package ss.type;

import java.awt.Color;

import ss.StarSector;
//														Fill			Border
//	EnumID			i	HUDMODE			RESPONSE		ColorAvail		ColorNotAvail	Coordinates
public enum ELEMENT {
	HUD_OVW_RULER	(0, HUDMODE.OVERVIEW, RESPONSE.RULER,	Color.darkGray, Color.darkGray,	StarSector.WIDTH - 64, 0, 32, 16),
	HUD_OVW_XYSWP	(1, HUDMODE.OVERVIEW, RESPONSE.XY, 		Color.darkGray, Color.darkGray,	StarSector.WIDTH - 32, 0, 32, 16),
	HUD_OVW_CLOCK 	(2, HUDMODE.OVERVIEW, RESPONSE.CLOCK,	Color.darkGray, Color.darkGray,	0, 0, 80, 16),
	HUD_OVW_ENTITY	(3, HUDMODE.OVERVIEW, RESPONSE.ENTITY,	Color.darkGray, Color.darkGray,	80, 0, 80, 16),
	HUD_OVW_OPS		(4, HUDMODE.OVERVIEW, RESPONSE.OPS,		Color.darkGray,	Color.darkGray,	160, 0, 32, 16),
	HUD_OPS 		(0, HUDMODE.OPS, RESPONSE.NULL,			Color.black, Color.darkGray,	160, 16, 128, 80),
	HUD_OPS_HDG 	(1, HUDMODE.OPS, RESPONSE.HDG, 			Color.darkGray, Color.green,	HUD_OPS.getX() + 8, 24, 52, 16),
	HUD_OPS_SPD 	(2, HUDMODE.OPS, RESPONSE.SPD,			Color.darkGray, Color.green,	HUD_OPS_HDG.getX() + 60, 24, 52, 16),
	HUD_OPS_MVS 	(3, HUDMODE.OPS, RESPONSE.MVS,			Color.darkGray, Color.green,	HUD_OPS.getX() + 8, HUD_OPS_HDG.getY() + 24, 32, 16),
	HUD_OPS_DCT 	(4, HUDMODE.OPS, RESPONSE.DCT,			Color.darkGray, Color.green,	HUD_OPS_MVS.getX() + 40, HUD_OPS_MVS.getY(), 32, 16),
	HUD_OPS_APR 	(5, HUDMODE.OPS, RESPONSE.APRCLR,		Color.darkGray, Color.green,	HUD_OPS_DCT.getX() + 40, HUD_OPS_DCT.getY(), 32, 16),
	HUD_OPS_XMT 	(6, HUDMODE.OPS, RESPONSE.XMIT,			Color.darkGray, Color.green,	HUD_OPS.getX() + 8, HUD_OPS_APR.getY() + 24, 52, 16),
	HUD_OPS_DIS 	(7, HUDMODE.OPS, RESPONSE.DISR,			Color.darkGray, Color.green,	HUD_OPS_XMT.getX() + 60, HUD_OPS_XMT.getY(), 52, 16),
	HUD_INP			(0, HUDMODE.INPUT, RESPONSE.NULL,		Color.black, Color.darkGray,	StarSector.WIDTH / 2 - 128 / 2, StarSector.HEIGHT / 2 - 160 / 2, 128,168),
	HUD_INP_ONE		(1, HUDMODE.INPUT, RESPONSE.ONE,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP.getY() + 8, 32, 32),
	HUD_INP_TWO		(2, HUDMODE.INPUT, RESPONSE.TWO,		Color.darkGray, Color.green,	HUD_INP_ONE.getX() + 40, HUD_INP_ONE.getY(), 32, 32),
	HUD_INP_THR		(3, HUDMODE.INPUT, RESPONSE.THREE,		Color.darkGray, Color.green,	HUD_INP_TWO.getX() + 40, HUD_INP_ONE.getY(), 32, 32),
	HUD_INP_FOU		(4, HUDMODE.INPUT, RESPONSE.FOUR,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP_ONE.getY() + 40, 32, 32),
	HUD_INP_FIV		(5, HUDMODE.INPUT, RESPONSE.FIVE,		Color.darkGray, Color.green,	HUD_INP_FOU.getX() + 40, HUD_INP_FOU.getY(), 32, 32),
	HUD_INP_SIX		(6, HUDMODE.INPUT, RESPONSE.SIX,		Color.darkGray, Color.green,	HUD_INP_FIV.getX() + 40, HUD_INP_FIV.getY(), 32, 32),
	HUD_INP_SEV		(7, HUDMODE.INPUT, RESPONSE.SEVEN,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP_FOU.getY() + 40, 32, 32),
	HUD_INP_EIG		(8, HUDMODE.INPUT, RESPONSE.EIGHT,		Color.darkGray, Color.green,	HUD_INP_SEV.getX() + 40, HUD_INP_SEV.getY(), 32, 32),
	HUD_INP_NIN		(9, HUDMODE.INPUT, RESPONSE.NINE,		Color.darkGray, Color.green,	HUD_INP_EIG.getX() + 40, HUD_INP_EIG.getY(), 32, 32),
	HUD_INP_ACP		(10, HUDMODE.INPUT, RESPONSE.ACPT,		Color.darkGray, Color.green,	HUD_INP.getX() + 8, HUD_INP_SEV.getY() + 40, 32, 32),
	HUD_INP_MRK		(11, HUDMODE.INPUT, RESPONSE.MARK,		Color.darkGray, Color.green,	HUD_INP_ACP.getX() + 40, HUD_INP_ACP.getY(), 32, 32),
	HUD_INP_CAN		(12, HUDMODE.INPUT, RESPONSE.CNCL,		Color.darkGray, Color.green,	HUD_INP_MRK.getX() + 40, HUD_INP_MRK.getY(), 32, 32);
	
	private int i;
	private int x;
	private int y;
	private int w;
	private int h;
	Color primary;
	Color secondary;
	HUDMODE mode;
	RESPONSE response;
	
	ELEMENT(int index, HUDMODE mode, RESPONSE response, Color primary, Color secondary, int x, int y, int w, int h){
		i = index;
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
	
	public int getIndex(){
		return i;
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
