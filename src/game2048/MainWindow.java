package game2048;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.RandomAccessFile;

final class MainWindow extends JFrame {

    GetResource res = new GetResource("game2048/Resource", DataStorage.readLocale());
    private NewGame newGame;
    private static final int FRAME_WIDTH = 550;
    private static final int FRAME_HEIGHT = 670;

    MainWindow() {
        setIconImage(new ImageIcon("Image/logo_2048.png").getImage()); // Set Icon

        setTitle(res.getString("version") + " - Mode - " + DataStorage.readMode());
        setSize(FRAME_WIDTH, FRAME_HEIGHT + 26);

        WindowActionsListener windowListener = new WindowActionsListener();

        newGame = new NewGame();

        JMenuBar menuBar = new MainMenuBuilder(res, this, newGame).buildMenuBar();

        setJMenuBar(menuBar);
        add(newGame);
        addWindowListener(windowListener);

        newGame.startGame();
    }

    private class WindowActionsListener extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent evt) {
            try {
                RandomAccessFile inout = new RandomAccessFile("Data/score.dat", "rw");
                if (inout.length() == 0) {
                    Object[] confirmOption = { res.getString("confirm") };
                    ShowDialog.showOptionDialog(
                            res.getString("what_is_new_details"),
                            res.getString("what_is_new"),
                            JOptionPane.INFORMATION_MESSAGE,
                            confirmOption);
                }
                inout.close();
            } catch (IOException ex) {
                Object[] confirmOption = { res.getString("confirm") };
                ShowDialog.showOptionDialog(
                        ex.getLocalizedMessage(),
                        "IOException",
                        JOptionPane.ERROR_MESSAGE,
                        confirmOption);
            }

            newGame.requestFocus();
            setVisible(true);
        }

        public void windowClosed(WindowEvent evt) {
            DataStorage.writeMode(newGame.getMode());
            DataStorage.writeScore(newGame.getTopScore());
            DataStorage.writeData(newGame.getData());
        }
    }
}

