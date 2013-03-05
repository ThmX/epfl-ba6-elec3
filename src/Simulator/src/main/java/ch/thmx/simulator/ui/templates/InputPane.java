package ch.thmx.simulator.ui.templates;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.thmx.simulator.vhdl.Std_Logic;
import ch.thmx.simulator.vhdl.Std_Logic_Vector;

import java.awt.Color;
import java.util.Arrays;

public class InputPane extends JPanel implements ActionListener {
	
	public static interface InputListener {
		void actionInput(String signal, Std_Logic_Vector value);
	}

	private static final long serialVersionUID = 1L;
	
	private InputListener listener;

	private String signal;
	
	private JLabel lblValue;

	private JCheckBox[] checkboxes;
	
	/**
	 * @wbp.parser.constructor
	 */
	public InputPane(String signal, int count) {
		this(signal, count, true);
	}

	public InputPane(String signal, int count, boolean input) {
		this.signal = signal;
		
		setBorder(new TitledBorder(null, signal, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};

		// Create inputs
		int[] columnWidths = new int[count+1];
		double[] columnWeights = new double[count+1];
		Arrays.fill(columnWeights, 1.0);
		columnWeights[count] = Double.MIN_VALUE;
		
		gridBagLayout.columnWidths = columnWidths;
		gridBagLayout.columnWeights = columnWeights;
		setLayout(gridBagLayout);
		
		this.lblValue = new JLabel("0");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 4;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(this.lblValue, gbc_lblNewLabel);
		
		this.checkboxes = new JCheckBox[count];
		for (int i=0; i<count; ++i) {
			this.checkboxes[i] = new JCheckBox();
			GridBagConstraints gbc_cb = new GridBagConstraints();
			gbc_cb.insets = new Insets(0, 0, 0, 5);
			gbc_cb.gridx = i;
			gbc_cb.gridy = 1;
			add(this.checkboxes[i], gbc_cb);
			
			if (input) {
				this.checkboxes[i].addActionListener(this);
			} else {
				this.checkboxes[i].setEnabled(false);
			}
		}
		
		setValue(new Std_Logic_Vector(0, count));
	}
	
	public void setValue(Std_Logic_Vector value) {
		if (value == null) {
			value = new Std_Logic_Vector(0, this.checkboxes.length);
			this.lblValue.setForeground(Color.RED);

		} else {
			this.lblValue.setForeground(value.isValid() ? Color.BLACK : Color.RED);
		}
		
		Std_Logic[] bits = value.getValue();
		
		this.lblValue.setText(value.unsignedValue() + " [0x" + value.hexValue() + "]");
		
		for (int i=0; i<bits.length; ++i) {
			this.checkboxes[i].setSelected(bits[i].boolValue());
			this.checkboxes[i].setForeground(bits[i].isValid() ? Color.BLACK : Color.RED);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Std_Logic[] bits = new Std_Logic[this.checkboxes.length];
		
		for (int i=0; i<this.checkboxes.length; ++i) {
			bits[i] = this.checkboxes[i].isSelected() ? Std_Logic.STD_LOGIC_1 : Std_Logic.STD_LOGIC_0;
		}
		
		Std_Logic_Vector vector = new Std_Logic_Vector(bits);
		setValue(vector);
		
		int value = vector.unsignedValue();
		if (this.getListener() != null) {
			this.getListener().actionInput(this.signal, new Std_Logic_Vector(value, this.checkboxes.length));
		}
	}

	/**
	 * @return the listener
	 */
	public InputListener getListener() {
		return this.listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(InputListener listener) {
		this.listener = listener;
	}

}
