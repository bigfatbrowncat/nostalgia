package nostalgia.tests;

import nostalgia.Core;
import nostalgia.Group;
import nostalgia.Handler;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public class SimpleTest {

	static Group group1 = new Group(100, 50, false) {
		@Override
		public boolean draw(Painter painter, boolean forced) {
			painter.setBackground(new Color(0, 0.5f, 0));
			painter.drawRectangle(5, 10, 15, 20);
			painter.setBackground(new Color(1, 0.5f, 0));
			painter.drawEllipse(5, 10, 15, 20);
			return true;
		}
	};
	final static Handler handler1 = new Handler() {
		
		@Override
		public void sizeChanged() {
			group1.resize(getPointsWidthCount(), getPointsHeightCount());
		}
		
		@Override
		public void frame() {
			group1.display(0, 0);
		}
	};

	public static void main(String[] args) {
		if (Core.open("JNILayerTest MainWindow", 800, 600, 2)) {
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
