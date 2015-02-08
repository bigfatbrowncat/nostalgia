package letterplot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nostalgia.Core;
import nostalgia.Core.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Font;
import nostalgia.graphics.Painter;

public class Main {
	private static int cW = 8, cH = 8;
	
	private static float[] c = new float[] {
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
	};
	private static float[] a = new float[] {
		1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
	};
	
	private static final Bitmap cursor = new Bitmap(c, c, c, a, cW, cH);
	
	private static Character currentChar;
	
	private static float cellsX0, cellsY0, cellSize;
	private static int previewX0, previewY0, previewWidth, previewHeight;
	private static Boolean drawingColor = null;
	
	private static int paddingTop = 48;
	private static int paddingBottom = 48;
	private static int paddingLeft = 20;
	private static int paddingRight = 20;
	private static int previewPadding = 2;
	
	private static Color foreColor = new Color(0.85f, 0.85f, 0.8f); 
	private static Color backColor = new Color(0.2f, 0.2f, 0.2f); 
	private static Color foreHighlightColor = new Color(0.5f, 0.85f, 0.6f); 
	private static Color backHighlightColor = new Color(0.25f, 0.5f, 0.3f); 
	
	//private static Font font;
	private static Font editingFont, systemFont;
	
	private static void drawPreview(Color fore, Color back, Bitmap bmp, boolean[] letter, int x0, int y0) {
		for (int i = 0; i < editingFont.getWidth(); i++) {
			for (int j = 0; j < editingFont.getHeight(); j++) {
				if (letter[j * editingFont.getWidth() + i]) {
					bmp.setPixel(x0 + i, y0 + j, fore);
				} else {
					bmp.setPixel(x0 + i, y0 + j, back);
				}
			}
		}
	}
	
