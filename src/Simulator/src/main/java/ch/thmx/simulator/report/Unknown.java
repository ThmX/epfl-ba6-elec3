package ch.thmx.simulator.report;

public class Unknown extends Report {

	private String message;

	public Unknown(String message) {
		super(ReportType.Unknown);
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
		return "Unknown [message=" + this.message + "]";
	}


}
