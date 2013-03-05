package ch.thmx.simulator.ui.templates;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ch.thmx.simulator.vhdl.Std_Logic;
import ch.thmx.simulator.vhdl.Std_Logic_Vector;

public class SegmentPane extends JPanel {

	private static final long serialVersionUID = 2L;

	private static final int PADDING = 8;
	private static final int LINE_WIDTH = 10;

	private static Color COLOR_FLASH = Color.yellow;
	private static Color COLOR_ON = Color.green;
	private static Color COLOR_OFF = Color.lightGray;
	private static Color COLOR_UNDEF = new Color(128, 0, 0);
	
	private Std_Logic_Vector flash;

	private Std_Logic[] signals;

	public SegmentPane() {
		super(null);
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(Color.GRAY);

		this.flash = Std_Logic.STD_LOGIC_0.vector();
		this.signals = new Std_Logic[7];

		repaint();
	}

	public void setSignals(Std_Logic_Vector vector) {
		if (vector == null) {
			return;
		}

		Std_Logic[] values = vector.getValue();

		if (values.length < 7) {
			throw new IllegalArgumentException("Should have at least 7 segments.");
		}

		this.signals = values;

		repaint();
	}

	public void setFlash(Std_Logic_Vector overflow) {
		this.flash = (overflow == null) ? Std_Logic.STD_LOGIC_0.vector() : overflow;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Draw Segment A
		drawSegment(g, 0, 0, false, this.signals[0]);

		// Draw Segment B
		drawSegment(g, 1, 0, true, this.signals[1]);

		// Draw Segment C
		drawSegment(g, 1, 1, true, this.signals[2]);

		// Draw Segment D
		drawSegment(g, 0, 2, false, this.signals[3]);

		// Draw Segment E
		drawSegment(g, 0, 1, true, this.signals[4]);

		// Draw Segment F
		drawSegment(g, 0, 0, true, this.signals[5]);

		// Draw Segment G
		drawSegment(g, 0, 1, false, this.signals[6]);
	}

	private void drawSegment(Graphics g, int x, int y, boolean vertical, Std_Logic bit) {

		int width = getWidth() - 2 * PADDING - LINE_WIDTH;
		int height = getHeight() / 2 - 3 * PADDING / 2;

		int wx;
		int hy;

		x *= width;
		y *= height;
		if (vertical) {
			wx = LINE_WIDTH;
			hy = height - LINE_WIDTH;
		} else {
			x += LINE_WIDTH;
			y -= LINE_WIDTH;
			wx = width - LINE_WIDTH;
			hy = LINE_WIDTH;
		}
		
		if (bit == null || !bit.isValid() || !this.flash.isValid()) {
			g.setColor(COLOR_UNDEF);
			
		} else if (bit.boolValue()) {
			g.setColor(this.flash.getValue()[0].boolValue() ? COLOR_FLASH : COLOR_ON);
		} else {
			g.setColor(COLOR_OFF);
		}
		
		g.fillRoundRect(x + PADDING, y + 2 * PADDING, wx, hy, 2, 2);
	}

}
