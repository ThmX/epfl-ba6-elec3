package ch.thmx.simulator.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.thmx.simulator.report.Report;
import ch.thmx.simulator.report.ReportReader;
import ch.thmx.simulator.report.ReportType;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"/AppContext-test.xml"
	})
public class ReportTest {

	@Test
	public void testReportReader() throws IOException {
		String buffer = "# ** Final:Simulation ended successfully.\n"
				+ "# ** Unknown:I'm suppossed to be unknown.\n"
				+ "I'm crazy unknown";

		ReportReader reader = new ReportReader(new InputStreamReader(
					new ByteArrayInputStream(buffer.getBytes())
				));
		
		try {
		
			Report report;
			
			report = reader.readReport();
			System.out.println(report);
			assertEquals(ReportType.Final, report.getType());
			
			report = reader.readReport();
			System.out.println(report);
			assertEquals(ReportType.Unknown, report.getType());
			
			report = reader.readReport();
			System.out.println(report);
			assertEquals(ReportType.Unknown, report.getType());
			
			// End of flux -> null
			report = reader.readReport();
			assertNull(report);
			
		} finally {
			reader.close();
		}
	}
	
	@Test
	public void testEmptyReportReader() throws IOException {
		ReportReader reader = new ReportReader(new InputStreamReader(
					new ByteArrayInputStream("".getBytes())
				));
		
		try {
		
			Report report;
			
			report = reader.readReport();
			assertNull(report);
			
		} finally {
			reader.close();
		}
	}

}
