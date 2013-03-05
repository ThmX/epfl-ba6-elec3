package ch.epfl.elec3;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ch.thmx.simulator.ui.simulatorUI;
import ch.thmx.simulator.ui.templates.SegmentPane;
import ch.thmx.simulator.vhdl.Std_Logic;
import ch.thmx.simulator.vhdl.Std_Logic_Vector;
import java.awt.Component;
import javax.swing.Box;

@Lazy
@Scope("singleton")
@Service("lab04ClockPane")
public class ClockPanel extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	@Resource(name = "simulatorUI")
	private simulatorUI simulatorUI;

	@Resource(name = "lab04Clock")
	Clock lab04Clock;

	private JButton btLaunch;

	Timer timer;

	private SegmentPane segS0;

	private SegmentPane segS1;

	private SegmentPane segM0;

	private SegmentPane segM1;

	private SegmentPane segH0;

	private SegmentPane segH1;

	public ClockPanel() {
		super();
		setBorder(new EmptyBorder(5, 5, 5, 5));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_segpane = new GridBagConstraints();
		gbc_segpane.insets = new Insets(0, 0, 5, 5);
		gbc_segpane.fill = GridBagConstraints.BOTH;
		gbc_segpane.gridx = 0;
		gbc_segpane.gridy = 0;
		
		JPanel segPaneHours = new JPanel();
		segPaneHours.setBorder(new EtchedBorder());
		add(segPaneHours, gbc_segpane);
		GridBagLayout gbl_segPaneWrapper = new GridBagLayout();
		gbl_segPaneWrapper.columnWidths = new int[]{0, 0, 0};
		gbl_segPaneWrapper.rowHeights = new int[]{0, 0};
		gbl_segPaneWrapper.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_segPaneWrapper.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		segPaneHours.setLayout(gbl_segPaneWrapper);
		
		this.segH1 = new SegmentPane();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 0, 5);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 0;
		segPaneHours.add(this.segH1, gbc_panel_4);
		
		this.segH0 = new SegmentPane();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		segPaneHours.add(this.segH0, gbc_panel_2);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 1;
		gbc_horizontalStrut.gridy = 0;
		add(horizontalStrut, gbc_horizontalStrut);
		
		JPanel segPaneMinutes = new JPanel((LayoutManager) null);
		segPaneMinutes.setBorder(new EtchedBorder());
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 0;
		add(segPaneMinutes, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		segPaneMinutes.setLayout(gbl_panel);
		
		this.segM1 = new SegmentPane();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		segPaneMinutes.add(this.segM1, gbc_panel_5);
		
		this.segM0 = new SegmentPane();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		segPaneMinutes.add(this.segM0, gbc_panel_3);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_1.gridx = 3;
		gbc_horizontalStrut_1.gridy = 0;
		add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		JPanel segPaneSeconds = new JPanel((LayoutManager) null);
		segPaneSeconds.setBorder(new EtchedBorder());
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 4;
		gbc_panel_1.gridy = 0;
		add(segPaneSeconds, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		segPaneSeconds.setLayout(gbl_panel_1);
		
		this.segS1 = new SegmentPane();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.insets = new Insets(0, 0, 0, 5);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 0;
		segPaneSeconds.add(this.segS1, gbc_panel_7);
		
		this.segS0 = new SegmentPane();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 1;
		gbc_panel_6.gridy = 0;
		segPaneSeconds.add(this.segS0, gbc_panel_6);

		this.btLaunch = new JButton("Connect");
		GridBagConstraints gbc_btLaunch = new GridBagConstraints();
		gbc_btLaunch.anchor = GridBagConstraints.SOUTH;
		gbc_btLaunch.fill = GridBagConstraints.HORIZONTAL;
		gbc_btLaunch.gridwidth = 5;
		gbc_btLaunch.gridx = 0;
		gbc_btLaunch.gridy = 1;
		add(this.btLaunch, gbc_btLaunch);
	}

	@PostConstruct
	public void initLab03Pane() {
		this.lab04Clock.addObserver(this);

		this.simulatorUI.addTabFirst("Clock", this);

		this.timer = new Timer(1000, this);

		this.btLaunch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				switch (ClockPanel.this.lab04Clock.getState()) {
					case compiling:
					case processing:
						break;

					case running:
						ClockPanel.this.lab04Clock.stop();
						ClockPanel.this.timer.stop();
						break;

					case halt:
					case error:
					default:
						try {
							ClockPanel.this.lab04Clock.work();
							ClockPanel.this.lab04Clock.compile();
							ClockPanel.this.lab04Clock.start();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
				}
			}
		});

	}

	public void updatePanel() {
		switch (this.lab04Clock.getState()) {
			case compiling:
			case processing:
				this.btLaunch.setEnabled(false);
				this.btLaunch.setText(this.lab04Clock.getMessage());
				break;

			case running:
				this.btLaunch.setEnabled(true);
				this.btLaunch.setText("Disconnect");
				this.lab04Clock.setTime(new Date());
				this.timer.start();
				break;

			case error:
				String message = this.lab04Clock.getMessage();
				JOptionPane.showMessageDialog(new JFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
				
			case halt:
			default:
				this.btLaunch.setEnabled(true);
				this.btLaunch.setText("Connect");
				break;
		}
	}

	@Override
	public void update(Observable observable, Object object) {
		
		Std_Logic_Vector digitH0 = this.lab04Clock.getDigitH0();
		Std_Logic_Vector digitH1 = this.lab04Clock.getDigitH1();
		Std_Logic_Vector digitM0 = this.lab04Clock.getDigitM0();
		Std_Logic_Vector digitM1 = this.lab04Clock.getDigitM1();
		Std_Logic_Vector digitS0 = this.lab04Clock.getDigitS0();
		Std_Logic_Vector digitS1 = this.lab04Clock.getDigitS1();
		
		this.segH0.setSignals(digitH0);
		this.segH1.setSignals(digitH1);
		this.segM0.setSignals(digitM0);
		this.segM1.setSignals(digitM1);
		this.segS0.setSignals(digitS0);
		this.segS1.setSignals(digitS1);

		updatePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.lab04Clock.setEn(Std_Logic.STD_LOGIC_1);
		this.lab04Clock.clock();
	}
}
