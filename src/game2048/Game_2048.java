package game2048;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;

final class Game_2048 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GetResource res = new GetResource("game2048/Resource", DataStorage.readLocale());

	private JMenuItem menuItem_modes;
	private JMenuItem menuItem_chinese, menuItem_english;
	private JMenuItem menuItem_help, menuItem_about;

	private NewGame newGame;

	private final int frameWidth = 550,
			frameHeight = 670;

	Game_2048() {
//		Set window icon
		setIconImage(new ImageIcon("Image/logo_2048.png").getImage()); // Set Icon

//		Initialize window
		setTitle(res.getString("version") + " - Mode - " + DataStorage.readMode());
		setSize(frameWidth, frameHeight + 26);
		
		JMenuBar menuBar = new JMenuBar();
		ComponentsListener listener = new ComponentsListener();
		
		JMenu menu_setting = new JMenu(res.getString("setting"));
		
		menuItem_modes = new JMenuItem(res.getString("modes"));
		menuItem_modes.addActionListener(listener);
		menu_setting.add(menuItem_modes);
		
		JMenu menu_language = new JMenu(res.getString("language"));
		menuItem_chinese = new JMenuItem(res.getString("chinese"));
		menuItem_chinese.addActionListener(listener);
		menu_language.add(menuItem_chinese);
		menuItem_english = new JMenuItem(res.getString("english"));
		menuItem_english.addActionListener(listener);
		menu_language.add(menuItem_english);
		
		menu_setting.add(menu_language);
		
		menuBar.add(menu_setting);
		
//		Create menu bar and menu of help
		JMenu menu_help = new JMenu(res.getString("help"));
		menuItem_help = new JMenuItem(res.getString("how_to_play"));
		menuItem_help.addActionListener(listener);
		menuItem_about = new JMenuItem(res.getString("about_2048"));
		menuItem_about.addActionListener(listener);
		menu_help.add(menuItem_help);
		menu_help.add(menuItem_about);
		
		menuBar.add(menu_help);
		
//		Create 4*4 blocks
		newGame = new NewGame();

		setJMenuBar(menuBar);
		add(newGame);
		addWindowListener(listener);

		newGame.startGame(); // Start a new game
	}
	
//	define all of components listener
	private class ComponentsListener extends WindowAdapter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == menuItem_help) {
				
				Object[] options = {res.getString("confirm")};
				ShowDialog.showOptionDialog(
						res.getString("how_to_play_details"),
						res.getString("how_to_play"),
						ShowDialog.INFORMATION_MESSAGE,
						options);
				
			} else if (e.getSource() == menuItem_about) {
				
				Object[] options = {
						res.getString("get_updates"),
						res.getString("what_is_new"),
						res.getString("cancel")};
				int answer = ShowDialog.showOptionDialog(
						res.getString("about_2048_details"),
						res.getString("about_2048"),
						ShowDialog.INFORMATION_MESSAGE,
						options);
				
				if (answer < 0) return;
				
				if (options[answer].equals(res.getString("get_updates"))) {
					URI uri = null;
					try {
						uri = new URI("https://yunpan.cn/ckpynJXqEzqYK");
					} catch (URISyntaxException ex) {
						Object[] confirmOption = {res.getString("confirm")};
						ShowDialog.showOptionDialog(
								ex.getMessage(),
								"URISyntaxException",
								ShowDialog.ERROR_MESSAGE,
								confirmOption);
					}
					
					Desktop desktop = Desktop.getDesktop();
					
					if (Desktop.isDesktopSupported() && 
							desktop.isSupported(Desktop.Action.BROWSE)) {
						try {
							desktop.browse(uri);
						} catch (IOException ex) {
							Object[] confirmOption = {res.getString("confirm")};
							ShowDialog.showOptionDialog(
									ex.getMessage(),
									"IOException",
									ShowDialog.ERROR_MESSAGE,
									confirmOption);
						}
					}
				} else if (options[answer].equals(res.getString("what_is_new"))) {
					
					Object[] confirmOption = {res.getString("confirm")};
					ShowDialog.showOptionDialog(
							res.getString("what_is_new_details"),
							res.getString("what_is_new"),
							JOptionPane.INFORMATION_MESSAGE,
							confirmOption);
					
				}
			} else if (e.getSource() == menuItem_chinese
					|| e.getSource() == menuItem_english) {
				
				if (e.getSource() == menuItem_chinese) {
					if (DataStorage.readLocale().equals("zh"))
						return;
					DataStorage.writeLocale("zh");
					res = new GetResource("game2048/Resource", "zh");
				} else if (e.getSource() == menuItem_english) {
					if (DataStorage.readLocale().equals("en"))
						return;
					DataStorage.writeLocale("en");
					res = new GetResource("game2048/Resource", "en");
				}
				
				menuItem_chinese.setText(res.getString("chinese"));
				menuItem_english.setText(res.getString("english"));
				Object[] options = {
						res.getString("close_now"),
						res.getString("close_latter")};
				int answer = ShowDialog.showOptionDialog(
						res.getString("restart_confirm"),
						res.getString("message"),
						JOptionPane.INFORMATION_MESSAGE,
						options);
				
				if (answer >= 0 && options[answer].equals(res.getString("close_now"))) {
					dispose();
				}
				
			} else if (e.getSource() == menuItem_modes) {
				String[] selectionValues = {"4", "8", "16", "32", "64", "128",
						"256", "512", "1024", "2048", "4096", "8192", "2147483648"};
				
				String mode = ShowDialog.showInputDialog(
						res.getString("select_a_mode"), res.getString("select_a_mode"),
						selectionValues);
				
				if (mode != null) {
					newGame.setMode(Long.parseLong(mode));
					newGame.init();
					newGame.startGame();
					setTitle(res.getString("version") + " - Mode - " + mode);
				}
				
			}
		}
		
		@Override
		public void windowOpened(WindowEvent evt) {
			
			try {
				RandomAccessFile inout = new RandomAccessFile("Data/score.dat", "rw");
				if (inout.length() == 0) {
					
					Object[] confirmOption = {res.getString("confirm")};
					ShowDialog.showOptionDialog(
							res.getString("what_is_new_details"),
							res.getString("what_is_new"),
							JOptionPane.INFORMATION_MESSAGE,
							confirmOption);

				}
				inout.close();
			} catch (IOException ex) {
				
				Object[] confirmOption = {res.getString("confirm")};
				ShowDialog.showOptionDialog(
						ex.getLocalizedMessage(),
						"IOException",
						JOptionPane.INFORMATION_MESSAGE,
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

	public static void main(String[] args) {
		Game_2048 frame = new Game_2048();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}

