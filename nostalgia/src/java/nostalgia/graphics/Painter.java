package nostalgia.graphics;


public class Painter {
	private final Bitmap bitmap;
	private Color foreground;
	private Color background;
	
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
		if (foreground != null) {
			drawLine(xmin, ymin, xmax, ymin);
			drawLine(xmin, ymax, xmax, ymax);
			drawLine(xmin, ymin, xmin, ymax);
			drawLine(xmax, ymin, xmax, ymax);
		}
		if (background != null) {
			xmin = Math.max(xmin, 0);
			xmax = Math.min(xmax, bitmap.getWidth() - 1);
			ymin = Math.max(ymin, 0);
			ymax = Math.min(ymax, bitmap.getHeight() - 1);
			for (int j = ymin; j <= ymax; j++) {
				for (int i = xmin; i <= xmax; i++) {
					bitmap.setPixel(i, j, background);
				}
			}
		}
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
	
}
