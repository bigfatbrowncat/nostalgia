package nostalgia.graphics;


public class Painter {
	private final Bitmap bitmap;
	private Color foreground;
	private Color background;
	private Font font;
	
	public Painter(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		if (foreground != null) {
			if (x1 == x2) {
				if (x1 >= 0 && x1 < bitmap.getWidth() - 1) {
					int ymin = Math.min(y1, y2), ymax = Math.max(y1, y2);
					if (ymin < 0) ymin = 0;
					if (ymax > bitmap.getHeight() - 1) ymax = bitmap.getHeight() - 1;
					for (int j = ymin; j <= ymax; j++) {
						bitmap.setPixel(x1, j, foreground);
					}
				}
			} else if (y1 == y2) {
				if (y1 >= 0 && y1 < bitmap.getHeight() - 1) {
					int xmin = Math.min(x1, x2), xmax = Math.max(x1, x2);
					if (xmin < 0) xmin = 0;
					if (xmax > bitmap.getWidth() - 1) xmax = bitmap.getWidth() - 1;
					for (int i = xmin; i <= xmax; i++) {
						bitmap.setPixel(i, y1, foreground);
					}
				}
			} else {
				int ymin = Math.min(y1, y2), ymax = Math.max(y1, y2);
				int xmin = Math.min(x1, x2), xmax = Math.max(x1, x2);
				if (xmax - xmin < ymax - ymin) {
					double k = ((double)xmax - xmin) / (ymax - ymin);
					if ((x2 - x1) * (y2 - y1) > 0) {
						for (int j = ymin; j <= ymax; j++) {
							int i = (int) Math.round(((j - ymin) * k) + xmin);
							bitmap.setPixel(i, j, foreground);
						}
					} else {
						for (int j = ymax; j >= ymin; j--) {
							int i = (int) Math.round(-((j - ymin) * k) + xmax);
							bitmap.setPixel(i, j, foreground);
						}
					}
				} else {
					double k = ((double)ymax - ymin) / (xmax - xmin);
					if ((x2 - x1) * (y2 - y1) > 0) {
						for (int i = xmin; i <= xmax; i++) {
							int j = (int) Math.round(((i - xmin) * k) + ymin);
							bitmap.setPixel(i, j, foreground);
						}
					} else {
						for (int i = xmax; i >= xmin; i--) {
							int j = (int) Math.round(-((i - xmin) * k) + ymax);
							bitmap.setPixel(i, j, foreground);
						}
					}
					
				}
			}
		}
	}
	
	public void drawRectangle(int x1, int y1, int x2, int y2) {
		int xmin = Math.min(x1, x2), xmax = Math.max(x1, x2);
		int ymin = Math.min(y1, y2), ymax = Math.max(y1, y2);
		if (background != null) {
			int xmin2 = Math.max(xmin, 0);
			int xmax2 = Math.min(xmax, bitmap.getWidth() - 1);
			int ymin2 = Math.max(ymin, 0);
			int ymax2 = Math.min(ymax, bitmap.getHeight() - 1);
			for (int j = ymin2; j <= ymax2; j++) {
				for (int i = xmin2; i <= xmax2; i++) {
					bitmap.setPixel(i, j, background);
				}
			}
		}
		if (foreground != null) {
			drawLine(xmin, ymin, xmax, ymin);
			drawLine(xmin, ymax, xmax, ymax);
			drawLine(xmin, ymin, xmin, ymax);
			drawLine(xmax, ymin, xmax, ymax);
		}
	}
	
