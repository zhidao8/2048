package game2048;

import javax.swing.*;

public class ShowDialog extends JOptionPane {

    private ShowDialog() {
    }

    public static int showOptionDialog(Object message, String title,
            int messageType, Object[] options) {
        return showOptionDialog(null, message, title, messageType,
                messageType, null, options, null);
    }
}
