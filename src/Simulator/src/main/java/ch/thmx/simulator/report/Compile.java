package ch.thmx.simulator.report;

public class Compile extends Report {

	private String message;
	
	public Compile(String message) {
		super(ReportType.Compile);
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
		return "Compile [message=" + this.message + "]";
	}

}