	/**
	 * This method paints this bitmap inside another one. 
	 * It checks any problems with edges (for example if you try
	 * to paint your image outside the other's boundaries, no
	 * out-of-range errors will occur. If alpha channel is presented
	 * 
	 * @param other 
	 * @param x0
	 * @param y0
	 */
	public void drawBitmap(Bitmap toDraw, int x0, int y0) {
		// Cutting edges
		int newOffset = toDraw.getOffset(), newWidth = toDraw.getWidth(), newHeight = toDraw.getHeight();
		int width = toDraw.getWidth(), height = toDraw.getHeight(), offset = toDraw.getOffset(), stride = toDraw.getStride();
		
		if (x0 < 0) {
			newOffset -= x0;
			newWidth += x0;
			x0 = 0;
		}
		
		if (x0 + width > bitmap.getWidth()) {
			newWidth -= x0 + width - bitmap.getWidth();
		}
		
		if (y0 < 0) {
			newOffset -= y0 * bitmap.getStride();
			newHeight += y0;
			y0 = 0;
		}
		
		if (y0 + height > bitmap.getHeight()) {
			newHeight -= y0 + height - bitmap.getHeight();
		}
		
		// Checking if we cut the whole image away
		if (newWidth <= 0 || newHeight <= 0 || newOffset + stride * newHeight > toDraw.getR().length) return;
		
		Bitmap tmpBmp;
		if (newOffset != offset || newWidth != width || newHeight != height) {
			tmpBmp = new Bitmap(toDraw.getR(), toDraw.getG(), toDraw.getB(), toDraw.getA(), newWidth, newHeight, newOffset, stride);
		} else {
			tmpBmp = toDraw;
		}
		
		int index = tmpBmp.getOffset();
		for (int j = 0; j < tmpBmp.getHeight(); j++) { 
			for (int i = 0; i < tmpBmp.getWidth(); i++) {
				if (tmpBmp.getA() != null) {
					bitmap.setPixel(x0 + i, y0 + j, tmpBmp.getR()[index + i], tmpBmp.getG()[index + i], tmpBmp.getB()[index + i], tmpBmp.getA()[index + i]);
				} else {
					bitmap.setPixel(x0 + i, y0 + j, tmpBmp.getR()[index + i], tmpBmp.getG()[index + i], tmpBmp.getB()[index + i]);
				}
			}
			index += tmpBmp.getStride();
		}
	}
	
	public void drawEllipse(int x1, int y1, int x2, int y2) {
		// Ranges
		int x1r = Math.min(x1, x2), x2r = Math.max(x1, x2);
		int y1r = Math.min(y1, y2), y2r = Math.max(y1, y2);

		// Screen mins and maxes
		int x1a = Math.max(x1r, 0), x2a = Math.min(x2r, getBitmap().getWidth() - 1);
		int y1a = Math.max(y1r, 0), y2a = Math.min(y2r, getBitmap().getHeight() - 1);

		float xc = (float)(x2r + x1r) / 2, yc = (float)(y2r + y1r) / 2;
		
		// Drawing
		if (background != null) {

			if (y1 == y2) {
				// We have a line here
				for (int i = x1r; i <= x2r; i++) {
					bitmap.setPixel(i, y1, background);
				}
			} else {
				float k = (float)(x2r - x1r) / (y2r - y1r);
				float rsqr = (float)(y2r - y1r + 0.3f)*(y2r - y1r + 0.3f) / 4;
				for (int i = x1a; i <= x2a; i++) {
					for (int j = y1a; j <= y2a; j++) {
						if ((i - xc)*(i - xc)/k/k + (j - yc)*(j - yc) <= rsqr) {
							bitmap.setPixel(i, j, background);
						}
					}
				}
			}
		}
		
		
	}
	
	public int stringWidth(String s) {
		return s.length() * font.getWidth();
	}
	
	public void drawString(int x0, int y0, String s) {
		if (foreground != null) {
			int x = x0;
			char[] chars = s.toCharArray();
			for (int k = 0; k < chars.length; k++) {
				boolean[] symbol = font.getSymbol(chars[k]);
				if (symbol != null) {
					for (int i = 0; i < font.getWidth(); i++) {
						for (int j = 0; j < font.getHeight(); j++) {
							if (symbol[j * font.getWidth() + i]) {
								bitmap.setPixel(x + i, y0 + j, foreground);
							}
						}
					}
				}
				x += font.getWidth();
			}
		}
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public Font getFont() {
		return font;
	}
}
