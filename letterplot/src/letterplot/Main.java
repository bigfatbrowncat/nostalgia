package letterplot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

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
	private static Color screenBackgroundColor = new Color(0.3f, 0.3f, 0.35f);
	private static Color darkDialogBackColor = new Color(0.2f, 0.2f, 0.25f, 0.85f); 
	
	//private static Font font;
	private static Font editingFont, systemFont;
	
	private static String pangram = "Sphinx of black quartz, judge my vow.";
	private static String addSymbolHexCodeInput = "";
	
	private static boolean isHexDigit(char c) {
		return (c >= '0' && c <= '9') || 
	    (c >= 'a' && c <= 'f') || 
	    (c >= 'A' && c <= 'F');
	}
	
	private static String[] easySplit(String s, char sep) {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> parts = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != sep) {
				sb.append(s.charAt(i));
			} else {
				parts.add(sb.toString());
				sb.setLength(0);
			}
		}
		parts.add(sb.toString());
		return parts.toArray(new String[] { });
	}
	
	private static boolean canTypeSymbolHexCodeInput(char c) {
		String[] splitted = easySplit(addSymbolHexCodeInput, '-');
		if (isHexDigit(c)) {
			if (splitted.length == 1 && splitted[0].length() < 4 ||
			    splitted.length == 2 && splitted[1].length() < 4) {
				
				return true;
			} else {
				return false;
			}
		} else if (c == '-') {
			return splitted.length < 2;
		} else {
			return false;
		}
	}
	
	private static String formatHexCodeOrRange(String input, int digs) {
		String[] splitted = easySplit(input, '-');
		for (int i = 0; i < splitted.length; i++) {
			while (splitted[i].length() < digs) {
				splitted[i] = "0" + splitted[i];
			}
		}
		if (splitted.length == 1) {
			return splitted[0].toUpperCase();
		} else {
			return (splitted[0] + "-" + splitted[1]).toUpperCase();
		}
	}
	
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

	private static boolean loadFont(String filename) {
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
	
	private static void createFont() {
		editingFont = new Font(fontWidth, fontHeight);
		editingFont.addSymbol('A');
		currentChar = 'A';
	}
	
	private enum State {
		Global, AddSymbolsDialog
	}

	private static void drawHotkeys(Bitmap screen, String[] hotKeyDesc) {
		String[] hotKeys = new String[] { "Esc", " F1", " F2", " F3", " F4", " F5", " F6", " F7", " F8", " F9", "F10", "F11", "F12" };
		drawHotkeys(screen, hotKeys, hotKeyDesc);
	}
	
	private static void drawHotkeys(Bitmap screen, String[] hotKeys, String[] hotKeyDesc) {

		Painter p = new Painter(screen);
		p.setForeground(null);
		p.setBackground(backHighlightColor);
		p.setFont(systemFont);
		
		int hotKeyLenMax = 0;
		for (int i = 0; i < hotKeys.length; i++) {
			if (hotKeys[i].length() > hotKeyLenMax) {
				hotKeyLenMax = hotKeys[i].length(); 
			}
		}
		hotKeyLenMax++;
		
		int hotkeysNumberLimit = hotKeyDesc.length + 1;
		int hotKeySpace, hotKeyDescLenMax;
		do {
			hotkeysNumberLimit --;
			hotKeyDescLenMax = 0;
			for (int i = 0; i < hotKeyDesc.length; i++) {
				if (hotKeyDesc[i].length() > hotKeyDescLenMax) {
					hotKeyDescLenMax = hotKeyDesc[i].length(); 
				}
			}
			hotKeySpace = screen.getWidth() / hotkeysNumberLimit;
		} while (hotKeySpace < (hotKeyDescLenMax + hotKeyLenMax) * systemFont.getWidth());
		
		
		int hotKeyY = screen.getHeight() - systemFont.getHeight() - 1;
		for (int i = 0; i < hotkeysNumberLimit; i++) {
			p.setForeground(null);
			int x = i * hotKeySpace;
			p.setBackground(backHighlightColor);
			p.drawRectangle(x + hotKeyLenMax * systemFont.getWidth(), hotKeyY - 1, x + hotKeySpace, screen.getHeight());
			
			p.setForeground(foreHighlightColor);
			p.drawString(x + hotKeyLenMax * systemFont.getWidth() + 1, hotKeyY, hotKeyDesc[i]);
			p.setBackground(backColor);
			p.setForeground(foreColor);
			p.drawString(x + systemFont.getWidth(), hotKeyY, hotKeys[i]);
		}
	}
	
	public static void main(String[] args) throws IOException {
		systemFont = Font.fromStream(Font.class.getResource("font6x8.dat").openStream());
		
		createFont();
		
		if (Core.open("LetterPlot", 800, 600, 2)) {
			Core.setCursorVisibility(false);
			boolean res = Core.run(new Handler() {
				
				private int mouseX = -10, mouseY = -10;
				private boolean textCurVisible = false;
				private State state = State.Global;

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
						textCurVisible = System.currentTimeMillis() / 100 % 2 == 0;
						boolean[] currentLetter = editingFont.getSymbol(currentChar);
						Bitmap screen = getScreen();
						Painter p = new Painter(screen);
						p.setBackground(screenBackgroundColor);
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
						
						// Drawing pangram and the cursor
						p.setFont(editingFont);
						p.setForeground(foreColor);
						int panW = p.stringWidth(pangram);
						int pangramX0 = (int)(screen.getWidth() / 2 - panW / 2);
						int pangramY0 = (int)(cellsY0 + cellSize * editingFont.getHeight() + paddingBottom / 3);
						p.drawString(pangramX0, pangramY0, pangram);

						int textCurX = pangramX0 + editingFont.getWidth() * pangram.length();
						if (textCurVisible && state == State.Global) { 
							p.setForeground(null);
							p.setBackground(foreColor);
							p.drawRectangle(textCurX, pangramY0, textCurX + editingFont.getWidth(), pangramY0 + editingFont.getHeight());
						}
						
						// Drawing dialogs over the global screen
						if (state == State.AddSymbolsDialog) {
							Painter p2 = new Painter(screen);
							p2.setBackground(darkDialogBackColor);
							p2.setForeground(null);
							p2.drawRectangle(0, 0, screen.getWidth(), screen.getHeight());
							
							String addSymbolsTitle = "Add symbols to the font";
							String[] addSymbolsDescription = new String[] {
									"Type here the unicode character index",
									"as 4 digits in hexadecimal format (like 01AB or FFFF).",
									"You can also type '-' after some digits, to create",
									"a range of codes (like 0410-042F)"
							};

							p2.setFont(systemFont);
							p2.setForeground(foreHighlightColor);
							int y0 = screen.getHeight() / 2 - 7 * p2.getFont().getHeight();
							int x0 = screen.getWidth() / 2 - p2.stringWidth(addSymbolsTitle) / 2;
							p2.drawString(x0, y0, addSymbolsTitle);

							p2.setForeground(foreColor);
							y0 += p2.getFont().getHeight();

							for (int i = 0; i < addSymbolsDescription.length; i++) {
								x0 = screen.getWidth() / 2 - p2.stringWidth(addSymbolsDescription[i]) / 2;
								y0 += p2.getFont().getHeight();
								p2.drawString(x0, y0, addSymbolsDescription[i]);
							}
							
							String formattedRange = formatHexCodeOrRange(addSymbolHexCodeInput, 4);
							x0 = screen.getWidth() / 2 - p2.stringWidth(formattedRange) / 2;
							y0 += p2.getFont().getHeight() * 3;
							p2.drawString(x0, y0, formattedRange);
							
							int addDigstextCurX = x0 + editingFont.getWidth() * formattedRange.length();
							if (textCurVisible) { 
								p.setForeground(null);
								p.setBackground(foreColor);
								p.drawRectangle(addDigstextCurX - editingFont.getWidth(), y0, addDigstextCurX, y0 + editingFont.getHeight());
							}

						}
						
						// Drawing hot keys
						if (state == State.Global) {
							String[] hotKeyDesc = new String[] { "Exit", "New", "Save", "Load", "AddS", "RemS" };
							drawHotkeys(screen, hotKeyDesc);
						} else {
							String[] hotKeys = new String[] { "  Esc", "Enter" };
							String[] hotKeyDesc = new String[] { "Cancel", "OK" };
							
							drawHotkeys(screen, hotKeys, hotKeyDesc);
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
					if (this.state == State.Global) {
						if (key == Key.ESCAPE && state == KeyState.PRESS && modifiers.isEmpty()) {
							Core.close();
						} else if (key == Key.RIGHT && (state == KeyState.PRESS || state == KeyState.REPEAT)) {
							if (modifiers.isEmpty()) {
								Character nextChar = editingFont.nextSymbol(currentChar);
								if (nextChar != null) {
									currentChar = nextChar;
								} else {
									currentChar = editingFont.getFirstSymbol();
								}
							} else if (modifiers.equals(Modifiers.of(Modifier.ALT))) {
								for (int k = 0; k < 10; k++) {
									Character nextChar = editingFont.nextSymbol(currentChar);
									if (nextChar != null) 
										currentChar = nextChar;
									else
										break;
								}
							}
						} else if (key == Key.LEFT && (state == KeyState.PRESS || state == KeyState.REPEAT)) {
							if (modifiers.isEmpty()) {
								Character nextChar = editingFont.previousSymbol(currentChar);
								if (nextChar != null) {
									currentChar = nextChar;
								} else {
									currentChar = editingFont.getLastSymbol();
								}
							} else if (modifiers.equals(Modifiers.of(Modifier.ALT))) {
								for (int k = 0; k < 10; k++) {
									Character nextChar = editingFont.previousSymbol(currentChar);
									if (nextChar != null) 
										currentChar = nextChar;
									else
										break;
								}
							}
						} else if (key == Key.F1 && state == KeyState.PRESS && modifiers.isEmpty()) {
							createFont();
						} else if (key == Key.F2 && state == KeyState.PRESS && modifiers.isEmpty()) {
							saveFont("font.dat");
						} else if (key == Key.F3 && state == KeyState.PRESS && modifiers.isEmpty()) {
							loadFont("font.dat");
						} else if (key == Key.F4 && state == KeyState.PRESS && modifiers.isEmpty()) {
							this.state = State.AddSymbolsDialog;
						} else if (key == Key.BACKSPACE && (state == KeyState.PRESS || state == KeyState.REPEAT) && modifiers.isEmpty()) {
							if (!pangram.equals("")) {
								pangram = pangram.substring(0, pangram.length() - 1);
							}
						}
					} else if (this.state == State.AddSymbolsDialog) {
						if (key == Key.ESCAPE && state == KeyState.PRESS && modifiers.isEmpty()) {
							this.state = State.Global;
						} else if (key == Key.ENTER && state == KeyState.PRESS && modifiers.isEmpty()) {
							this.state = State.Global;
							
							if (!addSymbolHexCodeInput.contains("-")) {
								int addCode = Integer.parseInt(addSymbolHexCodeInput, 16);
								editingFont.addSymbol((char)addCode);
								currentChar = (char)addCode;
							} else {
								String[] spl = easySplit(addSymbolHexCodeInput, '-');
								int rangeStart = Integer.parseInt(spl[0], 16);
								int rangeEnd = Integer.parseInt(spl[1], 16);
								for (int c = rangeStart; c <= rangeEnd; c++) {
									editingFont.addSymbol((char)c);
								}
								currentChar = (char)rangeStart;
							}
							
						} else if (key == Key.BACKSPACE && (state == KeyState.PRESS || state == KeyState.REPEAT) && modifiers.isEmpty()) {
							if (!addSymbolHexCodeInput.equals("")) {
								addSymbolHexCodeInput = addSymbolHexCodeInput.substring(0, addSymbolHexCodeInput.length() - 1);
							}
						}
					}
				}
				
				@Override
				public void character(char character, Modifiers modifiers) {
					if (this.state == State.Global) {
						if (modifiers.isEmpty() || modifiers.equals(Modifiers.of(Modifier.SHIFT))) {
							pangram += character;
						}
					} else if (this.state == State.AddSymbolsDialog) {
						if (canTypeSymbolHexCodeInput(character)) {
							addSymbolHexCodeInput += character;
						}
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
