package game2048;

import java.io.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

class DataStorage {
	private static String file_score = "Data/score.dat";
	private static String file_locale = "Data/locale.dat";
	private static String file_data = "Data/data.dat";
	private static String file_mode = "Data/mode.dat";
	private static RandomAccessFile inout = null; // inout for read data and write data

	private DataStorage() {
	}
	
	private static void init(String path) {
		File file = new File(path);
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs(); // New the path of file if it is not exists
	}
	
	public static void writeScore(int score) {
		try {
			writeScore1(score);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Update the top score and store in file
	private static void writeScore1(int score) throws IOException {
		try {
			init(file_score);
			inout = new RandomAccessFile(file_score, "rw");
			inout.seek(0);
			inout.writeInt(score);
		} finally {
			if (inout != null)
				inout.close();
		}
	}
	
	public static int readScore() {
		int score = 0;
		try {
			score = readScore1();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		}
		
		return score;
	}
	
	// Read the top score from file
	private static int readScore1() throws IOException {
		int score = 0;
		try {
			init(file_score);
			inout = new RandomAccessFile(file_score, "rw");
			
			if (inout.length() > 0) {
				inout.seek(0);
				score = inout.readInt();
			}
			
		} finally {
			if (inout != null)
				inout.close();
		}
		
		return score;
	}
	
	public static void writeLocale(String language) {
		try {
			writeLocale1(language);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(),
					"IOException", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeLocale1(String language) throws IOException {
		try {
			init(file_locale);
			inout = new RandomAccessFile(file_locale, "rw");
			inout.seek(0);
			inout.writeUTF(language);
		} finally {
			if (inout != null)
				inout.close();
		}

	}
	
	public static String readLocale() {
		String language = "zh";
		try {
			language = readLocale1();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		}
		return language;
	}
	
	// Read the top score from file
	private static String readLocale1() throws IOException {
		String language = "zh";
		try {
			init(file_locale);
			inout = new RandomAccessFile(file_locale, "rw");
		
			if (inout.length() > 0) {
				inout.seek(0);
				language = inout.readUTF();
			}
			
		} finally {
			if (inout != null)
				inout.close();
		}
		
		return language;
	}
	
	public static Object readData() {
		ArrayList<Integer> list = new ArrayList<Integer>(17);
		try {
			list = readData1();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
		}
		
		return list;
	}
	
	private static ArrayList<Integer> readData1() throws IOException, ClassNotFoundException {
		ObjectInputStream input = null;
		ArrayList<Integer> list = new ArrayList<Integer>(17);
		try {
			init(file_data);
			
			if (isFileExists(file_data))
				input = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(file_data)));
			
			if (input != null) {
				while (input.available() > 0) {
					list.add(input.readInt());
				}
			}
			
		} finally {
			if (input != null)
				input.close();
		}
		
		return list;
	}
	
	private static boolean isFileExists(String path) {
		return new File(path).exists();
	}
	
	public static void writeData(ArrayList<Integer> list) {
		try {
			writeData1(list);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeData1(ArrayList<Integer> list) throws
			IOException, ClassNotFoundException {
		ObjectOutputStream output = null;
		try {
			init(file_data);
			
			output = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(file_data)));
			
			for (int i = 0; i < list.size(); i++) {
				output.writeInt(list.get(i));
			}
			
		} finally {
			if (output != null)
				output.close();
		}
	}
	
	public static long readMode() {
		long mode = 2048;
		try {
			mode = readMode1();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
		}
		
		return mode;
	}
	
	private static long readMode1() throws IOException, ClassNotFoundException {
		ObjectInputStream input = null;
		long mode = 2048;
		try {
			init(file_mode);
			
			if (isFileExists(file_mode))
				input = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(file_mode)));
			
			if (input != null)
				mode = input.readLong();
			
		} finally {
			if (input != null)
				input.close();
		}
		
		return mode;
	}
	
	public static void writeMode(long mode) {
		try {
			writeMode1(mode);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"IOException", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), 
					"ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeMode1(long mode) throws
			IOException, ClassNotFoundException {
		ObjectOutputStream output = null;
		try {
			init(file_mode);
			
			output = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(file_mode)));
			
			output.writeLong(mode);
			
		} finally {
			if (output != null)
				output.close();
		}
	}
	
}
