package nostalgia;

import java.util.Random;

import nostalgia.JNILayer.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public class JNILayerTest {
	public static void main(String[] args) {
		
		int cW = 8, cH = 8;
		
		float[] c = new float[] {
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
		};
		float[] a = new float[] {
			1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
		};
		
		final Bitmap cursor = new Bitmap(c, c, c, a, cW, cH);
		
		if (JNILayer.createWindow("JNILayerTest MainWindow", 800, 600, 2)) {
			JNILayer.setCursorVisibility(false);
			boolean res = JNILayer.mainLoop(new Handler() {
				
				private Random random = new Random();
				private int mouseX = -10, mouseY = -10;
				
				private boolean zoomed = false;
				private int ls, ts, rs, bs;
				private int clickedX, clickedY;
				
				public void frame() {
					try {
						Bitmap screen = getScreen();
						Painter p = new Painter(screen);
						p.setForeground(null);
						p.setBackground(new Color(0.5f, 0.5f, 0.5f));
						p.drawRectangle(0, 0, screen.getWidth() - 1, screen.getHeight() - 1);
						
						if (zoomed) {
							ls = 0; ts = 0; rs = screen.getWidth() - 1; bs = screen.getHeight() - 1;
						} else {
							ls = screen.getWidth() / 2 - 30; rs = screen.getWidth() / 2 + 30;
							ts = screen.getHeight() / 2 - 25; bs = screen.getHeight() / 2 + 25; 
						}
						
						Bitmap randomPix = Bitmap.createWithoutAlpha(rs - ls, bs - ts); 
						float[] r = randomPix.getR(), g = randomPix.getG(), b = randomPix.getB();
						for (int i = 0; i < randomPix.getWidth(); i++) {
							for (int j = 0; j < randomPix.getHeight(); j++) {
								r[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
								g[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
								b[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
							}
						}
						randomPix.blit(screen, ls, ts);
						
						cursor.blit(screen, mouseX, mouseY);
						
						p.setForeground(new Color(0.5f, 0f, 0f, 0.5f));
						p.drawLine(10, 10, 10, 100);
						p.drawLine(40, 100, 20, 10);
						p.drawLine(40, 10, 20, 100);
						p.drawLine(20, 120, 120, 140);
						p.drawLine(20, 140, 120, 120);
						p.drawLine(60, 10, 100, 50);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				public void mouseMove(double xPts, double yPts) {
					mouseX = (int) Math.round(xPts);
					mouseY = (int) Math.round(yPts);
				}
				
				@Override
				public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
					if (state == MouseButtonState.PRESS) {
						clickedX = mouseX;
						clickedY = mouseY;
					}
					
					if (state == MouseButtonState.RELEASE && clickedX > ls && clickedX < rs && clickedY > ts && clickedY < bs) {
						zoomed = !zoomed;
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
