package letterplot.views;

import letterplot.Constants;
import letterplot.model.Data;
import nostalgia.Group;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public class BaseGroup extends Group {

	private boolean textCurVisible = false;

	private static float cellsX0, cellsY0, cellSize;
	private static int previewX0, previewY0, previewWidth, previewHeight;
	
	private static int paddingTop = 48;
	private static int paddingBottom = 48;
	private static int paddingLeft = 20;
	private static int paddingRight = 20;
	private static int previewPadding = 2;
	
	private Data data;
	
	private static String pangram;
	
	protected void drawMainScreen(Bitmap screen, boolean pangramEntryActive) {
		textCurVisible = System.currentTimeMillis() / 100 % 2 == 0;
		boolean[] currentLetter = data.getCurrentSymbol();
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

	
	public BaseGroup(int width, int height, Data data) {
		super(width, height, false);
		this.data = data;
	}
	
	@Override
	public boolean draw(Painter painter, boolean forced) {
		Bitmap bitmap = painter.getBitmap();
		if (forced) {
			int widthAligned = bitmap.getWidth() - paddingLeft - paddingRight;
			int heightAligned = bitmap.getHeight() - paddingTop - paddingBottom;

			cellSize = Math.min((float)widthAligned / data.getEditingFont().getWidth(), (float)heightAligned / data.getEditingFont().getHeight());
			cellsX0 = bitmap.getWidth() / 2 - cellSize * data.getEditingFont().getWidth() / 2;
			cellsY0 = bitmap.getHeight() / 2 - cellSize * data.getEditingFont().getHeight() / 2;
			
			previewX0 = bitmap.getWidth() / 2 - data.getEditingFont().getWidth() / 2 - previewPadding;
			previewY0 = paddingTop / 2 - data.getEditingFont().getHeight() / 2 - previewPadding;
			previewWidth = data.getEditingFont().getWidth() + 2 * previewPadding - 1;
			previewHeight = data.getEditingFont().getHeight() + 2 * previewPadding - 1;
		}
		return true;
	}

	public boolean isInCells(int x, int y) {
		return x > cellsX0 && y > cellsY0 &&
		       x < cellsX0 + cellSize * data.getEditingFont().getWidth() &&
		       y < cellsY0 + cellSize * data.getEditingFont().getHeight();
	}
	
	public int cellX(int mouseX) {
		return (int) ((mouseX - cellsX0) / cellSize); 
	}
	public int cellY(int mouseY) {
		return (int) ((mouseY - cellsY0) / cellSize); 
	}
	
	public static void setPangram(String pangram) {
		BaseGroup.pangram = pangram;
	}
	public boolean isTextCurVisible() {
		return textCurVisible;
	}
	
}
