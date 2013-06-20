package ch.epfl.elec3;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.thmx.simulator.ui.simulatorUI;

public class Main {

	public static void main(String[] args) {

		File propFile = new File("simulator.properties");
		if (!propFile.exists()) {
			String message = "Impossible to open the properties file, make sure you open it inside the laboratory directory.";
			JOptionPane.showMessageDialog(new JFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			if (System.getProperty("os.name").equals("Unix")) {
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			}
			UIManager.setLookAndFeel(lookAndFeel);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
		}

		try {
			// Launch Application using Spring Framework
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "META-INF/AppContext.xml" });

			// Get Lab04 ClockPane and Open the whole UI
			context.getBean(ECUPanel.class);

			simulatorUI ui = context.getBean(simulatorUI.class);
			ui.setTitle("The ZysCar");

		} catch (BeansException e) {
			System.err.println(e);
			JOptionPane.showMessageDialog(new JFrame(), e.getCause().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}