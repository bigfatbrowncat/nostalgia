package letterplot.views;

import letterplot.Constants;
import letterplot.model.Data;
import nostalgia.Core;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Painter;

public abstract class MainHandler extends BaseHandler {

	public MainHandler(Data data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	protected abstract void showAddSymbolDialog();
	
	@Override
	public void frame() {
		Bitmap screen = getScreen();
		Painter p = new Painter(screen);
		
		drawMainScreen(true);
		
		String[] hotKeyDesc = new String[] { "Exit", "New", "Save", "Load", "AddS", "RemS" };
		drawHotkeys(screen, hotKeyDesc);
		
		// Drawing mouse cursor at last
		p.drawBitmap(Constants.ARROW_CURSOR, getMouseX(), getMouseY());
	}
	
	@Override
	public void key(Key key, int scancode, KeyState state, Modifiers modifiers) {
		if (key == Key.ESCAPE && state == KeyState.PRESS && modifiers.isEmpty()) {
			Core.close();
		} else if (key == Key.RIGHT && (state == KeyState.PRESS || state == KeyState.REPEAT)) {
			if (modifiers.isEmpty()) {
				Character nextChar = getData().getEditingFont().nextSymbol(getData().getCurrentChar());
				if (nextChar != null) {
					getData().setCurrentChar(nextChar);
				} else {
					getData().setCurrentChar(getData().getEditingFont().getFirstSymbol());
				}
			} else if (modifiers.equals(Modifiers.of(Modifier.ALT))) {
				for (int k = 0; k < 10; k++) {
					Character nextChar = getData().getEditingFont().nextSymbol(getData().getCurrentChar());
					if (nextChar != null) 
						getData().setCurrentChar(nextChar);
					else
						break;
				}
			}
		} else if (key == Key.LEFT && (state == KeyState.PRESS || state == KeyState.REPEAT)) {
			if (modifiers.isEmpty()) {
				Character nextChar = getData().getEditingFont().previousSymbol(getData().getCurrentChar());
				if (nextChar != null) {
					getData().setCurrentChar(nextChar);
				} else {
					getData().setCurrentChar(getData().getEditingFont().getLastSymbol());
				}
			} else if (modifiers.equals(Modifiers.of(Modifier.ALT))) {
				for (int k = 0; k < 10; k++) {
					Character nextChar = getData().getEditingFont().previousSymbol(getData().getCurrentChar());
					if (nextChar != null) 
						getData().setCurrentChar(nextChar);
					else
						break;
				}
			}
		} else if (key == Key.F1 && state == KeyState.PRESS && modifiers.isEmpty()) {
			getData().createFont();
		} else if (key == Key.F2 && state == KeyState.PRESS && modifiers.isEmpty()) {
			getData().saveFont("font.dat");
		} else if (key == Key.F3 && state == KeyState.PRESS && modifiers.isEmpty()) {
			getData().loadFont("font.dat");
		} else if (key == Key.F4 && state == KeyState.PRESS && modifiers.isEmpty()) {
			showAddSymbolDialog();
		} else if (key == Key.BACKSPACE && (state == KeyState.PRESS || state == KeyState.REPEAT) && modifiers.isEmpty()) {
			if (!pangram.equals("")) {
				pangram = pangram.substring(0, pangram.length() - 1);
			}
		}

	}
	
	@Override
	public void character(char character, Modifiers modifiers) {
		if (modifiers.isEmpty() || modifiers.equals(Modifiers.of(Modifier.SHIFT))) {
			pangram += character;
		}
	}
	
	@Override
	public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
		if (button == MouseButton.LEFT) {
			if (state == MouseButtonState.PRESS) {
				boolean[] currentLetter = getData().getEditingFont().getSymbol(getData().getCurrentChar());
				if (isInCells(getMouseX(), getMouseY())) {
					int i = cellX(getMouseX()); 
					int j = cellY(getMouseY());
					currentLetter[j * getData().getEditingFont().getWidth() + i] = !currentLetter[j * getData().getEditingFont().getWidth() + i];
					setDrawingColor(currentLetter[j * getData().getEditingFont().getWidth() + i]);
				}
			} else if (state == MouseButtonState.RELEASE) {
				setDrawingColor(null);
			}
		}
	}
}
