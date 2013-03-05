package ch.thmx.simulator.ui.templates;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

public class Select3Pane extends JPanel implements ActionListener {
	
	public static interface SelectListener {
		void actionSelect(String signal, int value);
	}

	private static final long serialVersionUID = 1L;
	
	private SelectListener listener;
	
	private String signal;

	private JComboBox selValue;
	private JCheckBox cbA;
	private JCheckBox cbB;
	private JCheckBox cbC;
	private JLabel lblValue;

	/**
	 * @wbp.parser.constructor
	 */
	public Select3Pane(String signal, String... values) {
		this(signal, false, values);
	}
	
	public Select3Pane(String signal, boolean input, String... values) {
		if (values.length != 8) {
			throw new IllegalArgumentException("Should have 8 values");
		}
		
		this.signal = signal;
		
		setBorder(new TitledBorder(null, signal, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		this.lblValue = new JLabel("0");
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblValue.gridx = 0;
		gbc_lblValue.gridy = 0;
		add(this.lblValue, gbc_lblValue);
		
		this.selValue = new JComboBox();
		this.selValue.setModel(new DefaultComboBoxModel(values));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		add(this.selValue, gbc_comboBox);
		
		this.cbA = new JCheckBox();
		GridBagConstraints gbc_cbA = new GridBagConstraints();
		gbc_cbA.insets = new Insets(0, 0, 0, 5);
		gbc_cbA.gridx = 0;
		gbc_cbA.gridy = 1;
		add(this.cbA, gbc_cbA);
		
		this.cbB = new JCheckBox();
		GridBagConstraints gbc_cbB = new GridBagConstraints();
		gbc_cbB.insets = new Insets(0, 0, 0, 5);
		gbc_cbB.gridx = 1;
		gbc_cbB.gridy = 1;
		add(this.cbB, gbc_cbB);
		
		this.cbC = new JCheckBox();
		GridBagConstraints gbc_cbC = new GridBagConstraints();
		gbc_cbC.gridx = 2;
		gbc_cbC.gridy = 1;
		add(this.cbC, gbc_cbC);
		
		this.selValue.addActionListener(this);
		this.cbA.addActionListener(this);
		this.cbB.addActionListener(this);
		this.cbC.addActionListener(this);
		
		setValue(0);
	}
	
	public void setValue(int value) {
		value %= 8;
		if (value < 0) {
			this.lblValue.setForeground(Color.RED);
			value = 0;
		} else {
			this.lblValue.setForeground(Color.BLACK);
		}
		
		this.lblValue.setText(String.valueOf(value));
		this.selValue.setSelectedIndex(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int value = 0;
		if (e.getSource().equals(this.selValue)) {
			value = this.selValue.getSelectedIndex();
			this.cbA.setSelected((value & 0x4) == 0x4);
			this.cbB.setSelected((value & 0x2) == 0x2);
			this.cbC.setSelected((value & 0x1) == 0x1);
					
		} else {
			value = (this.cbA.isSelected() ? 0x4 : 0)
				  + (this.cbB.isSelected() ? 0x2 : 0)
				  + (this.cbC.isSelected() ? 0x1 : 0);
			
			this.selValue.setSelectedIndex(value);
		}
		
		this.lblValue.setText(String.valueOf(value));
		
		if (this.listener != null) {
			this.listener.actionSelect(this.signal, value);
		}
	}

	/**
	 * @return the listener
	 */
	public SelectListener getListener() {
		return this.listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(SelectListener listener) {
		this.listener = listener;
	}

}
