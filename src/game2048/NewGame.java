package game2048;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class NewGame extends JPanel {

    private static final int COUNT_OF_DIGITS_ON_ONE_SIDE = 4;

    private transient GetResource res = new GetResource("game2048/Resource", DataStorage.readLocale());

    private long mode = 2048;

    private ScorePanel currentScorePanel, highestScorePanel;

    private NumberPanel[][] numberPanels = new NumberPanel[COUNT_OF_DIGITS_ON_ONE_SIDE][COUNT_OF_DIGITS_ON_ONE_SIDE];

    private boolean isWin = false;
    private boolean isOver = false;

    private final int frameWidth = 550,
            frameHeight = 670;
    private final int otherWidth = (int) (frameWidth / 3),
            otherHeight = 80;

    private GameController gameController = new GameController(COUNT_OF_DIGITS_ON_ONE_SIDE);

    NewGame() {
        setLayout(null);

        currentScorePanel = new ScorePanel(res.getString("score"));
        currentScorePanel.setBounds(0, 0, otherWidth, otherHeight);
        add(currentScorePanel);

        highestScorePanel = new ScorePanel(res.getString("top_score"));
        highestScorePanel.setScore(DataStorage.readScore());
        highestScorePanel.setBounds(frameWidth - otherWidth - 6, 0, otherWidth, otherHeight);
        add(highestScorePanel);

        JButton restartButton = new JButton(res.getString("restart"));
        restartButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        restartButton.setBackground(new Color(187, 173, 160));
        restartButton.setForeground(new Color(249, 246, 242));
        restartButton.setBounds(otherWidth, 0, otherWidth - 5, otherHeight);
        restartButton.addActionListener(e -> {
            init();
            startGame();
        });
        add(restartButton);

        // Set this panel's layout to GridLayout, the border is border
        JPanel numberBlocksContainer = new JPanel(new GridLayout(4, 4, 12, 12));
        numberBlocksContainer.setBackground(new Color(187, 173, 160));
        numberBlocksContainer.setBorder(new LineBorder(new Color(187, 173, 160), 12));
        numberBlocksContainer.setBounds(0, frameHeight - frameWidth - 20, frameWidth - 6, frameWidth - 6);
        add(numberBlocksContainer);

        // Initialize number panels
        for (int i = 0; i < COUNT_OF_DIGITS_ON_ONE_SIDE; i++) {
            for (int j = 0; j < COUNT_OF_DIGITS_ON_ONE_SIDE; j++) {
                numberPanels[i][j] = new NumberPanel(0);
                numberBlocksContainer.add(numberPanels[i][j]);
            }
        }

        setData();

        addKeyListener(new KeyboardListener());
    }

    void startGame() {
        isWin = false;
        isOver = false;
        requestFocus();
    }

    void init() {
        gameController = new GameController(COUNT_OF_DIGITS_ON_ONE_SIDE);

        currentScorePanel.setScore(0);
        currentScorePanel.repaint();

        gameController.newTwo();
        gameController.newTwo();
        updateAllNumbers();

        // todo delete
        printData();
    }

    long getMode() {
        return mode;
    }

    void setMode(long mode) {
        this.mode = mode;
    }

    int getTopScore() {
        return highestScorePanel.getScore();
    }

    @SuppressWarnings("unchecked")
    private void setData() {
        setMode(DataStorage.readMode());

        ArrayList<Integer> list = (ArrayList<Integer>) DataStorage.readData();
        if (list.size() > 0) {
            int index = 0;
            currentScorePanel.setScore(list.get(index++));

            for (int i = 0; i < numberPanels.length; i++) {
                for (int j = 0; j < numberPanels[i].length; j++)
                    numberPanels[i][j].setNumber(list.get(index++));
            }
        } else
            init();
    }

    public ArrayList<Integer> getData() {
        ArrayList<Integer> list = new ArrayList<Integer>(17);
        list.add(currentScorePanel.getScore());
        for (NumberPanel[] numberPanel : numberPanels) {
            for (NumberPanel panel : numberPanel) {
                list.add(panel.getNumber());
            }
        }

        return list;
    }

    private void updateAllNumbers() {
        int[][] numbers = gameController.getNumbers();
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                numberPanels[i][j].setNumber(numbers[i][j]);
                numberPanels[i][j].repaint();
            }
        }
    }

    private void printData() {
        int[][] ints = gameController.getNumbers();
        for (int[] anInt : ints) {
            for (int i : anInt) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    private class KeyboardListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            // todo delete
            System.out.println("before move");
            printData();

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    gameController.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    gameController.moveDown();
                    break;
                case KeyEvent.VK_LEFT:
                    gameController.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    gameController.moveRight();
                    break;
                default:
                    return;
            }

            int score = gameController.calculateCurrentStepScore();
            currentScorePanel.setScore(currentScorePanel.getScore() + score);
            updateScorePanel();

            // todo delete
            System.out.println("after move");
            updateAllNumbers();
            printData();

            processing();
        }

        private void processing() {
            updateScorePanel();

            outer:
            for (int i = 0; i < COUNT_OF_DIGITS_ON_ONE_SIDE; i++) {
                for (int j = 0; j < COUNT_OF_DIGITS_ON_ONE_SIDE; j++) {
                    if (!isWin && numberPanels[i][j].getNumber() >= mode) {
                        win();
                        break outer;
                    }
                }
            }

            if (!isGameOver()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // ignored
                }
                gameController.newTwo();
                gameController.resetStatus();
            } else if (!isOver) {
                over();
            }

            // todo delete
            System.out.println("new two");
            updateAllNumbers();
            printData();
            System.out.println();
        }

        private void updateScorePanel() {
            currentScorePanel.repaint();
            if (currentScorePanel.getScore() > highestScorePanel.getScore()) {
                highestScorePanel.setScore(currentScorePanel.getScore());
            }
            highestScorePanel.repaint();
        }

        // The operation when you win
        private void win() {
            Object[] message = { res.getString("you_win"),
                    res.getString("whether_restart") };
            Object[] options = { res.getString("yes"),
                    res.getString("no") };
            int answer = ShowDialog.showOptionDialog(
                    message,
                    res.getString("message"),
                    ShowDialog.INFORMATION_MESSAGE,
                    options);
            if (answer == JOptionPane.YES_OPTION) {
                // 开始游戏
                init();
                startGame();
                repaint();
            } else if (answer == JOptionPane.NO_OPTION) {
                isWin = true;
            }
        }

        // The operation when you over
        private void over() {
            Object[] message = { res.getString("game_over"),
                    res.getString("whether_restart") };
            Object[] options = { res.getString("yes"),
                    res.getString("no") };
            int answer = ShowDialog.showOptionDialog(
                    message,
                    res.getString("message"),
                    ShowDialog.INFORMATION_MESSAGE,
                    options);
            if (answer == JOptionPane.YES_OPTION) {
                init();
                startGame();
                repaint();
            } else if (answer == JOptionPane.NO_OPTION) {
                isOver = true;
            }
        }

        private boolean isGameOver() {
            return !existsEmptyNumberPanel() && !canLongitudinalMove() && !canHorizontalMove();
        }

        private boolean existsEmptyNumberPanel() {
            for (NumberPanel[] horizontalNumberPanels : numberPanels) {
                for (NumberPanel numberPanel : horizontalNumberPanels) {
                    if (numberPanel.getNumber() == 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean canLongitudinalMove() {
            for (int horizontalIndex = 0; horizontalIndex < COUNT_OF_DIGITS_ON_ONE_SIDE; horizontalIndex++) {
                for (int longitudinalIndex = 0; longitudinalIndex < COUNT_OF_DIGITS_ON_ONE_SIDE - 1; longitudinalIndex++) {
                    int currentNumber = numberPanels[longitudinalIndex][horizontalIndex].getNumber();
                    int nextNumber = numberPanels[longitudinalIndex + 1][horizontalIndex].getNumber();
                    if (currentNumber == nextNumber) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean canHorizontalMove() {
            for (int longitudinalIndex = 0; longitudinalIndex < COUNT_OF_DIGITS_ON_ONE_SIDE; longitudinalIndex++) {
                for (int horizontalIndex = 0; horizontalIndex < COUNT_OF_DIGITS_ON_ONE_SIDE - 1; horizontalIndex++) {
                    int currentNumber = numberPanels[longitudinalIndex][horizontalIndex].getNumber();
                    int nextNumber = numberPanels[longitudinalIndex][horizontalIndex + 1].getNumber();
                    if (currentNumber == nextNumber) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}