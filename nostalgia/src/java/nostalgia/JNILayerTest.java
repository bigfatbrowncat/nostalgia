package nostalgia;

import java.util.Random;

import nostalgia.JNILayer.FrameHandler;

public class JNILayerTest {
	public static void main(String[] args) {
		int res = JNILayer.mainLoop("JNILayerTest MainWindow", 800, 600, 4, new FrameHandler() {
			
			private Random random = new Random();
			
			public void frame() {
				int pointsWidthCount = getPointsWidthCount();
				int pointsHeightCount = getPointsHeightCount();
				float[] r = getR();
				float[] g = getG();
				float[] b = getB();
				
				for (int i = 0; i < pointsWidthCount; i++) {
					for (int j = 0; j < pointsHeightCount; j++) {
						/*r[j * pointsWidthCount + i] = (float)i / pointsWidthCount;
						g[j * pointsWidthCount + i] = (float)j / pointsHeightCount;
						b[j * pointsWidthCount + i] = 0.5f;*/
						
						r[j * pointsWidthCount + i] = random.nextFloat();
						g[j * pointsWidthCount + i] = random.nextFloat();
						b[j * pointsWidthCount + i] = random.nextFloat();
					}
				}
			}
			
		});
		System.out.println("mainLoop exited with code " + res);
	}
}
