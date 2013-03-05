package ch.thmx.simulator.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Launch every test
 * @author Thomas Denoréaz
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		ModelSimTest.class,
		ReportTest.class
	})
public final class AllTestsSuite {
	// Nothing to do :o)
} 