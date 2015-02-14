package letterplot.views;

import java.util.ArrayList;

import letterplot.Constants;
import letterplot.model.Data;
import nostalgia.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public abstract class BaseHandler extends Handler {

	private int mouseX = -10, mouseY = -10;
	protected boolean textCurVisible = false;

	private static float cellsX0, cellsY0, cellSize;
	private static int previewX0, previewY0, previewWidth, previewHeight;
	private Boolean drawingColor = null;
	
	private static int paddingTop = 48;
	private static int paddingBottom = 48;
	private static int paddingLeft = 20;
	private static int paddingRight = 20;
	private static int previewPadding = 2;
	
	protected static String pangram = "Sphinx of black quartz, judge my vow.";
	protected static String addSymbolHexCodeInput = "";
	
	private static boolean isHexDigit(char c) {
		return (c >= '0' && c <= '9') || 
	    (c >= 'a' && c <= 'f') || 
	    (c >= 'A' && c <= 'F');
	}
	
	protected static String[] easySplit(String s, char sep) {
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
	
	protected static boolean canTypeSymbolHexCodeInput(char c) {
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
	
	protected static String formatHexCodeOrRange(String input, int digs) {
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
	
	private void drawPreview(Color fore, Color back, Bitmap bmp, boolean[] letter, int x0, int y0) {
		for (int i = 0; i < data.getEditingFont().getWidth(); i++) {
			for (int j = 0; j < data.getEditingFont().getHeight(); j++) {
				if (letter[j * data.getEditingFont().getWidth() + i]) {
					bmp.setPixel(x0 + i, y0 + j, fore);
				} else {
					bmp.setPixel(x0 + i, y0 + j, back);
				}
			}
		}
	}

	protected static void drawHotkeys(Bitmap screen, String[] hotKeyDesc) {
		String[] hotKeys = new String[] { "Esc", " F1", " F2", " F3", " F4", " F5", " F6", " F7", " F8", " F9", "F10", "F11", "F12" };
		drawHotkeys(screen, hotKeys, hotKeyDesc);
	}
	
	protected static void drawHotkeys(Bitmap screen, String[] hotKeys, String[] hotKeyDesc) {

		Painter p = new Painter(screen);
		p.setForeground(null);
		p.setBackground(Constants.BACK_HIGHLIGHT_COLOR);
		p.setFont(Constants.SYSTEM_FONT);
		
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
		} while (hotKeySpace < (hotKeyDescLenMax + hotKeyLenMax) * Constants.SYSTEM_FONT.getWidth());
		
		
		int hotKeyY = screen.getHeight() - Constants.SYSTEM_FONT.getHeight() - 1;
		for (int i = 0; i < hotkeysNumberLimit; i++) {
			p.setForeground(null);
			int x = i * hotKeySpace;
			p.setBackground(Constants.BACK_HIGHLIGHT_COLOR);
			p.drawRectangle(x + hotKeyLenMax * Constants.SYSTEM_FONT.getWidth(), hotKeyY - 1, x + hotKeySpace, screen.getHeight());
			
			p.setForeground(Constants.FORE_HIGHLIGHT_COLOR);
			p.drawString(x + hotKeyLenMax * Constants.SYSTEM_FONT.getWidth() + 1, hotKeyY, hotKeyDesc[i]);
			p.setBackground(Constants.BACK_COLOR);
			p.setForeground(Constants.FORE_COLOR);
			p.drawString(x + Constants.SYSTEM_FONT.getWidth(), hotKeyY, hotKeys[i]);
		}
	}
	
	boolean isInCells(int x, int y) {
		return x > cellsX0 && y > cellsY0 &&
		       x < cellsX0 + cellSize * data.getEditingFont().getWidth() &&
		       y < cellsY0 + cellSize * data.getEditingFont().getHeight();
	}
	int cellX(int x) {
		return (int) ((mouseX - cellsX0) / cellSize); 
	}
	int cellY(int y) {
		return (int) ((mouseY - cellsY0) / cellSize); 
	}

	private Data data;

	public BaseHandler(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	protected void drawMainScreen(boolean pangramEntryActive) {
		textCurVisible = System.currentTimeMillis() / 100 % 2 == 0;
		boolean[] currentLetter = data.getCurrentSymbol();
		Bitmap screen = getScreen();
		Painter p = new Painter(screen);
		p.setBackground(Constants.BACK_SCREEN_COLOR);
		p.drawRectangle(0, 0, screen.getWidth() - 1, screen.getHeight() - 1);
		
		p.setForeground(new Color(0.5f, 0.5f, 0.5f));
		
		// Drawing cells
		for (int i = 0; i < data.getEditingFont().getWidth(); i++) {
			for (int j = 0; j < data.getEditingFont().getHeight(); j++) {
				
				if (currentLetter[j * data.getEditingFont().getWidth() + i]) {
					p.setBackground(Constants.FORE_COLOR);
				} else {
					p.setBackground(Constants.BACK_COLOR);
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
		drawPreview(Constants.FORE_COLOR, Constants.BACK_COLOR, screen, currentLetter, previewX0 + previewPadding, previewY0 + previewPadding);
		
		// Drawing other previews for the other characters
		Character cur = data.getCurrentChar();
		int pos = previewX0 - 2 * previewPadding;
		while ((cur = data.getEditingFont().previousSymbol(cur)) != null) {
			pos -= data.getEditingFont().getWidth() + 2 * previewPadding;
			p.drawRectangle(pos, previewY0, pos + previewWidth, previewY0 + previewHeight);
			drawPreview(Constants.FORE_COLOR, Constants.BACK_COLOR, screen, data.getEditingFont().getSymbol(cur), pos + previewPadding, previewY0 + previewPadding);
		}
		cur = data.getCurrentChar();
		pos = previewX0 + 2 * previewPadding;
		while ((cur = data.getEditingFont().nextSymbol(cur)) != null) {
			pos += data.getEditingFont().getWidth() + 2 * previewPadding;
			p.drawRectangle(pos, previewY0, pos + previewWidth, previewY0 + previewHeight);
			drawPreview(Constants.FORE_COLOR, Constants.BACK_COLOR, screen, data.getEditingFont().getSymbol(cur), pos + previewPadding, previewY0 + previewPadding);
		}
		
		// Drawing pangram and the cursor
		p.setFont(data.getEditingFont());
		p.setForeground(Constants.FORE_COLOR);
		int panW = p.stringWidth(pangram);
		int pangramX0 = (int)(screen.getWidth() / 2 - panW / 2);
		int pangramY0 = (int)(cellsY0 + cellSize * data.getEditingFont().getHeight() + paddingBottom / 3);
		p.drawString(pangramX0, pangramY0, pangram);

		int textCurX = pangramX0 + data.getEditingFont().getWidth() * pangram.length();
		if (pangramEntryActive && textCurVisible) { 
			p.setForeground(null);
			p.setBackground(Constants.FORE_COLOR);
			p.drawRectangle(textCurX, pangramY0, textCurX + data.getEditingFont().getWidth(), pangramY0 + data.getEditingFont().getHeight());
		}

	}

	@Override
	public void sizeChanged() {
		Bitmap screen = getScreen();

		int widthAligned = getScreen().getWidth() - paddingLeft - paddingRight;
		int heightAligned = getScreen().getHeight() - paddingTop - paddingBottom;

		cellSize = Math.min((float)widthAligned / data.getEditingFont().getWidth(), (float)heightAligned / data.getEditingFont().getHeight());
		cellsX0 = screen.getWidth() / 2 - cellSize * data.getEditingFont().getWidth() / 2;
		cellsY0 = screen.getHeight() / 2 - cellSize * data.getEditingFont().getHeight() / 2;
		
		previewX0 = screen.getWidth() / 2 - data.getEditingFont().getWidth() / 2 - previewPadding;
		previewY0 = paddingTop / 2 - data.getEditingFont().getHeight() / 2 - previewPadding;
		previewWidth = data.getEditingFont().getWidth() + 2 * previewPadding - 1;
		previewHeight = data.getEditingFont().getHeight() + 2 * previewPadding - 1;
	}
	
	@Override
	public void mouseMove(double xPts, double yPts) {
		mouseX = (int) Math.round(xPts);
		mouseY = (int) Math.round(yPts);
		boolean[] currentLetter = data.getEditingFont().getSymbol(data.getCurrentChar());
		if (drawingColor != null) {
			if (isInCells(mouseX, mouseY)) {
				int i = cellX(mouseX); 
				int j = cellY(mouseY);
				currentLetter[j * data.getEditingFont().getWidth() + i] = drawingColor;
			}
		}
	}
	
	protected Boolean getDrawingColor() {
		return drawingColor;
	}
	
	protected void setDrawingColor(Boolean drawingColor) {
		this.drawingColor = drawingColor;
	}
}
