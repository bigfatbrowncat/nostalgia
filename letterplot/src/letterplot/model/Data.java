package letterplot.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nostalgia.graphics.Font;

public class Data {
	private static int fontWidth = 6, fontHeight = 8;
	
	private Font editingFont;
	private Character currentChar;

	public Data() {
		createFont();
	}
	
	public Font getEditingFont() {
		return editingFont;
	}
	
	public Character getCurrentChar() {
		return currentChar;
	}
	public void setCurrentChar(Character currentChar) {
		this.currentChar = currentChar;
	}
	
	public void saveFont(String filename) {
		FileOutputStream fos = null;
		try {
			try {
				fos = new FileOutputStream(filename);
				editingFont.toStream(fos);
				fos.close();
			} finally {
				if (fos != null) fos.close();
			}
		} catch (IOException ex) {
			System.err.println("Can't save the font file");
			ex.printStackTrace();
		} 
	}
	
	public boolean loadFont(String filename) {
		FileInputStream fis = null;
		try {
			try {
				fis = new FileInputStream(filename);
				editingFont = Font.fromStream(fis);
				return true;
			} finally {
				if (fis != null) fis.close();
			}
		} catch (IOException ex) {
			System.err.println("Can't load the font file");
			ex.printStackTrace();
		}
		return false;
	}

	public void createFont() {
		editingFont = new Font(fontWidth, fontHeight);
		editingFont.addSymbol('A');
		currentChar = 'A';
	}
	
	public boolean[] getCurrentSymbol() {
		return editingFont.getSymbol(currentChar);
	}
}
