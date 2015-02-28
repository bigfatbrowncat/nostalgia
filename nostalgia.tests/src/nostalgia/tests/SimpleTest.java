package nostalgia.tests;

import nostalgia.Core;
import nostalgia.Group;
import nostalgia.Handler;

public class SimpleTest {

	static Group group1;
	final static Handler handler1 = new Handler() {
		
		@Override
		public void sizeChanged() {
			group1.resize(getPointsWidthCount(), getPointsHeightCount());
		}
		
		@Override
		public void frame() {
			group1.display();
		}
	};

	public static void main(String[] args) {
		if (Core.open("JNILayerTest MainWindow", 800, 600, 2)) {
			group1 = new Group(800, 600);			
			Core.setCursorVisibility(false);
			
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
