package ch.thmx.simulator.report;

abstract public class Report {
	
	private ReportType type;
	
	public Report(ReportType type) {
		super();
		setType(type);
	}

	abstract public void accept(ReportVisitor visitor);
	
	@Override
	abstract public String toString();
	
	/* *** Getter & Setter *** */

	public ReportType getType() {
		return this.type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}
}
