package game2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

class NumberPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number = 0;

	public NumberPanel() {
		setColor();
	}

	public NumberPanel(int number) {
		this.number = number;
		setColor();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		setColor();
	}
	
	// Set different color for numberPanel with different number
	private void setColor() {
		if (number <= 4)
			setForeground(new Color(119, 110, 101));
		else
			setForeground(new Color(249, 246, 242));
		
		switch (number) {
			case 0:
				setBackground(new Color(214, 205, 196));
				break;
			case 2:
				setBackground(new Color(238, 228, 218));
				break;
			case 4:
				setBackground(new Color(237, 224, 190));
				break;
			case 8:
				setBackground(new Color(242, 177, 121));
				break;
			case 16:
				setBackground(new Color(245, 149, 99));
				break;
			case 32:
				setBackground(new Color(246, 124, 95));
				break;
			case 64:
				setBackground(new Color(246, 94, 59));
				break;
			case 128:
				setBackground(new Color(237, 207, 114));
				break;
			case 256:
				setBackground(new Color(237, 204, 97));
				break;
			case 512:
				setBackground(new Color(237, 200, 80));
				break;
			case 1024:
				setBackground(new Color(237, 197, 62));
				break;
			case 2048:
				setBackground(new Color(237, 194, 45));
				break;
		}
		
		if (number > 2048)
			setBackground(new Color(237, 194, 0));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setFont(new Font("SansSerif", Font.BOLD, 50));

		FontMetrics fm = g.getFontMetrics();

		int stringWidth = fm.stringWidth(number == 0 ? "" : "" + number);
		int stringAscent = fm.getAscent();

		int xCoordinate = getWidth() / 2 - stringWidth / 2;
		int yCoordinate = getHeight() / 2 + stringAscent / 3;
		g.drawString(number == 0 ? "" : "" + number, xCoordinate, yCoordinate);
	}
}