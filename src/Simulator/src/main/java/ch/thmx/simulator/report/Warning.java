package ch.thmx.simulator.report;

public class Warning extends Report {

	private String message;
	
	public Warning(String message) {
		super(ReportType.Warning);
		this.message = message;
	}

	@Override
	public void accept(ReportVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Warning [message=" + this.message + "]";
	}

}
