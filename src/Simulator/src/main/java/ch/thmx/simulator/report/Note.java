package ch.thmx.simulator.report;

public class Note extends Report {

	private String message;
	
	public Note(String message) {
		super(ReportType.Note);
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
		return "Note [message=" + this.message + "]";
	}

}
