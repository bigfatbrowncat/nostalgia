package nostalgia;

import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Painter;

/**
 * <p><em>This class is called from JNI.
 * Don't change the signature</em></p>
 * <p>This class is used for drawing something </p>
 */
public class Group {
	static {
		System.loadLibrary("nostalgia");
	}

	private long nativeAddress;
	private native long createNative(boolean hasAlpha);
	private native void destroyNative(long nativeAddress);

	public Group(int width, int height, boolean hasAlpha) {
		nativeAddress = createNative(hasAlpha);
		this.width = width;
		this.height = height;
		this.hasAlpha = hasAlpha;
	}

	@Override
	protected void finalize() throws Throwable {
		if (nativeAddress != 0) {
			destroyNative(nativeAddress);
			nativeAddress = 0;
		}
		super.finalize();
	}
	
	private Bitmap bitmap;
	private Painter painter;
	private boolean hasAlpha;
	
	/**
	 * <p><em>This variable is used in JNI.
	 * Don't change the signature</em></p>
	 */
	private float[] r, g, b, a;

	/**
	 * <p><em>This variable is used in JNI.
	 * Don't change the signature</em></p>
	 */
	private int width, height;
	
	private boolean forcedDraw = true;
	
	/**
	 * <p><em>This method is called from JNI.
	 * Don't change the signature</em></p>
	 */
	private boolean innerDraw() {
		if (bitmap == null) {
			resize(width, height);
		}
		if (painter == null || painter.getBitmap() != bitmap) {
			painter = new Painter(bitmap);
		}
		boolean res = draw(painter, forcedDraw);
		forcedDraw = false;
		return res;
	}
	
	/**
	 * <p>This function should be implemented in a {@link Group} subclass in order to paint its
	 * contents. All the painting code should be placed here. Don't pass the <code>painter</code>
	 * object outside since it is controlled entirely by the framework and nothing guaranteed
	 * about its state outside this function.</p>
	 * <p>If you want to operate on the screen bitmap directly, use {@link Painter#getBitmap Painter.getBitmap()}
	 * function. But don't pass the returned {@link Bitmap} object out of the function as well.</p>
	 * @param painter a {@link Painter} that should be used to draw the contents
	 * @param forced this flag is <code>true</code> if the buffer bitmap has been invalidated somehow 
	 * (for instance if its size was changed) and the object has to redraw itself.  
	 * @return The subclass should return <code>true</code> if it has changed something
	 * in the image and wants to commit the changes. Return <code>false</code> if you haven't painted
	 * anything (that will avoid expensive image rebuilding and boost performance). 
	 */
	public boolean draw(Painter painter, boolean forced) {
		return false;
	}
	
	private native void innerResize(int width, int height);
	
	public void resize(int width, int height) {
		forcedDraw = true;
		this.width = width;
		this.height = height;
		
		if (bitmap == null) {
			bitmap = Bitmap.create(width, height, hasAlpha);
		} else {
			if (bitmap.getR().length < width * height) {
				bitmap = Bitmap.create(width, height, hasAlpha);
			} else {
				bitmap = new Bitmap(bitmap.getR(), bitmap.getG(), bitmap.getB(), bitmap.getA(), width, height);
			}
		}
		
		this.r = bitmap.getR();
		this.g = bitmap.getG();
		this.b = bitmap.getB();
		this.a = bitmap.getA();
		
		innerResize(width, height);
	}
	
	public native void display(float x, float y);
}
