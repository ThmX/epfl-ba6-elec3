package ch.thmx.simulator;

import java.io.File;
import java.io.Reader;
import java.util.Observable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import ch.thmx.simulator.report.Halt;
import ch.thmx.simulator.report.Report;
import ch.thmx.simulator.report.ReportReader;
import ch.thmx.simulator.tasks.Task;
import ch.thmx.simulator.tasks.TaskManager;

abstract public class Simulator extends Observable {

	static final Logger log = Logger.getLogger(Simulator.class);
	
	// FIXME Use scope prototype or something that fucking works -_-
	//@Resource(name="taskManager")
	private TaskManager taskManager = new TaskManager();
	
	@Resource(name="simConsole")
	private SimConsole simConsole;

	private File work;

	private SimulatorTask simulatorTask;

	public File getWork() {
		return this.work;
	}

	public void setWork(File work) {
		log.info("Changed work directory: " + work.getAbsolutePath());
		this.work = work;
		this.work.mkdirs();
		work();
	}

	@PostConstruct
	public void init() {
		log.info("Initialized " + toString());
		addObserver(this.simConsole);
	}

	abstract public Task work();

	public Task compile(File[] files) {
		Task task = null;
		for (File f : files) {
			task = compile(f.getAbsolutePath());
		}
		return task;
	}

	abstract public Task compile(String filename);

	abstract public Task start(String entity);
	
	abstract public Task startAndRun(String entity);

	abstract public Task start(String entity, File script);

	public ReportReader createReader(Reader reader) {
		return new ReportReader(reader);
	}
	
	public ReportReader createErrorReader(Reader reader) {
		return new ReportReader(reader);
	}
	
	public void processReport(Report report) {
		if (report != null) {
			log.info("Read report: " + report);
			setChanged();
			notifyObservers(report);
		}
	}
	
	void setCurrentTask(SimulatorTask task) {
		this.simulatorTask = task;
	}

	public void stop() {
		this.simulatorTask.stop();
		processReport(new Halt());
	}
	
	public void writeCommand(String cmd) {
		if (this.simulatorTask != null) {
			this.simulatorTask.writeCommand(cmd);
		}
	}
	
	protected Task execCommand(String... cmd) {
		return execCommand(null, cmd);
	}
	
	protected Task execCommand(Report report, String... cmd) {
		Task task = new SimulatorTask(this, report, cmd);
		this.taskManager.addTask(toString(), task);
		return task;
	}

	abstract public void force(String signal, String value);
	abstract public void force(String signal, String value, String repeat);
	abstract public void run(String time);
	abstract public void quit();

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return (this.simulatorTask != null) && this.simulatorTask.isActive();
	}

}