	private static void saveFont(String filename) {
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

	private static int fontWidth = 6, fontHeight = 8;

	private static void loadFont(String filename) {
		FileInputStream fis = null;
		try {
			try {
				fis = new FileInputStream(filename);
				editingFont = Font.fromStream(fis);
			} finally {
				if (fis != null) fis.close();
			}
		} catch (IOException ex) {
			System.err.println("Can't load the font file");
			ex.printStackTrace();
			System.err.println("Creating the new font");
			editingFont = new Font(fontWidth, fontHeight);
		}
	}
	
	public static void main(String[] args) {
		try {
			systemFont = Font.fromStream(Font.class.getResource("font6x8.dat").openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		//new Font(fontWidth, fontHeight);
		/*for (char c = '0'; c <= '9'; c++) {
			font.addSymbol(c);
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			font.addSymbol(c);
		}
		for (char c = 'a'; c <= 'z'; c++) {
			font.addSymbol(c);
		}
		font.addSymbol('!');
		font.addSymbol('?');
		font.addSymbol('@');
		font.addSymbol('#');
		font.addSymbol('$');
		font.addSymbol('%');
		font.addSymbol('^');
		font.addSymbol('&');
		font.addSymbol('*');
		font.addSymbol('(');
		font.addSymbol(')');
		font.addSymbol('[');
		font.addSymbol(']');
		font.addSymbol('{');
		font.addSymbol('}');
		font.addSymbol('_');
		font.addSymbol('-');
		font.addSymbol('+');
		font.addSymbol('=');
		font.addSymbol('/');
		font.addSymbol('\\');
		font.addSymbol('|');
		font.addSymbol('<');
		font.addSymbol('>');
		font.addSymbol('\'');
		font.addSymbol('`');
		font.addSymbol('~');
		font.addSymbol('"');
		font.addSymbol('.');
		font.addSymbol(',');
		font.addSymbol(':');
		font.addSymbol(';');*/
		editingFont = new Font(fontWidth, fontHeight);
		editingFont.addSymbol('A');
		currentChar = 'A';
		
		if (Core.open("LetterPlot", 800, 600, 2)) {
			Core.setCursorVisibility(false);
			boolean res = Core.run(new Handler() {
				
				private int mouseX = -10, mouseY = -10;
				
				boolean isInCells(int x, int y) {
					return x > cellsX0 && y > cellsY0 &&
					       x < cellsX0 + cellSize * editingFont.getWidth() &&
					       y < cellsY0 + cellSize * editingFont.getHeight();
				}
				int cellX(int x) {
					return (int) ((mouseX - cellsX0) / cellSize); 
				}
				int cellY(int y) {
					return (int) ((mouseY - cellsY0) / cellSize); 
				}

				public void frame() {
					try {
						boolean[] currentLetter = editingFont.getSymbol(currentChar);
						Bitmap screen = getScreen();
						Painter p = new Painter(screen);
						p.setBackground(new Color(0.3f, 0.3f, 0.35f));
						p.drawRectangle(0, 0, screen.getWidth() - 1, screen.getHeight() - 1);
						
						p.setForeground(new Color(0.5f, 0.5f, 0.5f));
						
						// Drawing cells
						for (int i = 0; i < editingFont.getWidth(); i++) {
							for (int j = 0; j < editingFont.getHeight(); j++) {
								
								if (currentLetter[j * editingFont.getWidth() + i]) {
									p.setBackground(foreColor);
								} else {
									p.setBackground(backColor);
								}

								p.drawRectangle(
										(int)(cellsX0 + cellSize * i), 
										(int)(cellsY0 + cellSize * j),
										(int)(cellsX0 + cellSize * (i + 1)), 
										(int)(cellsY0 + cellSize * (j + 1)));
							}
						}
						
						// Drawing preview for the current character
						p.setBackground(new Color(0.2f, 0.2f, 0.2f));
						p.drawRectangle(previewX0 - previewPadding, previewY0 - previewPadding, previewX0 + previewWidth + previewPadding, previewY0 + previewHeight + previewPadding);
						drawPreview(foreColor, backColor, screen, currentLetter, previewX0 + previewPadding, previewY0 + previewPadding);
						
						// Drawing other previews for the other characters
						Character cur = currentChar;
						int pos = previewX0 - 2 * previewPadding;
						while ((cur = editingFont.previousSymbol(cur)) != null) {
							pos -= editingFont.getWidth() + 2 * previewPadding;
							p.drawRectangle(pos, previewY0, pos + previewWidth, previewY0 + previewHeight);
							drawPreview(foreColor, backColor, screen, editingFont.getSymbol(cur), pos + previewPadding, previewY0 + previewPadding);
						}
						cur = currentChar;
						pos = previewX0 + 2 * previewPadding;
						while ((cur = editingFont.nextSymbol(cur)) != null) {
							pos += editingFont.getWidth() + 2 * previewPadding;
							p.drawRectangle(pos, previewY0, pos + previewWidth, previewY0 + previewHeight);
							drawPreview(foreColor, backColor, screen, editingFont.getSymbol(cur), pos + previewPadding, previewY0 + previewPadding);
						}
						
						// Drawing pangram
						p.setFont(editingFont);
						p.setForeground(foreColor);
						String pangram = "Sphinx of black quartz, judge my vow.";
						int panW = p.stringWidth(pangram);
						p.drawString((int)(screen.getWidth() / 2 - panW / 2), (int)(cellsY0 + cellSize * editingFont.getHeight() + paddingBottom / 3), pangram);
						
						// Drawing hot keys
						//p.setForeground(foreHighlightColor);
						p.setForeground(null);
						p.setBackground(backHighlightColor);
						p.setFont(systemFont);
						String[] hotKeys = new String[] { "Esc", " F1", " F2", " F3", " F4", " F5", " F6", " F7", " F8", " F9", "F10", "F11", "F12" };
						String[] hotKeyDesc = new String[] { "Exit", "", "Save", "Load", "", "", "", "", "", "", "", "", "" };
						
						int hotkeysNumberLimit = hotKeys.length + 1;
						int hotKeyWidth, hotKeyDescLenMax;
						do {
							hotkeysNumberLimit --;
							hotKeyDescLenMax = 0;
							for (int i = 0; i < hotKeyDesc.length; i++) {
								if (hotKeyDesc[i].length() > hotKeyDescLenMax) {
									hotKeyDescLenMax = hotKeyDesc[i].length(); 
								}
							}
							hotKeyWidth = screen.getWidth() / hotkeysNumberLimit;
						} while (hotKeyWidth < (hotKeyDescLenMax + 4) * systemFont.getWidth());
						
						
						int hotKeyY = screen.getHeight() - systemFont.getHeight() - 1;
						for (int i = 0; i < hotkeysNumberLimit; i++) {
							p.setForeground(null);
							int x = i * hotKeyWidth;
							p.setBackground(backHighlightColor);
							p.drawRectangle(x + 4 * systemFont.getWidth(), hotKeyY - 1, x + hotKeyWidth, screen.getHeight());
							
							p.setForeground(foreHighlightColor);
							p.drawString(x + 4 * systemFont.getWidth() + 1, hotKeyY, hotKeyDesc[i]);
							p.setBackground(backColor);
							p.setForeground(foreColor);
							p.drawString(x + systemFont.getWidth(), hotKeyY, hotKeys[i]);
						}
						
						// Drawing mouse cursor at last
						p.drawBitmap(cursor, mouseX, mouseY);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void sizeChanged() {
					Bitmap screen = getScreen();

					int widthAligned = getScreen().getWidth() - paddingLeft - paddingRight;
					int heightAligned = getScreen().getHeight() - paddingTop - paddingBottom;

					cellSize = Math.min((float)widthAligned / editingFont.getWidth(), (float)heightAligned / editingFont.getHeight());
					cellsX0 = screen.getWidth() / 2 - cellSize * editingFont.getWidth() / 2;
					cellsY0 = screen.getHeight() / 2 - cellSize * editingFont.getHeight() / 2;
					
					previewX0 = screen.getWidth() / 2 - editingFont.getWidth() / 2 - previewPadding;
					previewY0 = paddingTop / 2 - editingFont.getHeight() / 2 - previewPadding;
					previewWidth = editingFont.getWidth() + 2 * previewPadding - 1;
					previewHeight = editingFont.getHeight() + 2 * previewPadding - 1;
				}
				
				@Override
				public void mouseMove(double xPts, double yPts) {
					mouseX = (int) Math.round(xPts);
					mouseY = (int) Math.round(yPts);
					boolean[] currentLetter = editingFont.getSymbol(currentChar);
					if (drawingColor != null) {
						if (isInCells(mouseX, mouseY)) {
							int i = cellX(mouseX); 
							int j = cellY(mouseY);
							currentLetter[j * editingFont.getWidth() + i] = drawingColor;
						}
					}
				}
				
				@Override
				public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
					if (button == MouseButton.LEFT) {
						if (state == MouseButtonState.PRESS) {
							boolean[] currentLetter = editingFont.getSymbol(currentChar);
							if (isInCells(mouseX, mouseY)) {
								int i = cellX(mouseX); 
								int j = cellY(mouseY);
								currentLetter[j * editingFont.getWidth() + i] = !currentLetter[j * editingFont.getWidth() + i];
								drawingColor = currentLetter[j * editingFont.getWidth() + i];
							}
						} else if (state == MouseButtonState.RELEASE) {
							drawingColor = null;
						}
					}
				}
				
				@Override
				public void key(Key key, int scancode, KeyState state, Modifiers modifiers) {
					if (key == Key.ESCAPE && state == KeyState.PRESS && modifiers.isEmpty()) {
						Core.close();
					} else if (key == Key.RIGHT && (state == KeyState.PRESS || state == KeyState.REPEAT) && modifiers.isEmpty()) {
						Character nextChar = editingFont.nextSymbol(currentChar);
						if (nextChar != null) {
							currentChar = nextChar;
						} else {
							currentChar = editingFont.getFirstSymbol();
						}
					} else if (key == Key.LEFT && (state == KeyState.PRESS || state == KeyState.REPEAT) && modifiers.isEmpty()) {
						Character nextChar = editingFont.previousSymbol(currentChar);
						if (nextChar != null) {
							currentChar = nextChar;
						} else {
							currentChar = editingFont.getLastSymbol();
						}
					} else if (key == Key.F2 && state == KeyState.PRESS && modifiers.isEmpty()) {
						saveFont("font.dat");
					} else if (key == Key.F3 && state == KeyState.PRESS && modifiers.isEmpty()) {
						loadFont("font.dat");
					}
				}
			});
			if (res) {
				System.out.println("mainLoop exited successfully");
			} else {
				System.err.println("mainLoop failed");
			}
		} else {
			System.err.println("createWindow failed");
		}
	}
}
