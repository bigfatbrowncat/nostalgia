package letterplot.views;

import letterplot.Constants;
import letterplot.model.Data;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Painter;

public abstract class AddSymbolDialogHandler extends BaseHandler {

	protected abstract void showMainScreen();

	public AddSymbolDialogHandler(Data data) {
		super(data);
	}

	@Override
	public void frame() {
		Bitmap screen = getScreen();
		Painter p = new Painter(screen);

		drawMainScreen(false);

		Painter p2 = new Painter(screen);
		p2.setBackground(Constants.DIALOG_BACK_COLOR);
		p2.setForeground(null);
		p2.drawRectangle(0, 0, screen.getWidth(), screen.getHeight());
		
		String addSymbolsTitle = "Add symbols to the font";
		String[] addSymbolsDescription = new String[] {
				"Type here the unicode character index",
				"as 4 digits in hexadecimal format (like 01AB or FFFF).",
				"You can also type '-' after some digits, to create",
				"a range of codes (like 0410-042F)"
		};

		p2.setFont(Constants.SYSTEM_FONT);
		p2.setForeground(Constants.FORE_HIGHLIGHT_COLOR);
		int y0 = screen.getHeight() / 2 - 7 * p2.getFont().getHeight();
		int x0 = screen.getWidth() / 2 - p2.stringWidth(addSymbolsTitle) / 2;
		p2.drawString(x0, y0, addSymbolsTitle);

		p2.setForeground(Constants.FORE_COLOR);
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
		
		int addDigstextCurX = x0 + getData().getEditingFont().getWidth() * formattedRange.length();
		if (textCurVisible) { 
			p.setForeground(null);
			p.setBackground(Constants.FORE_COLOR);
			p.drawRectangle(addDigstextCurX - getData().getEditingFont().getWidth(), y0, addDigstextCurX, y0 + getData().getEditingFont().getHeight());
		}

		String[] hotKeys = new String[] { "  Esc", "Enter" };
		String[] hotKeyDesc = new String[] { "Cancel", "OK" };
		
		drawHotkeys(screen, hotKeys, hotKeyDesc);
	}
	
	@Override
	public void key(Key key, int scancode, KeyState state, Modifiers modifiers) {
		if (key == Key.ESCAPE && state == KeyState.PRESS && modifiers.isEmpty()) {
			showMainScreen();
		} else if (key == Key.ENTER && state == KeyState.PRESS && modifiers.isEmpty()) {
			if (!addSymbolHexCodeInput.contains("-")) {
				int addCode;
				if (addSymbolHexCodeInput != "") {
					addCode = Integer.parseInt(addSymbolHexCodeInput, 16);
				} else {
					addCode = 0;
				}
				getData().getEditingFont().addSymbol((char)addCode);
				getData().setCurrentChar((char)addCode);
			} else {
				String[] spl = easySplit(addSymbolHexCodeInput, '-');
				int rangeStart = Integer.parseInt(spl[0], 16);
				int rangeEnd = Integer.parseInt(spl[1], 16);
				for (int c = rangeStart; c <= rangeEnd; c++) {
					getData().getEditingFont().addSymbol((char)c);
				}
				getData().setCurrentChar((char)rangeStart);
			}
			showMainScreen();
			
		} else if (key == Key.BACKSPACE && (state == KeyState.PRESS || state == KeyState.REPEAT) && modifiers.isEmpty()) {
			if (!addSymbolHexCodeInput.equals("")) {
				addSymbolHexCodeInput = addSymbolHexCodeInput.substring(0, addSymbolHexCodeInput.length() - 1);
			}
		}

	}
	
	@Override
	public void character(char character, Modifiers modifiers) {
		if (canTypeSymbolHexCodeInput(character)) {
			addSymbolHexCodeInput += character;
		}
	}

}
