package ch.epfl.elec3;

import java.util.Date;
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

@Service(value = "lab04Clock")
public class Clock extends Laboratory {
	
	private static Logger log = Logger.getLogger(Clock.class);
	
	@Value("${lab4.clock.entity}")
	private String labEntity;
	
	@Value("${lab4.clock.files}")
	private String labFiles;
	
	@PostConstruct
	public void initLab04Clock() {
		setEntity(this.labEntity);
		setFiles(this.labFiles);
	}
	
	private Std_Logic_Vector digitH0;
	private Std_Logic_Vector digitH1;
	private Std_Logic_Vector digitM0;
	private Std_Logic_Vector digitM1;
	private Std_Logic_Vector digitS0;
	private Std_Logic_Vector digitS1;
	
	public Std_Logic_Vector getDigitH0() {
		return this.digitH0;
	}

	public Std_Logic_Vector getDigitH1() {
		return this.digitH1;
	}

	public Std_Logic_Vector getDigitM0() {
		return this.digitM0;
	}

	public Std_Logic_Vector getDigitM1() {
		return this.digitM1;
	}

	public Std_Logic_Vector getDigitS0() {
		return this.digitS0;
	}

	public Std_Logic_Vector getDigitS1() {
		return this.digitS1;
	}

	public void setEn(Std_Logic input) {
		this.getSim().force("en", input.toString());
	}
	
	public void setLoad(Std_Logic input) {
		this.getSim().force("load", input.toString());
	}
	
	public void setPreH0(Std_Logic_Vector input) {
		this.getSim().force("preH0", input.toString());
	}
	
	public void setPreH1(Std_Logic_Vector input) {
		this.getSim().force("preH1", input.toString());
	}
	
	public void setPreM0(Std_Logic_Vector input) {
		this.getSim().force("preM0", input.toString());
	}
	
	public void setPreM1(Std_Logic_Vector input) {
		this.getSim().force("preM1", input.toString());
	}
	
	public void setPreS0(Std_Logic_Vector input) {
		this.getSim().force("preS0", input.toString());
	}
	
	public void setPreS1(Std_Logic_Vector input) {
		this.getSim().force("preS1", input.toString());
	}
	
	public void setTime(Date time) {
		setEn(Std_Logic.STD_LOGIC_1);
		setLoad(Std_Logic.STD_LOGIC_1);
		int h = time.getHours();
		setPreH1(new Std_Logic_Vector(h / 10, 4));
		setPreH0(new Std_Logic_Vector(h % 10, 4));
		
		int m = time.getMinutes();
		setPreM1(new Std_Logic_Vector(m / 10, 4));
		setPreM0(new Std_Logic_Vector(m % 10, 4));
		
		int s = time.getSeconds();
		setPreS1(new Std_Logic_Vector(s / 10, 4));
		setPreS0(new Std_Logic_Vector(s % 10, 4));
		
		clock();
		setLoad(Std_Logic.STD_LOGIC_0);
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
		if (vectors.size() >= 6) {
			this.digitH0 = vectors.get(0);
			this.digitH1 = vectors.get(1);
			this.digitM0 = vectors.get(2);
			this.digitM1 = vectors.get(3);
			this.digitS0 = vectors.get(4);
			this.digitS1 = vectors.get(5);
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
