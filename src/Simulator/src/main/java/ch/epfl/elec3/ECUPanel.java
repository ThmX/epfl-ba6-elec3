package ch.epfl.elec3;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ch.thmx.simulator.ui.simulatorUI;
import ch.thmx.simulator.vhdl.Std_Logic_Vector;
import javax.swing.JToggleButton;

@Lazy
@Scope("singleton")
@Service("ecuPane")
public class ECUPanel extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	@Resource(name = "simulatorUI")
	private simulatorUI simulatorUI;

	@Resource(name = "ecu")
	ECU ecu;

	private JButton btLaunch;

	Timer timer;
	private JPanel panelECU;
	private JLabel lblKmh;
	private JLabel lblDistance;
	private JLabel lblEngine;
	private JPanel panel;
	private JPanel panel_1;
	JToggleButton btnAccelerate;
	JToggleButton btnBrake;

	public ECUPanel() {
		super();
		setBorder(new EmptyBorder(5, 5, 5, 5));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		this.panelECU = new JPanel();
		this.panelECU.setBorder(new EmptyBorder(0, 0, 0, 0));
		GridBagConstraints gbc_panelECU = new GridBagConstraints();
		gbc_panelECU.gridwidth = 3;
		gbc_panelECU.insets = new Insets(0, 0, 5, 5);
		gbc_panelECU.fill = GridBagConstraints.BOTH;
		gbc_panelECU.gridx = 0;
		gbc_panelECU.gridy = 0;
		add(this.panelECU, gbc_panelECU);
		GridBagLayout gbl_panelECU = new GridBagLayout();
		gbl_panelECU.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panelECU.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panelECU.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelECU.rowWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		this.panelECU.setLayout(gbl_panelECU);

		this.lblKmh = new JLabel("Km/h");
		GridBagConstraints gbc_lblKmh = new GridBagConstraints();
		gbc_lblKmh.insets = new Insets(0, 0, 5, 5);
		gbc_lblKmh.gridx = 0;
		gbc_lblKmh.gridy = 0;
		this.panelECU.add(this.lblKmh, gbc_lblKmh);

		this.lblDistance = new JLabel("Distance");
		GridBagConstraints gbc_lblDistance = new GridBagConstraints();
		gbc_lblDistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblDistance.gridx = 2;
		gbc_lblDistance.gridy = 0;
		this.panelECU.add(this.lblDistance, gbc_lblDistance);

		this.lblEngine = new JLabel("Engine");
		GridBagConstraints gbc_lblEngine = new GridBagConstraints();
		gbc_lblEngine.insets = new Insets(0, 0, 0, 5);
		gbc_lblEngine.gridx = 0;
		gbc_lblEngine.gridy = 2;
		this.panelECU.add(this.lblEngine, gbc_lblEngine);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_1.gridx = 3;
		gbc_horizontalStrut_1.gridy = 0;
		add(horizontalStrut_1, gbc_horizontalStrut_1);

		this.panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 4;
		gbc_panel.gridy = 0;
		add(this.panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		this.panel.setLayout(gbl_panel);

		this.panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		this.panel.add(this.panel_1, gbc_panel_1);

		this.btnAccelerate = new JToggleButton("Accelerate");
		GridBagConstraints gbc_btnAccelerate = new GridBagConstraints();
		gbc_btnAccelerate.insets = new Insets(0, 0, 0, 5);
		gbc_btnAccelerate.gridx = 0;
		gbc_btnAccelerate.gridy = 2;
		this.panel.add(this.btnAccelerate, gbc_btnAccelerate);

		this.btnBrake = new JToggleButton("Brake\n");
		GridBagConstraints gbc_btnBrake = new GridBagConstraints();
		gbc_btnBrake.gridx = 1;
		gbc_btnBrake.gridy = 2;
		this.panel.add(this.btnBrake, gbc_btnBrake);

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
	public void initTimestampPane() {
		this.ecu.addObserver(this);

		this.simulatorUI.addTabFirst("ECU", this);

		this.timer = new Timer(100, this);

		this.btnAccelerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ECUPanel.this.ecu.accelerate(ECUPanel.this.btnAccelerate.isSelected());
			}
		});
		
		this.btnBrake.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ECUPanel.this.ecu.brake(ECUPanel.this.btnBrake.isSelected());
			}
		});

		this.btLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {

				switch (ECUPanel.this.ecu.getState()) {
					case compiling:
					case processing:
						break;

					case running:
						ECUPanel.this.ecu.stop();
						ECUPanel.this.timer.stop();
						break;

					case halt:
					case error:
					default:
						try {
							ECUPanel.this.ecu.work();
							ECUPanel.this.ecu.compile();
							ECUPanel.this.ecu.start();

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
		switch (this.ecu.getState()) {
			case compiling:
			case processing:
				this.btLaunch.setEnabled(false);
				this.btLaunch.setText(this.ecu.getMessage());
				break;

			case running:
				this.btLaunch.setEnabled(true);
				this.btLaunch.setText("Disconnect");
				// this.ecu.set...
				this.timer.start();
				break;

			case error:
				String message = this.ecu.getMessage();
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
		Std_Logic_Vector velocity = this.ecu.getVelocity();
		if (velocity != null) {
			this.lblKmh.setText(velocity.unsignedValue() + " Km/h");
		}

		Std_Logic_Vector engine = this.ecu.getEngine();
		if (engine != null) {
			this.lblEngine.setText(engine.unsignedValue() + " rpm");
		}

		Std_Logic_Vector distance = this.ecu.getDistance();
		if (distance != null) {
			this.lblDistance.setText(distance.unsignedValue() + " km");
		}

		updatePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.ecu.clock();
	}
}
