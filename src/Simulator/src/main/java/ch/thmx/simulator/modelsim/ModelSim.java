package ch.thmx.simulator.modelsim;

import java.io.File;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ch.thmx.simulator.Simulator;
import ch.thmx.simulator.report.Compile;
import ch.thmx.simulator.report.Report;
import ch.thmx.simulator.report.ReportReader;
import ch.thmx.simulator.report.Simulate;
import ch.thmx.simulator.tasks.Task;

@Lazy
@Scope("prototype")
@Service("modelsim")
public class ModelSim extends Simulator {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ModelSim.class);

	@Value("${modelsim.path}${modelsim.vcom}")
	private String vcom;

	@Value("${modelsim.path}${modelsim.vlib}")
	private String vlib;

	@Value("${modelsim.path}${modelsim.vsim}")
	private String vsim;

	@Override
	public String toString() {
		return "ModelSim [vcom=" + this.vcom + ", vlib=" + this.vlib + ", vsim=" + this.vsim + "]";
	}

	@Override
	public ReportReader createReader(Reader reader) {
		return new ReportReader(reader);
	}

	@Override
	public Task work() {
		// vlib work
		return execCommand(this.vlib, "work");
	}

	@Override
	public Task compile(String filename) {
		Report report = new Compile("Compiling " + filename);
		
		// vcom *.vhd
		return execCommand(report, this.vcom, filename);
	}

	@Override
	public Task start(String entity) {
		Report report = new Simulate("Simulating " + entity);
		
		// vsim -c sevenseg_tb -do "restart -f;run 0"
		return execCommand(report, this.vsim, "-c", entity, "-do", "restart -f; run 0");
	}
	
	@Override
	public Task startAndRun(String entity) {
		Report report = new Simulate("Simulating " + entity);
		
		// vsim -c sevenseg_tb -do "restart -f;run -a"
		return execCommand(report, this.vsim, "-c", entity, "-do", "restart -f;run -a; quit");
	}

	@Override
	public Task start(String entity, File script) {
		Report report = new Simulate("Simulating " + entity + " with script " + script.getAbsolutePath());
		
		// vsim -c sevenseg_tb -do "do script_path; quit"
		return execCommand(report, this.vsim, "-c", entity, "-do", script.getAbsolutePath());
	}

	@Override
	public void force(String signal, String value) {
		writeCommand("force " + signal + " " + value);
	}
	
	@Override
	public void force(String signal, String value, String repeat) {
		writeCommand("force " + signal + " " + value + " -repeat " + repeat);
	}

	@Override
	public void run(String time) {
		writeCommand("run " + time);
	}

	@Override
	public void quit() {
		writeCommand("quit -f");
	}	
	

}
