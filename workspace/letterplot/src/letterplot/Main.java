package letterplot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.ListIterator;

import nostalgia.Core;
import nostalgia.Core.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
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
	
	private static int letterWidth = 8, letterHeight = 12;
	
	private static LinkedHashMap<Character, boolean[]> letters = new LinkedHashMap<Character, boolean[]>();
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
	
	private static void drawPreview(Color fore, Color back, Bitmap bmp, boolean[] letter, int x0, int y0) {
		for (int i = 0; i < letterWidth; i++) {
			for (int j = 0; j < letterHeight; j++) {
				if (letter[j * letterWidth + i]) {
					bmp.setPixel(x0 + i, y0 + j, fore);
				} else {
					bmp.setPixel(x0 + i, y0 + j, back);
				}
			}
		}
	}
	
	private static Character nextLetter(char character) {
		int cur = -1;
		Character[] chars = letters.keySet().toArray(new Character[] { });
		for (int i = 0; i < chars.length; i++) {
			if (chars[i].equals(character)) cur = i;
		}

		if (cur < chars.length - 1) {
			return chars[cur + 1];
		} else {
			return null;
		}
	}

	private static Character previousLetter(char character) {
		int cur = -1;
		Character[] chars = letters.keySet().toArray(new Character[] { });
		for (int i = 0; i < chars.length; i++) {
			if (chars[i].equals(character)) cur = i;
		}

		if (cur > 0) {
			return chars[cur - 1];
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		
		letters.put('A', new boolean[letterWidth * letterHeight]);
		letters.put('B', new boolean[letterWidth * letterHeight]);
		letters.put('C', new boolean[letterWidth * letterHeight]);
		letters.put('D', new boolean[letterWidth * letterHeight]);
		letters.put('E', new boolean[letterWidth * letterHeight]);
		letters.put('F', new boolean[letterWidth * letterHeight]);
		currentChar = 'A';
		
		if (Core.open("LetterPlot", 800, 600, 2)) {
			Core.setCursorVisibility(false);
			boolean res = Core.run(new Handler() {
				
				private int mouseX = -10, mouseY = -10;
				
				boolean isInCells(int x, int y) {
					return x > cellsX0 && y > cellsY0 &&
					       x < cellsX0 + cellSize * letterWidth &&
					       y < cellsY0 + cellSize * letterHeight;
				}
				int cellX(int x) {
					return (int) ((mouseX - cellsX0) / cellSize); 
				}
				int cellY(int y) {
					return (int) ((mouseY - cellsY0) / cellSize); 
				}

				public void frame() {
					try {
						boolean[] currentLetter = letters.get(currentChar);
						Bitmap screen = getScreen();
						Painter p = new Painter(screen);
						p.setBackground(new Color(0.3f, 0.3f, 0.35f));
						p.drawRectangle(0, 0, screen.getWidth() - 1, screen.getHeight() - 1);
						
						p.setForeground(new Color(0.5f, 0.5f, 0.5f));
						
						// Drawing cells
						for (int i = 0; i < letterWidth; i++) {
							for (int j = 0; j < letterHeight; j++) {
								
								if (currentLetter[j * letterWidth + i]) {
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
						while ((cur = previousLetter(cur)) != null) {
							pos -= letterWidth + 2 * previewPadding;
							p.drawRectangle(pos, previewY0, pos + previewWidth, previewY0 + previewHeight);
							drawPreview(foreColor, backColor, screen, letters.get(cur), pos + previewPadding, previewY0 + previewPadding);
						}
						cur = currentChar;
						pos = previewX0 + 2 * previewPadding;
						while ((cur = nextLetter(cur)) != null) {
							pos += letterWidth + 2 * previewPadding;
							p.drawRectangle(pos, previewY0, pos + previewWidth, previewY0 + previewHeight);
							drawPreview(foreColor, backColor, screen, letters.get(cur), pos + previewPadding, previewY0 + previewPadding);
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

					cellSize = Math.min((float)widthAligned / letterWidth, (float)heightAligned / letterHeight);
					cellsX0 = screen.getWidth() / 2 - cellSize * letterWidth / 2;
					cellsY0 = screen.getHeight() / 2 - cellSize * letterHeight / 2;
					
					previewX0 = screen.getWidth() / 2 - letterWidth / 2 - previewPadding;
					previewY0 = paddingTop / 2 - letterHeight / 2 - previewPadding;
					previewWidth = letterWidth + 2 * previewPadding - 1;
					previewHeight = letterHeight + 2 * previewPadding - 1;
				}
				
				@Override
				public void mouseMove(double xPts, double yPts) {
					mouseX = (int) Math.round(xPts);
					mouseY = (int) Math.round(yPts);
					boolean[] currentLetter = letters.get(currentChar);
					if (drawingColor != null) {
						if (isInCells(mouseX, mouseY)) {
							int i = cellX(mouseX); 
							int j = cellY(mouseY);
							currentLetter[j * letterWidth + i] = drawingColor;
						}
					}
				}
				
				@Override
				public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
					if (button == MouseButton.LEFT) {
						if (state == MouseButtonState.PRESS) {
							boolean[] currentLetter = letters.get(currentChar);
							if (isInCells(mouseX, mouseY)) {
								int i = cellX(mouseX); 
								int j = cellY(mouseY);
								currentLetter[j * letterWidth + i] = !currentLetter[j * letterWidth + i];
								drawingColor = currentLetter[j * letterWidth + i];
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
					} else if (key == Key.RIGHT && state == KeyState.PRESS && modifiers.isEmpty()) {
						Character nextChar = nextLetter(currentChar);
						if (nextChar != null) {
							currentChar = nextChar;
						} else {
							currentChar = (Character) letters.keySet().toArray()[0];
						}
					} else if (key == Key.LEFT && state == KeyState.PRESS && modifiers.isEmpty()) {
						Character nextChar = previousLetter(currentChar);
						if (nextChar != null) {
							currentChar = nextChar;
						} else {
							currentChar = (Character) letters.keySet().toArray()[letters.keySet().size() - 1];
						}
					}
				}
			});
			if (res) {
				System.out.println("mainLoop exited successfully");
			} else {
				System.out.println("mainLoop failed");
			}
		} else {
			System.out.println("createWindow failed");
		}
	}
}
