package game2048;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBuilder {

    private GetResource res;
    private MainWindow mainWindow;
    private NewGame newGame;

    public MainMenuBuilder(GetResource res, MainWindow mainWindow, NewGame newGame) {
        this.res = res;
        this.mainWindow = mainWindow;
        this.newGame = newGame;
    }

    public JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu settingMenu = new JMenu(res.getString("setting"));
        settingMenu.add(buildModeMenu());
        settingMenu.addSeparator();
        settingMenu.add(buildLanguageMenu());

        menuBar.add(settingMenu);

        JMenu helpMenu = new JMenu(res.getString("help"));
        helpMenu.add(buildHelpMenuItem());
        helpMenu.add(buildAboutMenuItem());

        menuBar.add(helpMenu);

        return menuBar;
    }

    private JMenuItem buildAboutMenuItem() {
        JMenuItem aboutMenuItem = new JMenuItem(res.getString("about_2048"));
        aboutMenuItem.addActionListener(e -> {
            Object[] options = {
                    res.getString("what_is_new"),
                    res.getString("cancel") };
            int answer = ShowDialog.showOptionDialog(
                    res.getString("about_2048_details"),
                    res.getString("about_2048"),
                    ShowDialog.INFORMATION_MESSAGE,
                    options);

            if (answer < 0) return;

            if (options[answer].equals(res.getString("what_is_new"))) {

                Object[] confirmOption = { res.getString("confirm") };
                ShowDialog.showOptionDialog(
                        res.getString("what_is_new_details"),
                        res.getString("what_is_new"),
                        JOptionPane.INFORMATION_MESSAGE,
                        confirmOption);
            }
        });
        return aboutMenuItem;
    }

    private JMenuItem buildHelpMenuItem() {
        JMenuItem helpMenuItem = new JMenuItem(res.getString("how_to_play"));
        helpMenuItem.addActionListener(e -> {
            Object[] options = { res.getString("confirm") };
            ShowDialog.showOptionDialog(
                    res.getString("how_to_play_details"),
                    res.getString("how_to_play"),
                    ShowDialog.INFORMATION_MESSAGE,
                    options
            );
        });
        return helpMenuItem;
    }

    private JMenu buildLanguageMenu() {
        JMenu languageMenu = new JMenu(res.getString("language"));
        JRadioButtonMenuItem chineseMenuItem = buildLanguageMenuItem("chinese", "zh");
        languageMenu.add(chineseMenuItem);

        JRadioButtonMenuItem englishMenuItem = buildLanguageMenuItem("english", "en");
        languageMenu.add(englishMenuItem);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(chineseMenuItem);
        buttonGroup.add(englishMenuItem);
        return languageMenu;
    }

    private JRadioButtonMenuItem buildLanguageMenuItem(String menuItemLabelKey, String languageCode) {
        JRadioButtonMenuItem chineseMenuItem = new JRadioButtonMenuItem(res.getString(menuItemLabelKey));
        chineseMenuItem.addActionListener(e -> {
            if (DataStorage.readLocale().equals(languageCode)) {
                return;
            }
            DataStorage.writeLocale(languageCode);
            res = new GetResource("game2048/Resource", languageCode);

            Object[] options = {
                    res.getString("close_now"),
                    res.getString("close_latter")
            };
            int answer = ShowDialog.showOptionDialog(
                    res.getString("restart_confirm"),
                    res.getString("message"),
                    JOptionPane.INFORMATION_MESSAGE,
                    options
            );

            if (answer >= 0 && options[answer].equals(res.getString("close_now"))) {
                mainWindow.dispose();
            }
        });
        return chineseMenuItem;
    }

    private JMenu buildModeMenu() {
        JMenu modeMenu = new JMenu(res.getString("modes"));

        class ModeItemAction implements ActionListener {
            private long value;

            ModeItemAction(long value) {
                this.value = value;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                newGame.setMode(value);
                newGame.init();
                newGame.startGame();
                mainWindow.setTitle(res.getString("version") + " - Mode - " + value);
            }
        }

        ButtonGroup radioButtonGroup = new ButtonGroup();
        long[] selectionValues = { 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 2147483648L };
        for (long selectionValue : selectionValues) {
            JRadioButtonMenuItem modeMenuItem = new JRadioButtonMenuItem(String.valueOf(selectionValue));
            modeMenuItem.addActionListener(new ModeItemAction(selectionValue));
            if (selectionValue == 2048) {
                modeMenuItem.setSelected(true);
            }
            radioButtonGroup.add(modeMenuItem);
            modeMenu.add(modeMenuItem);
        }

        return modeMenu;
    }
}
