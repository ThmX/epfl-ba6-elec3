package ch.thmx.simulator.report;

public class Halt extends Report {

	public Halt() {
		super(ReportType.Halt);
	}

	@Override
	public void accept(ReportVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Halt []";
	}

}
