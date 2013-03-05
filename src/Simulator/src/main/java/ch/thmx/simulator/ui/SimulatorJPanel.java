package ch.thmx.simulator.ui;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.JPanel;

import ch.thmx.simulator.Simulator;
import ch.thmx.simulator.report.Report;
import ch.thmx.simulator.report.ReportVisitor;

@Deprecated
abstract public class SimulatorJPanel extends JPanel implements Observer, ReportVisitor {

	private static final long serialVersionUID = 1L;
	
	@Resource(name="${simulator}")
	protected Simulator sim;
	
	public SimulatorJPanel() {
		super();
	}

	@PostConstruct
	public void initSimulatorJPanel() {
		this.sim.addObserver(this);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if (!this.sim.isActive()) {
			return;
		}
		
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
}
