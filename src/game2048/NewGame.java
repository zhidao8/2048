package game2048;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

class NewGame extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient GetResource res = new GetResource("game2048/Resource", DataStorage.readLocale());
	
	private long mode = 2048;
	
	private ScorePanel scorePanel_currentScore, scorePanel_topScore;
	private JButton button_restart;
	
	// Define 4*4 number panel
	private NumberPanel[][] numberPanel = new NumberPanel[4][4];
	
	private boolean boolean_win = false;
	private boolean boolean_over = false;
	
	private final int frameWidth = 550,
			frameHeight = 670;
	private final int otherWidth = (int) (frameWidth / 3),
			otherHeight = 80;
	
	// New a game
	NewGame() {
		setLayout(null);
		
//		Create score panel which records current score
		scorePanel_currentScore = new ScorePanel(res.getString("score"));
		scorePanel_currentScore.setBounds(0, 0, otherWidth, otherHeight);
		add(scorePanel_currentScore);

//		Create score panel which records top score
		scorePanel_topScore = new ScorePanel(res.getString("top_score"));
		scorePanel_topScore.setScore(DataStorage.readScore());
		scorePanel_topScore.setBounds(frameWidth - otherWidth - 6, 0,
				otherWidth, otherHeight);
		add(scorePanel_topScore);
		
//		Button of restart
		button_restart = new JButton(res.getString("restart"));
		button_restart.setFont(new Font("SansSerif", Font.BOLD, 24));
		button_restart.setBackground(new Color(187, 173, 160));
		button_restart.setForeground(new Color(249, 246, 242));
		button_restart.setBounds(otherWidth, 0, otherWidth - 5, otherHeight);
		button_restart.addActionListener(new ButtonListener());
		add(button_restart);
		
		// Set this panel's layout to GridLayout, the border is border
		JPanel jPanel_numberPanel = new JPanel(new GridLayout(4, 4, 12, 12));
		jPanel_numberPanel.setBackground(new Color(187, 173, 160));
		jPanel_numberPanel.setBorder(new LineBorder(new Color(187, 173, 160), 12));
		jPanel_numberPanel.setBounds(0, frameHeight - frameWidth - 20,
				frameWidth - 6, frameWidth - 6);
		add(jPanel_numberPanel);
		
		// Initialize number panels
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				numberPanel[i][j] = new NumberPanel(0);
				numberPanel[i][j].setForeground(Color.DARK_GRAY);

				// Add number panels to this panel
				jPanel_numberPanel.add(numberPanel[i][j]);
			}
		}
		
		setData();

		// Register KeyboardListener for keyboard to listen keyboard events
		addKeyListener(new KeyboardListener());
	}
	
	// Start game
	void startGame() {
		boolean_win = false;
		boolean_over = false;
		requestFocus();
		
//		// Test color of number panel
//		int k = 0;
//		for (int i = 0; i < 4; i++) {
//			for (int j = 0; j < 4; j++) {
//				if (k == 0) {
//					 numberPanel[i][j].setNumber(0);
//					 k++;
//				} else
//					 numberPanel[i][j].setNumber((int)Math.pow(2, (double)k++));
//		 	}
//		}
	}
	
	void init() {
		// Initialize each number panel
		for (int i = 0; i < numberPanel.length; i++) {
			for (int j = 0; j < numberPanel[i].length; j++) {
				numberPanel[i][j].setNumber(0);
			}
		}

		scorePanel_currentScore.setScore(0);
		scorePanel_currentScore.repaint();
		
		// Select a number panel randomly and set number to 2
		int i = (int) (Math.random() * 4);
		int j = (int) (Math.random() * 4);
		numberPanel[i][j].setNumber(2);

		// Select a number panel randomly again and set number to 2
		do {
			i = (int) (Math.random() * 4);
			j = (int) (Math.random() * 4);
		} while (numberPanel[i][j].getNumber() == 2);
		numberPanel[i][j].setNumber(2);
	}
	
	long getMode() {
		return mode;
	}
	
	void setMode(long mode) {
		this.mode = mode;
	}
	
	int getTopScore() {
		return scorePanel_topScore.getScore();
	}
	
	@SuppressWarnings("unchecked")
	private void setData() {
		setMode(DataStorage.readMode());
		
		ArrayList<Integer> list = (ArrayList<Integer>)DataStorage.readData();
		if (list.size() > 0) {
			int index = 0;
			scorePanel_currentScore.setScore(list.get(index++));
			
			for (int i = 0; i < numberPanel.length; i++) {
				for (int j = 0; j < numberPanel[i].length; j++)
					numberPanel[i][j].setNumber(list.get(index++));
			}
		} else
			init();
	}
	
	public ArrayList<Integer> getData() {
		ArrayList<Integer> list = new ArrayList<Integer>(17);
		list.add(scorePanel_currentScore.getScore());
		for (int i = 0; i < numberPanel.length; i++) {
			for (int j = 0; j < numberPanel[i].length; j++)
				list.add(numberPanel[i][j].getNumber());
		}
		
		return list;
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			init();
			startGame();
		}
		
	}

	private class KeyboardListener extends KeyAdapter {
		private boolean boolean_up = false;
		private boolean boolean_down = false;
		private boolean boolean_left = false;
		private boolean boolean_right = false;

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					up();
					break;
				case KeyEvent.VK_DOWN:
					down();
					break;
				case KeyEvent.VK_LEFT:
					left();
					break;
				case KeyEvent.VK_RIGHT:
					right();
					break;
				default:
					return;
			}
			
			processing();
		}

		// The operation when pressed the key of ↑
		private void up() {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 3; i++) {
					if (numberPanel[i][j].getNumber() == 0)
						continue;
					for (int k = i + 1; k < 4; k++) {
						if (numberPanel[k][j].getNumber() == 0)
							continue;
						if (numberPanel[i][j].getNumber() == numberPanel[k][j].getNumber()) {
							numberPanel[i][j].setNumber(numberPanel[i][j].getNumber() * 2);
							scorePanel_currentScore.addIncreasedScore(numberPanel[i][j].getNumber());
							numberPanel[k][j].setNumber(0);
							
							// Some number panel's number have changed, set boolean_up to true
							boolean_up = true;
							break;
						} else {
							i = k - 1;
							break;
						}
					}
				}

				for (int i = 0; i < 3; i++) {
					if (numberPanel[i][j].getNumber() > 0)
						continue;
					for (int k = i + 1; k < 4; k++) {
						if (numberPanel[k][j].getNumber() == 0)
							continue;
						numberPanel[i][j].setNumber(numberPanel[k][j].getNumber());
						numberPanel[k][j].setNumber(0);
						
						// Some number panel's number have moved, set boolean_up to true
						boolean_up = true;
						break;
					}
				}
			}
		}

		// The operation when pressed the key of ↓
		private void down() {
			for (int j = 0; j < 4; j++) {
				for (int i = 3; i > 0; i--) {
					if (numberPanel[i][j].getNumber() == 0)
						continue;
					for (int k = i - 1; k >= 0; k--) {
						if (numberPanel[k][j].getNumber() == 0)
							continue;
						if (numberPanel[i][j].getNumber() == numberPanel[k][j].getNumber()) {
							numberPanel[i][j].setNumber(numberPanel[i][j].getNumber() * 2);
							scorePanel_currentScore.addIncreasedScore(numberPanel[i][j].getNumber());
							numberPanel[k][j].setNumber(0);
							
							// Some number panel's number have changed, set boolean_down to true
							boolean_down = true;
							break;
						} else {
							i = k + 1;
							break;
						}
					}
				}

				for (int i = 3; i > 0; i--) {
					if (numberPanel[i][j].getNumber() > 0)
						continue;
					for (int k = i - 1; k >= 0; k--) {
						if (numberPanel[k][j].getNumber() == 0)
							continue;
						numberPanel[i][j].setNumber(numberPanel[k][j].getNumber());
						numberPanel[k][j].setNumber(0);
						
						// Some number panel's number have moved, set boolean_down to true
						boolean_down = true;
						break;
					}
				}
			}
		}

		// The operation when pressed the key of ←
		private void left() {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 3; j++) {
					if (numberPanel[i][j].getNumber() == 0)
						continue;
					for (int k = j + 1; k < 4; k++) {
						if (numberPanel[i][k].getNumber() == 0)
							continue;
						if (numberPanel[i][j].getNumber() == numberPanel[i][k].getNumber()) {
							numberPanel[i][j].setNumber(numberPanel[i][j].getNumber() * 2);
							scorePanel_currentScore.addIncreasedScore(numberPanel[i][j].getNumber());
							numberPanel[i][k].setNumber(0);
							
							// Some number panel's number have changed, set boolean_left to true
							boolean_left = true;
							break;
						} else {
							j = k - 1;
							break;
						}
					}
				}

				for (int j = 0; j < 3; j++) {
					if (numberPanel[i][j].getNumber() > 0)
						continue;
					for (int k = j + 1; k < 4; k++) {
						if (numberPanel[i][k].getNumber() == 0)
							continue;
						numberPanel[i][j].setNumber(numberPanel[i][k].getNumber());
						numberPanel[i][k].setNumber(0);
						
						// Some number panel's number have moved, set boolean_left to true
						boolean_left = true;
						break;
					}
				}
			}
		}

		// The operation when pressed the key of  → 
		private void right() {
			for (int i = 0; i < 4; i++) {
				for (int j = 3; j > 0; j--) {
					if (numberPanel[i][j].getNumber() == 0)
						continue;
					for (int k = j - 1; k >= 0; k--) {
						if (numberPanel[i][k].getNumber() == 0)
							continue;
						if (numberPanel[i][j].getNumber() == numberPanel[i][k].getNumber()) {
							numberPanel[i][j].setNumber(numberPanel[i][j].getNumber() * 2);
							scorePanel_currentScore.addIncreasedScore(numberPanel[i][j].getNumber());
							numberPanel[i][k].setNumber(0);
							
							// Some number panel's number have changed, set boolean_right to true
							boolean_right = true;
							break;
						} else {
							j = k + 1;
							break;
						}
					}
				}

				for (int j = 3; j > 0; j--) {
					if (numberPanel[i][j].getNumber() > 0)
						continue;
					for (int k = j - 1; k >= 0; k--) {
						if (numberPanel[i][k].getNumber() == 0)
							continue;
						numberPanel[i][j].setNumber(numberPanel[i][k].getNumber());
						numberPanel[i][k].setNumber(0);
						
						// Some number panel's number have moved, set boolean_right to true
						boolean_right = true;
						break;
					}
				}
			}
		}
		
		private void processing() {
			// Update ScorePanels
			updateScorePanel();
			
			/* Define linear list list, which is storage all of number panels with 0,
			 * the index is 0 to 15
			 */
			ArrayList<Integer> list = new ArrayList<Integer>(16);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (numberPanel[i][j].getNumber() == 0) {

						// add serial number of number panel to list
						list.add(i * 4 + j);

						/* if a number panel whose number is equal or greater than 2048,
						 * you win
						 */
					} else if (!boolean_win && numberPanel[i][j].getNumber() >= mode) {
						win();
						
						/* Set i and j to array's length, in order to let i, j 
						 * out of array's index bound
						 */
						j = numberPanel[i].length;
						i = numberPanel.length;
						list.clear();
					}
				}
			}

			/* if number panel with 0 is exit, 
			 * select a number panel with 0 randomly and set the number to 2
			 */
			if ((boolean_up || boolean_down || boolean_left || boolean_right)
						&& list.size() > 0) {
				
				newTwo(list);
				
				boolean_up = false;
				boolean_down = false;
				boolean_left = false;
				boolean_right = false;
			} else {
				if (!boolean_over && isGameOver()) {
					over();
				}
			}
		}
		
		private void updateScorePanel() {
			scorePanel_currentScore.repaint();
			if (scorePanel_currentScore.getScore() > scorePanel_topScore.getScore())
				scorePanel_topScore.setScore(scorePanel_currentScore.getScore());
			scorePanel_topScore.repaint();
		}
		
		private void newTwo(ArrayList<Integer> list) {
			/* This class's function: 
			 * select a number panel randomly and set the number to 2
			 */
			class NewTwoOnSeparateThread implements Runnable {
				private ArrayList<Integer> list;
				
				public NewTwoOnSeparateThread(ArrayList<Integer> list) {
					this.list = list;
				}
				
				public void newTwo() {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						JOptionPane.showMessageDialog(
								null, 
								e.getMessage(), 
								"InterruptedException", 
								JOptionPane.ERROR_MESSAGE);
					}
					int ilIndex = (int) (Math.random() * list.size());
					int sum = (int) list.get(ilIndex);
					int i = sum / 4;
					int j = sum % 4;
					numberPanel[i][j].setNumber(2);
					numberPanel[i][j].repaint();
				}

				@Override
				public void run() {
					newTwo();
				}
			}
			
			Runnable newTwo = new NewTwoOnSeparateThread(list);
			Thread thread_newTwo = new Thread(newTwo);
			thread_newTwo.start();
		}

		// The operation when you win
		private void win() {
			Object[] message = {res.getString("you_win"),
					res.getString("whether_restart")};
			Object[] options = {res.getString("yes"),
					res.getString("no")};
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
				boolean_win = true;
			}
		}
		
		// The operation when you over
		private void over() {
			Object[] message = {res.getString("game_over"),
					res.getString("whether_restart")};
			Object[] options = {res.getString("yes"),
					res.getString("no")};
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
				boolean_over = true;
			}
		}
		
		// Judge the game is game over or not
		private boolean isGameOver() {
			// Check can it be carry out the method up()
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 3; i++) {
					if (numberPanel[i][j].getNumber() == numberPanel[i + 1][j].getNumber()) {
						return false;
					}
				} // for (int i
			} // for (int j

			// Check can it be carry out the method down()
			for (int j = 0; j < 4; j++) {
				for (int i = 3; i > 0; i--) {
					if (numberPanel[i][j].getNumber() == numberPanel[i - 1][j].getNumber()) {
						return false;
					}
				} // for (int i
			} // for (int j

			// Check can it be carry out the method left()
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 3; j++) {
					if (numberPanel[i][j].getNumber() == numberPanel[i][j + 1].getNumber()) {
						return false;
					}
				} // for (int j
			} // for (int i

			// Check can it be carry out the method right()
			for (int i = 0; i < 4; i++) {
				for (int j = 3; j > 0; j--) {
					if (numberPanel[i][j].getNumber() == numberPanel[i][j - 1].getNumber()) {
						return false;
					}
				} // for (int j
			} // for (int i

			return true;
		}
	}
}