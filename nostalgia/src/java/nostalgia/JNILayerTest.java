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
				private int mouseX = 3, mouseY = 3;
				
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
