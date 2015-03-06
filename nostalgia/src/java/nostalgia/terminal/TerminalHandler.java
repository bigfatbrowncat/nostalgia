package nostalgia.terminal;

import nostalgia.Group;
import nostalgia.Handler;
import nostalgia.graphics.Font;
import nostalgia.graphics.Painter;

public abstract class TerminalHandler extends Handler {

	private class CharGroup extends Group {
		private char ch;
		private int x0, y0;
		
		public CharGroup() {
			super(font.getWidth(), font.getHeight(), true);
		}

		@Override
		public boolean draw(Painter painter, boolean forced) {
			if (forced) {
				painter.drawString(0, 0, String.valueOf(ch));
				return true;
			} else {
				return false;
			}
		}
		public void setPosition(int x0, int y0) {
			this.x0 = x0;
			this.y0 = y0;
			invalidate();
		}
		
		public void setChar(char ch) {
			this.ch = ch;
			invalidate();
		}
		public char getChar() {
			return ch;
		}
		
		public void display() {
			super.display(x0 * font.getWidth(), y0 * font.getHeight());
		}
	}
	
	private int width, height;
	private CharGroup[] bufferChars;
	
	private Font font;
	
	@Override
	public void sizeChanged() {
		width = getPointsWidthCount() / font.getWidth();
		height = getPointsHeightCount() / font.getHeight();

		if (bufferChars == null || bufferChars.length < width * height) {
			bufferChars = new CharGroup[width * height];
			for (int k = 0; k < width * height; k++) {
				bufferChars[k] = new CharGroup();
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bufferChars[j * width + i].setPosition(i, j);
			}
		}
	}
	
	protected abstract void fillBuffer(char[] buffer, int width, int height); 
	
	@Override
	public void frame() {
		char[] newBuffer = new char[width * height];
		fillBuffer(newBuffer, width, height);
		for (int k = 0; k < width * height; k++) {
			if (bufferChars[k].getChar() != newBuffer[k]) {
				bufferChars[k].setChar(newBuffer[k]);
			}
			bufferChars[k].display();
		}
		
	}
	
}
