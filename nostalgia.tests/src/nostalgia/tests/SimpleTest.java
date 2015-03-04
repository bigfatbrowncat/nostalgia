package nostalgia.tests;

import nostalgia.Core;
import nostalgia.Group;
import nostalgia.Handler;
import nostalgia.Transform;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public class SimpleTest {

	static Group group1 = new Group(20, 20, false) {
		@Override
		public boolean draw(Painter painter, boolean forced) {
			painter.setBackground(new Color(0, 0.5f, 0));
			painter.drawRectangle(0, 0, 19, 19);
			painter.setBackground(new Color(1, 0.5f, 0));
			painter.drawEllipse(0, 0, 19, 19);
			return true;
		}
	};
	final static Handler handler1 = new Handler() {
		
		@Override
		public void sizeChanged() {
			//group1.resize(getPointsWidthCount(), getPointsHeightCount());
		}
		
		private float angle = 0;
		private long millisStart = System.currentTimeMillis();
		
		@Override
		public void frame() {
			angle = (float)(System.currentTimeMillis() - millisStart) / 1000;
			group1.display( Transform.multiply(Transform.translate(getPointsWidthCount() / 2 - group1.getWidth() / 2, -getPointsHeightCount() / 2 + group1.getHeight() / 2, 0), Transform.rotate(angle, 0, 0, 1)));
		}
	};

	public static void main(String[] args) {
		if (Core.open("JNILayerTest MainWindow", 800, 600, 4)) {
			Core.setHandler(handler1);
			
			boolean res = Core.run();
			
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
