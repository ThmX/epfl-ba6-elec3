package ch.thmx.simulator.tasks;

public interface Task {
	
	boolean isActive();
	
	void waitFor();
	
	int run();
	
	void stop();
	
}
