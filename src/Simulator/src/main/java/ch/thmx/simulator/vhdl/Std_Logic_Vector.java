package ch.thmx.simulator.vhdl;

import java.util.Arrays;

public class Std_Logic_Vector {

	private Std_Logic[] vector;
	
	public Std_Logic_Vector(Std_Logic... bits) {
		this.vector = bits;
	}
	
	public Std_Logic_Vector(int value, int len) {
		String hexValue = Integer.toBinaryString(value);
		
		while (hexValue.length() < len) {
			hexValue = "0" + hexValue;			
		}
		
		setValue(hexValue);
	}
	
	public Std_Logic_Vector(String value) {
		setValue(value);
	}
	
	public void setValue(String value) {
		this.vector = new Std_Logic[value.length()];
		int i=0;
		for (char c: value.toCharArray()) {
			this.vector[i++] = Std_Logic.fromValue(c);
		}
	}
	
	public Std_Logic[] getValue() {
		return Arrays.copyOf(this.vector, this.vector.length);
	}
	
	public boolean isValid() {
		if (this.vector == null) {
			return false;
		}
		
		for (Std_Logic b: this.vector) {
			if (!b.isValid()) {
				return false;
			}
		}
		
		return true;
	}
	
	public int unsignedValue() {
		// FIXME convert to unsigned int
		try {
			return Integer.parseInt(toString(), 2);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public int signedValue() {
		// TODO convert to signed int
		return 0;
	}
	
	public String hexValue() {
		try {
			return Integer.toHexString(Integer.parseInt(toString(), 2)).toUpperCase();
		} catch (NumberFormatException e) {
			return "";
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Std_Logic sl: this.vector) {
			builder.append(sl.value());
		}
		return builder.toString();
	}
}
