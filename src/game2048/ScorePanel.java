package game2048;

import javax.swing.*;
import java.awt.*;

class ScorePanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String message = "Message";
    private int score = 0;

    ScorePanel() {
        setLayout(null);
        setBackground(new Color(187, 173, 160));
    }

    ScorePanel(String message) {
        setLayout(null);
        setBackground(new Color(187, 173, 160));
        this.message = message;
    }

    void addIncreasedScore(int increasedScore) {
        score += increasedScore;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

//		Draw words
        g.setColor(new Color(214, 205, 196));
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(message);
        int stringAscent = fm.getAscent();
        int stringHeight = fm.getHeight();
        int xCoordinate = getWidth() / 2 - stringWidth / 2;
        int yCoordinate = (int) (getHeight() / 2 - stringAscent / 1.5);
        g.drawString(message, xCoordinate, yCoordinate);

//		Draw score
        g.setColor(new Color(249, 246, 242));
        g.setFont(new Font("SansSerif", Font.BOLD, 42));
        fm = g.getFontMetrics();
        int scoreWidth = fm.stringWidth("" + score);
        int xScore = getWidth() / 2 - scoreWidth / 2;
        int yScore = (int) (yCoordinate + stringHeight + 15);
        g.drawString("" + score, xScore, yScore);
    }
}