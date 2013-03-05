package ch.thmx.simulator.vhdl;

public class Signal {
	
	private String name;
	private Std_Logic_Vector vector;
	
	public Signal(String n, String v) {
		this.name = n;
		setValue(v);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Std_Logic_Vector getVector() {
		return this.vector;
	}
	
	public void setValue(String v) {
		this.vector = new Std_Logic_Vector(v);
	}
	
	@Override
	public String toString() {
		return "Signal [name=" + this.name + ", signal=" + this.vector + "]";
	}
}
