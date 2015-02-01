package nostalgia;

public final class JNILayer {
	static {
		System.loadLibrary("nostalgia");
	}
	static native int mainLoop(String title, int windowWidth, int windowHeight, int pixelsPerPoint);
}
