package ch.epfl.elec3;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.thmx.laboratory.Laboratory;
import ch.thmx.simulator.report.Failure;
import ch.thmx.simulator.report.Final;
import ch.thmx.simulator.report.Unknown;
import ch.thmx.simulator.report.Update;
import ch.thmx.simulator.report.Warning;
import ch.thmx.simulator.vhdl.Std_Logic;
import ch.thmx.simulator.vhdl.Std_Logic_Vector;

@Service(value = "ecu")
public class ECU extends Laboratory {

	private static Logger log = Logger.getLogger(ECU.class);

	@Value("${ecu.entity}")
	private String labEntity;

	@Value("${ecu.files}")
	private String labFiles;

	@PostConstruct
	public void initECU() {
		setEntity(this.labEntity);
		setFiles(this.labFiles);
	}

	private Std_Logic mmu_rw;
	private Std_Logic_Vector mmu_addr;
	private Std_Logic_Vector mmu_out;
	private Std_Logic_Vector velocity;
	private Std_Logic_Vector engine;
	private Std_Logic_Vector distance;
	private Std_Logic_Vector timestamp;

	public Std_Logic getMmu_rw() {
		return this.mmu_rw;
	}

	public Std_Logic_Vector getMmu_addr() {
		return this.mmu_addr;
	}

	public Std_Logic_Vector getMmu_out() {
		return this.mmu_out;
	}

	public Std_Logic_Vector getVelocity() {
		return this.velocity;
	}

	public Std_Logic_Vector getEngine() {
		return this.engine;
	}
	
	public Std_Logic_Vector getDistance() {
		return this.distance;
	}

	public Std_Logic_Vector getTimestamp() {
		return this.timestamp;
	}

	public void setMmu_in(Std_Logic_Vector input) {
		this.getSim().force("mmu_in", input.toString());
	}

	public void setOp(Std_Logic_Vector input) {
		this.getSim().force("op", input.toString());
	}

	public void setOpx(Std_Logic_Vector input) {
		this.getSim().force("opx", input.toString());
	}
	
	public void brake(boolean brake) {
		this.getSim().force("sim_brake", brake ? "1" : "0");
	}
	
	public void accelerate(boolean acc) {
		this.getSim().force("sim_accelerate", acc ? "1" : "0");
	}

	public void clock() {
		this.getSim().force("clk", "0");
		this.getSim().run("10 ns");
		this.getSim().force("clk", "1");
		this.getSim().run("10 ns");
	}

	public void reset() {
		this.getSim().force("clk", "0");
		this.getSim().force("reset", "1");
		this.getSim().run("5 ns");
		this.getSim().force("reset", "0");
	}

	@Override
	public void visit(Update report) {
		log.info("Update with " + report.toString());

		List<Std_Logic_Vector> vectors = report.getVectors();
		if (vectors.size() >= 7) {
			this.timestamp = vectors.get(0);
			this.mmu_rw = vectors.get(1).getValue()[0];
			this.mmu_addr = vectors.get(2);
			this.mmu_out = vectors.get(3);
			this.velocity = vectors.get(4);
			this.engine = vectors.get(5);
			this.distance = vectors.get(6);
		}

		setChanged();
		notifyObservers();
	}

	@Override
	public void visit(Warning report) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Failure report) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Error report) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Final report) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Unknown report) {
		// TODO Auto-generated method stub

	}

}
