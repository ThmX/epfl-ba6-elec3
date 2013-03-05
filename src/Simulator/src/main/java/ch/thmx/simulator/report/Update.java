package ch.thmx.simulator.report;

import java.util.ArrayList;
import java.util.List;

import ch.thmx.simulator.vhdl.Std_Logic_Vector;

public class Update extends Report {

	private List<Std_Logic_Vector> vectors;
	
	public Update(String message) {
		super(ReportType.Update);
		
		this.vectors = new ArrayList<Std_Logic_Vector>();
		for (String v: message.split(" ")) {
			this.vectors.add(new Std_Logic_Vector(v));
		}
	}

	@Override
	public void accept(ReportVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Update [vectors=" + this.vectors + "]";
	}
	
	public List<Std_Logic_Vector> getVectors() {
		return this.vectors;
	}

}
