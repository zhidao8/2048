package game2048;

import javax.swing.*;

public class ShowDialog extends JOptionPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ShowDialog() {
	}
	
	public static int showOptionDialog(Object message, String title,
            int messageType, Object[] options, Object initialValue) {
		return showOptionDialog(null, message, title, messageType,
				messageType, null, options, initialValue);
	}

	public static int showOptionDialog(Object message, String title,
            int messageType, Object[] options) {
		return showOptionDialog(null, message, title, messageType,
				messageType, null, options, null);
	}
	
	public static String showInputDialog(Object message, String title,
			Object[] selectionValues) {
		return (String)showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE,
				null, selectionValues, "2048");
	}

}
