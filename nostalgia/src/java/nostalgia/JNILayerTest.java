package nostalgia;

import java.util.Random;

import nostalgia.JNILayer.Handler;

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

				
				public void frame() {
					try {
						int pointsWidthCount = getPointsWidthCount();
						int pointsHeightCount = getPointsHeightCount();
						float[] r = getR();
						float[] g = getG();
						float[] b = getB();
						
						Bitmap screen = new Bitmap(r, g, b, null, pointsWidthCount, pointsHeightCount);
						for (int i = 0; i < pointsWidthCount; i++) {
							for (int j = 0; j < pointsHeightCount; j++) {
								r[j * pointsWidthCount + i] = 0.5f;
								g[j * pointsWidthCount + i] = 0.5f;
								b[j * pointsWidthCount + i] = 0.5f;
							}
						}
						
						if (zoomed) {
							ls = 0; ts = 0; rs = pointsWidthCount - 1; bs = pointsHeightCount - 1;
						} else {
							ls = pointsWidthCount / 2 - 30; rs = pointsWidthCount / 2 + 30;
							ts = pointsHeightCount / 2 - 25; bs = pointsHeightCount / 2 + 25; 
						}
						
						for (int i = ls; i <= rs; i++) {
							for (int j = ts; j <= bs; j++) {
								r[j * pointsWidthCount + i] = random.nextFloat() * 0.8f;
								g[j * pointsWidthCount + i] = random.nextFloat() * 0.8f;
								b[j * pointsWidthCount + i] = random.nextFloat() * 0.8f;
							}
						}
						
						cursor.blit(screen, mouseX, mouseY);
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
					if (mouseX > ls && mouseX < rs && mouseY > ts && mouseY < bs && state == MouseButtonState.RELEASE) {
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
