package ch.thmx.simulator.report;

public enum ReportType {
	
	// VHDL official severity
	Note, Warning, Error, Failure, FatalError,
	
	// Simulator severity
	Compile, Simulate, Halt, Update, Final,
	
	Unknown;
	
	public static ReportType fromName(String value) {
		for (ReportType t: ReportType.values()) {
			if (t.name().equalsIgnoreCase(value)) {
				return t;
			}
		}
		return Unknown;
	}
}
