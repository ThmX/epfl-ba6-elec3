package ch.thmx.simulator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.log4j.Logger;

import ch.thmx.simulator.report.FatalError;
import ch.thmx.simulator.report.Report;
import ch.thmx.simulator.report.ReportReader;
import ch.thmx.simulator.tasks.Task;

public class SimulatorTask implements Task {

	static final Logger log = Logger.getLogger(SimulatorTask.class);

	private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

	private PrintWriter writer;

	private Process process;
	private boolean activeProcess;

	private boolean active;

	private String[] command;

	private Simulator simulator;

	private Report launchingReport;

	public SimulatorTask(Simulator simulator, String... command) {
		this(simulator, null, command);
	}

	public SimulatorTask(Simulator simulator, Report report, String... command) {
		super();
		this.command = command;
		this.launchingReport = report;
		this.simulator = simulator;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public synchronized void waitFor() {
		while (isActive()) {
			try {
				wait();
			} catch (InterruptedException e) {
				log.error("", e);
			}
		}
	}

	@Override
	public synchronized int run() {
		this.active = true;
		this.simulator.setCurrentTask(this);

		int exitValue = -1;
		
		this.simulator.processReport(this.launchingReport);

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(this.command);
			processBuilder.directory(this.simulator.getWork());

			this.process = processBuilder.start();

			processReportReader(this.simulator.createReader(new InputStreamReader(this.process.getInputStream())));
			processReportReader(this.simulator.createReader(new InputStreamReader(this.process.getErrorStream())));
			this.writer = new PrintWriter(this.process.getOutputStream());

			this.activeProcess = true;
			do {
				try {
					exitValue = this.process.waitFor();
					this.activeProcess = false;

				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			} while (this.activeProcess);

		} catch (IOException e) {
			this.simulator.processReport(new FatalError("", e.getMessage()));
			log.error(e.getMessage(), e);

		} finally {

			WriteLock writeLock = this.reentrantReadWriteLock.writeLock();

			try {
				writeLock.lock();

				stop();
			} finally {
				writeLock.unlock();

				this.active = false;
				notifyAll();
			}
		}

		return exitValue;
	}

	@Override
	public void stop() {
		if (this.process != null) {
			this.process.destroy();
		}
		this.process = null;
		this.simulator.setCurrentTask(null);
	}

	private Thread processReportReader(final ReportReader reader) {

		Thread thread = new Thread() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void run() {
				ReadLock readLock = SimulatorTask.this.reentrantReadWriteLock.readLock();

				try {
					readLock.lock();
					do {
						Report report = reader.readReport();

						if (report != null) {
							SimulatorTask.this.simulator.processReport(report);
						}

					} while (SimulatorTask.this.activeProcess || reader.ready());
				} catch (IOException e) {
					SimulatorTask.this.simulator.processReport(new FatalError("", e.getMessage()));
					log.error(e.getMessage(), e);

				} finally {
					try {
						reader.close();
					} catch (Exception e) {
						// Nothing else to do...
					}

					readLock.unlock();
				}
			}
		};
		thread.start();

		return thread;
	}

	public void writeCommand(String cmd) {
		if (this.writer != null) {
			this.writer.println(cmd);
			this.writer.flush();
		}
	}
}
