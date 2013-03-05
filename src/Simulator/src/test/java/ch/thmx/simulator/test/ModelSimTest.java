package ch.thmx.simulator.test;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.thmx.simulator.Simulator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"/AppContext-test.xml"
	})
public class ModelSimTest {
	
	@Resource(name = "modelsim")
	private Simulator simulator;
	
	@Before
	public void init() {
		// :o)
	}
	
	@Test
	public void testVLIB() {
		// TODO ModelSim test: vlib
		
	}
	
	@Test
	public void testVCOM() {
		// TODO ModelSim test: vcom
	}
	
	@Test
	public void testVSIM() {
		// TODO ModelSim test: vsim
	}

}
