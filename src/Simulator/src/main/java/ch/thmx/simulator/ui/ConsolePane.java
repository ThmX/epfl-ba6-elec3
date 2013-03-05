package ch.thmx.simulator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ch.thmx.simulator.SimConsole;
import ch.thmx.simulator.report.Report;

@Lazy
@Scope("singleton")
@Service("consolePane")
public class ConsolePane extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	@Resource(name="simConsole")
	private SimConsole simConsole;

	JTextArea consoleArea;

	/**
	 * Create the panel.
	 */
	public ConsolePane() {
		super();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{364, 76, 0};
		gridBagLayout.rowHeights = new int[]{263, 27, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		this.consoleArea = new JTextArea();
		this.consoleArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.consoleArea);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		scrollPane.setAutoscrolls(true);
		
		DefaultCaret caret = (DefaultCaret)this.consoleArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JButton btnNewButton = new JButton("Clear");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 1;
		add(btnNewButton, gbc_btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ConsolePane.this.consoleArea.setText("");
			}
		});
	}
	
	@PostConstruct
	public void init() {
		this.simConsole.addObserver(this);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if (obj == null) {
			return;
		}

		try {
			Report report = (Report) obj;
			
			// TODO Use Visitor Pattern instead of toString
			this.consoleArea.append(report.toString() + "\n");

		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}
	
}
