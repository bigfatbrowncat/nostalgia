package nostalgia;

import java.util.Random;

import sun.awt.windows.ThemeReader;

public final class JNILayer {
	static {
		System.loadLibrary("nostalgia");
	}
	
	/**
	 * <p>This class is used for handling graphic events of the framework main event loop.</p>
	 * <p>You should subclass it, create an instance (you can do it inline) and pass this
	 * instance to {@link JNILayer#mainLoop JNILayer.mainLoop()}</p>
	 */
	public abstract static class FrameHandler {
		/**
		 * <p><em>This variable is used in JNI.
		 * Don't change the signature</em></p>
		 */
		private float[] r, g, b;
		private int pointsWidthCount, pointsHeightCount;
		
		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 * Sets the virtual screen size (in points)
		 * @param pointsWidthCount new width of the screen in points
		 * @param pointsHeightCount new height of the screen in points
		 */
		void setSize(int pointsWidthCount, int pointsHeightCount) {
			this.pointsWidthCount = pointsWidthCount;
			this.pointsHeightCount = pointsHeightCount;
			r = new float[pointsWidthCount * pointsHeightCount];
			g = new float[pointsWidthCount * pointsHeightCount];
			b = new float[pointsWidthCount * pointsHeightCount];
			sizeChanged();
		}
		
		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 * <p>This method is called by the framework each frame and should be
		 * implemented by the user of {@link JNILayer#mainLoop} 
		 * method to prepare the new screen contents.</p>
		 */
		public abstract void frame();
		/**
		 * <p>This method is called by the framework each time when the
		 * screen size is changed and could be implemented by user.</p>
		 * <p>Use {@link #getPointsWidthCount()} and {@link #getPointsHeightCount()} 
		 */
		public void sizeChanged() { }

		/**
		 * @return array of the Red color component of points. Indexed like <code>j*pointsWidthCount + i</code>.
		 * <p><em>Note: This array could (and should) be modified only from {@link #frame()} method.</em></p>
		 */
		protected float[] getR() {
			return r;
		}

		/**
		 * @return array of the Green color component of points. Indexed like <code>j*pointsWidthCount + i</code>.
		 * <p><em>Note: This array could (and should) be modified only from {@link #frame()} method.</em></p>
		 */
		protected float[] getG() {
			return g;
		}

		/**
		 * @return array of the Blue color component of points. Indexed like <code>j*pointsWidthCount + i</code>.
		 * <p><em>Note: This array could (and should) be modified only from {@link #frame()} method.</em></p>
		 */
		protected float[] getB() {
			return b;
		}

		/**
		 * @return The current virtual screen width in points. If you want to handle changes of this value,
		 * implement {@link #sizeChanged()} method.  
		 */
		protected int getPointsWidthCount() {
			return pointsWidthCount;
		}

		/**
		 * @return The current virtual screen height in points. If you want to handle changes of this value,
		 * implement {@link #sizeChanged()} method.  
		 */
		protected int getPointsHeightCount() {
			return pointsHeightCount;
		}
	}
	
	/**
	 * <p>This is the main method of the framework. It should be called <em>only once, on
	 * the main thread</em>. Additionally, if the app is run with the original JRE,
	 * you should use <code style="white-space: nowrap;">-XstartOnFirstThread</code> command line option.</p>
	 * <p>This method opens the main application window which creates a virtual screen 
	 * (full of beautiful square pixels inside), handles video events (such as screen resizes) and user input
	 * events (such as mouse movements and keyboard clicks). 
	 * 
	 * <h1>Usage example:</h3>
	 * <p>This code demonstrates the usage of {@link #mainLoop} function. It shows random color noise
	 * every frame</p>  
	 * <pre>
public static void main(String[] args) {
	int res = JNILayer.mainLoop("JNILayer Demo", 800, 600, 4, new FrameHandler() {
			
		private Random random = new Random();
			
		public void frame() {
			int pointsWidthCount = getPointsWidthCount();
			int pointsHeightCount = getPointsHeightCount();
			float[] r = getR();
			float[] g = getG();
			float[] b = getB();
				
			for (int i = 0; i < pointsWidthCount; i++) {
				for (int j = 0; j < pointsHeightCount; j++) {
					r[j * pointsWidthCount + i] = random.nextFloat();
					g[j * pointsWidthCount + i] = random.nextFloat();
					b[j * pointsWidthCount + i] = random.nextFloat();
				}
			}
		}
	});
}
	 * </pre>
	 * 
	 * @param title The main window title 
	 * @param windowWidth Initial width of the window in real pixels
	 * @param windowHeight Initial height of the window in real pixels
	 * @param pixelsPerPoint The size of a point in pixels (4, for example)
	 * @param frameHandler A user-made subclass of the {@link FrameHandler} object
	 * @return the exit code. If everything's fine it's zero.
	 * 
	 * 
	 */
	static native int mainLoop(String title, int windowWidth, int windowHeight, int pixelsPerPoint, FrameHandler frameHandler);
}
