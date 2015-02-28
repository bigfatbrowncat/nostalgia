package nostalgia;

import nostalgia.graphics.Bitmap;

/**
 * <p><em>This class is called from JNI.
 * Don't change the signature</em></p>
 * <p>This class is used for handling events of the framework main event loop.</p>
 * <p>You should subclass it, create an instance (you can do it inline) and pass this
 * instance to {@link Core#mainLoop JNILayer.mainLoop()}</p>
 * @see {@link Core#mainLoop JNILayer.mainLoop()}
 */
public class Group {
	static {
		System.loadLibrary("nostalgia");
	}

	private long nativeAddress;
	private native long createNative();
	private native void destroyNative(long nativeAddress);

	public Group(int width, int height) {
		nativeAddress = createNative();
		pointsWidthCount = width;
		pointsHeightCount = height;
	}

	@Override
	protected void finalize() throws Throwable {
		if (nativeAddress != 0) {
			destroyNative(nativeAddress);
			nativeAddress = 0;
		}
		super.finalize();
	}
	
	/**
	 * <p><em>This variable is used in JNI.
	 * Don't change the signature</em></p>
	 */
	private Bitmap bitmap;
	
	/**
	 * <p><em>This variable is used in JNI.
	 * Don't change the signature</em></p>
	 */
	private float[] r, g, b;

	/**
	 * <p><em>This variable is used in JNI.
	 * Don't change the signature</em></p>
	 */
	private int pointsWidthCount, pointsHeightCount;
	
	private boolean initial = true;
	
	/**
	 * <p><em>This method is called from JNI.
	 * Don't change the signature</em></p>
	 */
	private boolean innerDraw() {
		if (bitmap == null) {
			resize(pointsWidthCount, pointsHeightCount);
		}
		boolean res = draw(bitmap, initial);
		initial = false;
		return res;
	}
	
	public boolean draw(Bitmap bitmap, boolean initial) {
		return false;
	}
	
	private native void innerResize(int width, int height);
	
	public void resize(int width, int height) {
		initial = true;
		pointsWidthCount = width;
		pointsHeightCount = height;
		
		if (bitmap == null) {
			bitmap = Bitmap.createWithoutAlpha(width, height);
		} else {
			if (bitmap.getR().length < width * height) {
				bitmap = Bitmap.createWithoutAlpha(width, height);
			} else {
				bitmap = new Bitmap(bitmap.getR(), bitmap.getG(), bitmap.getB(), null, width, height);
			}
		}
		
		this.r = bitmap.getR();
		this.g = bitmap.getG();
		this.b = bitmap.getB();
		
		innerResize(width, height);
	}
	
	public native void display();
}
