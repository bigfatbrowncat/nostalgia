package nostalgia;

public final class JNILayer {
	static {
		System.loadLibrary("nostalgia");
	}
	
	/**
	 * <p>This class is used for handling events of the framework main event loop.</p>
	 * <p>You should subclass it, create an instance (you can do it inline) and pass this
	 * instance to {@link JNILayer#mainLoop JNILayer.mainLoop()}</p>
	 * @see {@link JNILayer#mainLoop JNILayer.mainLoop()}
	 */
	public abstract static class Handler {
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
		 * screen size is changed and could be implemented in subclass.</p>
		 * <p>Use {@link #getPointsWidthCount()} and {@link #getPointsHeightCount()} to
		 * determine the current virtual screen size</p>
		 */
		public void sizeChanged() { }

		/**
		 * <p>This method is called by the framework each time when the user
		 * moves the mouse while the application window is active.</p>
		 * <p>Use {@link #getPointsWidthCount()} and {@link #getPointsHeightCount()} 
		 * @param xPts x mouse coordinate in virtual points from the left size of
		 * the screen
		 * @param yPts y mouse coordinate in virtual points from the top side of
		 * the screen
		 */
		public void mouseMove(double xPts, double yPts) { }
		
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
	 * 
	 * @param title The main window title 
	 * @param windowWidth Initial width of the window in real pixels
	 * @param windowHeight Initial height of the window in real pixels
	 * @param pixelsPerPoint The size of a point in pixels (4, for example)
	 * @param frameHandler A user-made subclass of the {@link Handler} object
	 * @return <code>true</code> if the loop finished successfully, <code>false</code> otherwise. 
	 */
	static native boolean mainLoop(Handler frameHandler);
	
	static native boolean createWindow(String title, int windowWidth, int windowHeight, int pixelsPerPoint); 
	
	static native void setCursorVisibility(boolean visible);

}
