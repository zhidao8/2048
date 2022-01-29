package game2048;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class NumberPanel extends JPanel {

    private int number;
    private static final Map<Integer, Color> NUMBER_BACKGROUND_COLOR_MAP = new HashMap<>();

    static {
        NUMBER_BACKGROUND_COLOR_MAP.put(0, new Color(214, 205, 196));
        NUMBER_BACKGROUND_COLOR_MAP.put(2, new Color(238, 228, 218));
        NUMBER_BACKGROUND_COLOR_MAP.put(4, new Color(237, 224, 190));
        NUMBER_BACKGROUND_COLOR_MAP.put(8, new Color(242, 177, 121));
        NUMBER_BACKGROUND_COLOR_MAP.put(16, new Color(245, 149, 99));
        NUMBER_BACKGROUND_COLOR_MAP.put(32, new Color(246, 124, 95));
        NUMBER_BACKGROUND_COLOR_MAP.put(64, new Color(246, 94, 59));
        NUMBER_BACKGROUND_COLOR_MAP.put(128, new Color(237, 207, 114));
        NUMBER_BACKGROUND_COLOR_MAP.put(256, new Color(237, 204, 97));
        NUMBER_BACKGROUND_COLOR_MAP.put(512, new Color(237, 200, 80));
        NUMBER_BACKGROUND_COLOR_MAP.put(1024, new Color(237, 197, 62));
        NUMBER_BACKGROUND_COLOR_MAP.put(2048, new Color(237, 194, 45));
    }

    public NumberPanel() {
        this(0);
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

    private void setColor() {
        if (number <= 4) {
            setForeground(new Color(119, 110, 101));
        } else {
            setForeground(new Color(249, 246, 242));
        }

        Color color = NUMBER_BACKGROUND_COLOR_MAP.getOrDefault(number, new Color(237, 194, 0));
        setBackground(color);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(new Font("SansSerif", Font.BOLD, 50));

        FontMetrics fm = g.getFontMetrics();

        String strToBeDraw = number == 0 ? "" : String.valueOf(number);
        int stringWidth = fm.stringWidth(strToBeDraw);
        int stringAscent = fm.getAscent();
        int xCoordinate = getWidth() / 2 - stringWidth / 2;
        int yCoordinate = getHeight() / 2 + stringAscent / 3;
        g.drawString(strToBeDraw, xCoordinate, yCoordinate);
    }
}