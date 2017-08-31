package ss.type;

public enum FSTATUS {
	ARIDEP  (false),
	HNDOFF	(true),
	NORMAL	(true),
	ONAPR	(false),
	MISAPR	(false),
	EMGNCY	(true),
	CNFLCT	(true);
	
	private boolean canSelect;
	
	FSTATUS(boolean canSelect){
		this.canSelect = canSelect;
	}
	
	public boolean canSelect(){
		return this.canSelect;
	}
	
}
