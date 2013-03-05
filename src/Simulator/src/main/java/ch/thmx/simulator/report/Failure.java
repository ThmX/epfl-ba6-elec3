package ch.thmx.simulator.report;

public class Failure extends Report {

	private String message;
	
	public Failure(String message) {
		super(ReportType.Failure);
		this.message = message;
	}

	@Override
	public void accept(ReportVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Failure [message=" + this.message + "]";
	}

}
