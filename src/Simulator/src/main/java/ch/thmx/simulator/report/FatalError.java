package ch.thmx.simulator.report;

public class FatalError extends Report {

	private String line;
	private String message;

	public FatalError(String line, String message) {
		super(ReportType.FatalError);
		this.line = line;
		this.message = message;
	}

	public String getLine() {
		return this.line;
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
		return "FatalError [line=" + this.line + ", message=" + this.message + "]";
	}

}
