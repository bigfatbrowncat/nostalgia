package letterplot.views;

import java.util.ArrayList;

import letterplot.Constants;
import letterplot.model.Data;
import nostalgia.Group;
import nostalgia.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public abstract class BaseHandler extends Handler {

	private int mouseX = -10, mouseY = -10;
//	private static float cellsX0, cellsY0, cellSize;
	private Boolean drawingColor = null;

	private BaseGroup mainGroup;
	
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
		
	private Data data;

	public BaseHandler(Data data) {
		this.data = data;
	}
	
	public void setMainGroup(BaseGroup mainGroup) {
		this.mainGroup = mainGroup;
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
	
	protected BaseGroup getMainGroup() {
		return mainGroup;
	}
	
	
	@Override
	public void sizeChanged() {
		mainGroup.resize(getPointsWidthCount(), getPointsHeightCount());
	}
	
	@Override
	public void mouseMove(double xPts, double yPts) {
		mouseX = (int) Math.round(xPts);
		mouseY = (int) Math.round(yPts);
		boolean[] currentLetter = data.getEditingFont().getSymbol(data.getCurrentChar());
		if (drawingColor != null) {
			if (mainGroup.isInCells(mouseX, mouseY)) {
				int i = mainGroup.cellX(mouseX); 
				int j = mainGroup.cellY(mouseY);
				currentLetter[j * data.getEditingFont().getWidth() + i] = drawingColor;
			}
		}
	}
	
	@Override
	public void frame() {
		mainGroup.display();
	}
	
	protected Boolean getDrawingColor() {
		return drawingColor;
	}
	
	protected void setDrawingColor(Boolean drawingColor) {
		this.drawingColor = drawingColor;
	}
}
