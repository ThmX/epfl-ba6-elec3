package ch.thmx.simulator.vhdl;

public enum Std_Logic {
	
	STD_LOGIC_U('U'),
	STD_LOGIC_X('X'),
	STD_LOGIC_0('0'),
	STD_LOGIC_1('1'),
	STD_LOGIC_Z('Z'),
	STD_LOGIC_W('W'),
	STD_LOGIC_L('L'),
	STD_LOGIC_H('H'),
	STD_LOGIC_D('D');
	
	private final char value;
	
	Std_Logic(char v) {
        this.value = v;
    }
	
	@Override
	public String toString() {
		return String.valueOf(value());
	}

    public char value() {
        return this.value;
    }
    
    public static Std_Logic fromValue(boolean bool) {
        return bool ? STD_LOGIC_1: STD_LOGIC_0;
    }

    public static Std_Logic fromValue(char v) {
        for (Std_Logic c: Std_Logic.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown value '" + v + "'.");
    }
    
    public boolean boolValue() {
    	return Std_Logic.STD_LOGIC_1.equals(this) || Std_Logic.STD_LOGIC_H.equals(this);
    }
    
    public boolean isValid() {
    	switch (this) {
    		case STD_LOGIC_0:
    		case STD_LOGIC_1:
    		case STD_LOGIC_L:
    		case STD_LOGIC_H:
   				return true;
   				
    		case STD_LOGIC_U:
    		case STD_LOGIC_X:
    		case STD_LOGIC_Z:
    		case STD_LOGIC_W:
    		case STD_LOGIC_D:
    		default:
    			return false;
    	}
    }
    
    public Std_Logic_Vector vector() {
    	return new Std_Logic_Vector(this);
    }
}
