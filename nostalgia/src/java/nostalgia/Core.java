package nostalgia;

public final class Core {
	static {
		System.loadLibrary("nostalgia");
	}
	
	public static native void setHandler(Handler handler);
	
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
	 * @return <code>true</code> if the loop finished successfully, <code>false</code> otherwise. 
	 */
	public static native boolean run();
	
	/**
	 * Opens the main application window
	 * @param title the window's title
	 * @param windowWidth the window's initial width
	 * @param windowHeight the window's initial height
	 * @param pixelsPerPoint screen pixels per one virtual pixel
	 * @return <code>true</code> if it's created successively, <code>false</code> otherwise
	 */
	public static native boolean open(String title, int windowWidth, int windowHeight, int pixelsPerPoint); 
	
	/**
	 * Shows or hides the mouse pointer
	 * @param visible the cursor visibility value
	 */
	public static native void setCursorVisibility(boolean visible);

	/**
	 * Closes the window and ends the main loop
	 */
	public static native void close();
}
