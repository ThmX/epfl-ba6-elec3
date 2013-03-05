package ch.thmx.simulator.report;

public class Final extends Report {

	private String message;
	
	public Final(String message) {
		super(ReportType.Final);
		this.message = message;
	}

	@Override
	public void accept(ReportVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Final [message=" + this.message + "]";
	}
	
	public String getMessage() {
		return this.message;
	}

}
