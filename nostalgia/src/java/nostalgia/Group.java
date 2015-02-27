package nostalgia;

import java.io.Closeable;
import java.io.IOException;

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

	public Group() {
		nativeAddress = createNative();
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
	
	/**
	 * @return The screen bitmap. Draw here to make any changes to the screen.
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public native void updateRGB();
	private native void innerResize(int width, int height);
	
	public void resize(int width, int height) {
		pointsWidthCount = width;
		pointsHeightCount = height;
		
		Bitmap bmp = getBitmap();
		if (bmp == null) {
			bmp = Bitmap.createWithoutAlpha(width, height);
		} else {
			if (bmp.getR().length < width * height) {
				bmp = Bitmap.createWithoutAlpha(width, height);
			} else {
				bmp = new Bitmap(bmp.getR(), bmp.getG(), bmp.getB(), null, width, height);
			}
		}
		
		this.r = bmp.getR();
		this.g = bmp.getG();
		this.b = bmp.getB();
		this.bitmap = bmp;
		
		innerResize(width, height);
	}
	
	public native void display();
}
