package letterplot;

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
	
	private static int letterWidth = 12, letterHeight = 16;
	private static boolean[] letterData = new boolean[letterWidth * letterHeight];
	
	private static float cellsX0, cellsY0, cellSize;
	
	public static void main(String[] args) {
		
		if (Core.open("LetterPlot", 800, 600, 2)) {
			Core.setCursorVisibility(false);
			boolean res = Core.run(new Handler() {
				
				private int mouseX = -10, mouseY = -10;
				
				public void frame() {
					try {
						Bitmap screen = getScreen();
						Painter p = new Painter(screen);
						p.setBackground(new Color(0.3f, 0.3f, 0.35f));
						p.drawRectangle(0, 0, screen.getWidth() - 1, screen.getHeight() - 1);
						
						p.setForeground(new Color(0.5f, 0.5f, 0.5f));
						for (int i = 0; i < letterWidth; i++) {
							for (int j = 0; j < letterHeight; j++) {
								if (letterData[j * letterWidth + i]) {
									p.setBackground(new Color(0.85f, 0.85f, 0.8f));
								} else {
									p.setBackground(new Color(0.2f, 0.2f, 0.2f));
								}

								p.drawRectangle(
										(int)(cellsX0 + cellSize * i), 
										(int)(cellsY0 + cellSize * j),
										(int)(cellsX0 + cellSize * (i + 1)), 
										(int)(cellsY0 + cellSize * (j + 1)));
							}
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

					int padding = 20;
					int widthAligned = getScreen().getWidth() - 2 * padding;
					int heightAligned = getScreen().getHeight() - 2 * padding;

					cellSize = Math.min((float)widthAligned / letterWidth, (float)heightAligned / letterHeight);
					cellsX0 = screen.getWidth() / 2 - cellSize * letterWidth / 2;
					cellsY0 = screen.getHeight() / 2 - cellSize * letterHeight / 2;
				}
				
				public void mouseMove(double xPts, double yPts) {
					mouseX = (int) Math.round(xPts);
					mouseY = (int) Math.round(yPts);
				}
				
				@Override
				public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
					if (state == MouseButtonState.PRESS) {
						if (mouseX > cellsX0 && mouseY > cellsY0 &&
						    mouseX < cellsX0 + cellSize * letterWidth &&
						    mouseY < cellsY0 + cellSize * letterHeight) {
							
							int i = (int) ((mouseX - cellsX0) / cellSize);
							int j = (int) ((mouseY - cellsY0) / cellSize);
							letterData[j * letterWidth + i] = !letterData[j * letterWidth + i];
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
