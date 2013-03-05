package ch.thmx.simulator.ui;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("simulatorUI")
@Lazy
@Scope("singleton")
public class simulatorUI extends JFrame {
	
	private static final Logger log = Logger.getLogger(simulatorUI.class);

	private static final long serialVersionUID = 1L;
	
	// TODO ConfigurationPane
	//@Resource(name = "configurationPane")
	//private ConfigurationPane configurationPane;
	
	@Resource(name = "consolePane")
	private ConsolePane consolePane;
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	@PostConstruct
	public void init() {		
		//this.tabbedPane.addTab("Configuration", this.configurationPane);
		this.tabbedPane.addTab("Console", this.consolePane);
		
		this.setVisible(true);
	}
	
	public void addTab(String title, JPanel panel) {
		log.info("Adding tab " + title);
		this.tabbedPane.addTab(title, panel);
	}
	
	public void addTabFirst(String title, JPanel panel) {
		log.info("Adding tab " + title);
		this.tabbedPane.insertTab(title, null, panel, null, 0);
		this.tabbedPane.setSelectedIndex(0);
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("static-access")
	public simulatorUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 480);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.contentPane.add(this.tabbedPane, BorderLayout.CENTER);
	}

}
