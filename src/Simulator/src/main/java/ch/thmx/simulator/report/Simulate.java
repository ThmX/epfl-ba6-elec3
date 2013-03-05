package ch.thmx.simulator.report;

public class Simulate extends Report {

	private String message;
	
	public Simulate(String message) {
		super(ReportType.Simulate);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public void accept(ReportVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Simulate [message=" + this.message + "]";
	}

}
