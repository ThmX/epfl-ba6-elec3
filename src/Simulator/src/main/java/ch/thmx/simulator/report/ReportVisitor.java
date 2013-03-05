package ch.thmx.simulator.report;

public interface ReportVisitor {

	// VHDL Severity
	public void visit(Note report);
	public void visit(Warning report);
	public void visit(Failure report);
	public void visit(Error report);
	public void visit(FatalError report);	
	
	// Simulator severity
	public void visit(Update report);
	public void visit(Final report);
	
	// Simulator report
	public void visit(Compile report);
	public void visit(Simulate report);
	public void visit(Halt report);
	
	// Unknown
	public void visit(Unknown report);
	
}
