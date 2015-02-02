package nostalgia;

public final class JNILayer {
	static {
		System.loadLibrary("nostalgia");
	}
	
	abstract static class FrameHandler {
		/**
		 * <p><em>This variable is used in JNI.
		 * Don't change the signature</em></p>
		 */
		private float[] r, g, b;
		private int pointsWidthCount, pointsHeightCount;
		
		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 * @param size the new size of the color arrays 
		 */
		void setSize(int pointsWidthCount, int pointsHeightCount) {
			this.pointsWidthCount = pointsWidthCount;
			this.pointsHeightCount = pointsHeightCount;
			r = new float[pointsWidthCount * pointsHeightCount];
			g = new float[pointsWidthCount * pointsHeightCount];
			b = new float[pointsWidthCount * pointsHeightCount];
		}
		
		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 * <p>This method is called by the framework each frame and should be
		 * implemented by the user of {@link JNILayer.mainLoop} 
		 * method to prepare the new screen contents.
		 * @param r array of the Red color component of points. Indexed like <code>j*pointsWidthCount + i</code>.
		 * @param g array of the Green color component of points. Indexed like <code>j*pointsWidthCount + i</code>.
		 * @param b array of the Blue color component of points. Indexed like <code>j*pointsWidthCount + i</code>.
		 * @param pointsWidthCount width of the screen in points
		 * @param pointsHeightCount height of the screen in points
		 */
		public abstract void frame();

		protected float[] getR() {
			return r;
		}

		protected float[] getG() {
			return g;
		}

		protected float[] getB() {
			return b;
		}

		protected int getPointsWidthCount() {
			return pointsWidthCount;
		}

		protected int getPointsHeightCount() {
			return pointsHeightCount;
		}
	}
	
	static native int mainLoop(String title, int windowWidth, int windowHeight, int pixelsPerPoint, FrameHandler frameHandler);
}
