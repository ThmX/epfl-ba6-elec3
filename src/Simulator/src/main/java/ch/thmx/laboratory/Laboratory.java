package ch.thmx.laboratory;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import ch.thmx.simulator.Simulator;
import ch.thmx.simulator.report.Compile;
import ch.thmx.simulator.report.FatalError;
import ch.thmx.simulator.report.Halt;
import ch.thmx.simulator.report.Note;
import ch.thmx.simulator.report.Report;
import ch.thmx.simulator.report.ReportVisitor;
import ch.thmx.simulator.report.Simulate;
import ch.thmx.simulator.tasks.Task;

public abstract class Laboratory extends Observable implements Observer, ReportVisitor {
	
	public static enum LaboratoryState {
		halt, compiling, processing, running, completed, error
	}
	
	private String entity;
	
	private String files;

	LaboratoryState state = LaboratoryState.halt;
	
	private String message;

	public LaboratoryState getState() {
		return this.state;
	}
	
	public void setState(LaboratoryState state) {
		this.state = state;
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Resource(name="${simulator}")
	private Simulator sim;
	
	@PostConstruct
	public void initLaboratory() {
		this.sim.addObserver(this);
	}

	@SuppressWarnings("unused")
	public void work() throws IOException {

		// FIXME Put back generateWorkDir

		File dir = new File("work-" + Integer.toHexString(new Random().nextInt()) + File.separator);
		// File dir = File.createTempFile("work-", "");

		work(dir);
	}

	public void work(File work) {
		this.getSim().setWork(work);
	}

	public Task compile() {

		Task task = null;
		
		String filesStr = getFiles();

		if (filesStr != null && !filesStr.isEmpty()) {
			for (String s : filesStr.split("\\s")) {
				task = this.getSim().compile(s);
			}
		}

		return task;
	}

	public Task start() {
		String en = getEntity();
		
		if ((en == null) || en.isEmpty()) {
			return null;
		}
		
		return this.getSim().start(en);
	}
	
	public Task startAndRun() {
		String en = getEntity();
		
		if ((en == null) || en.isEmpty()) {
			return null;
		}
		
		return this.getSim().startAndRun(en);
	}

	public void stop() {
		this.getSim().stop();
	}
	
	public boolean isActive() {
		return this.getSim().isActive();
	}

	@Override
	public void update(Observable obs, Object obj) {
		if (obj == null) {
			return;
		}

		try {
			Report report = (Report) obj;
			report.accept(this);

		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	public Simulator getSim() {
		return this.sim;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getFiles() {
		return this.files;
	}

	public void setFiles(String files) {
		this.files = files;
	}
	
	@Override
	public void visit(Compile report) {
		this.state = LaboratoryState.compiling;
		this.message = report.getMessage();
		
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void visit(FatalError report) {
		this.state = LaboratoryState.error;
		this.message = report.getMessage();
		
		setChanged();
		notifyObservers();
	}

	@Override
	public void visit(Simulate report) {
		this.state = LaboratoryState.running;
		this.message = report.getMessage();
		
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void visit(Note report) {
		this.state = LaboratoryState.processing;
		this.message = report.getMessage();
		
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void visit(Halt report) {
		this.state = LaboratoryState.halt;
		this.message = "";
		
		setChanged();
		notifyObservers();
	}
}
