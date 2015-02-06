package nostalgia;

import java.util.EnumSet;
import java.util.Iterator;

import nostalgia.graphics.Bitmap;

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
		 * Mouse buttons
		 */
		public enum MouseButton {
			LEFT(0), RIGHT(1), MIDDLE(2),
			BUTTON_4(3), BUTTON_5(4), BUTTON_6(5), BUTTON_7(6), BUTTON_8(7);
			final int id;
			MouseButton(int id) {
				this.id = id;
			}
			static MouseButton fromId(int id) {
				switch (id) {
				case 0: return LEFT;
				case 1: return RIGHT;
				case 2: return MIDDLE;
				case 3: return BUTTON_4;
				case 4: return BUTTON_5;
				case 5: return BUTTON_6;
				case 6: return BUTTON_7;
				case 7: return BUTTON_8;
				default: throw new IllegalArgumentException("incorrect id");
				}
				
			}
		}
		
		/**
		 * Mouse button states
		 */
		public enum MouseButtonState {
			RELEASE(0), PRESS(1);
			final int id;
			MouseButtonState(int id) {
				this.id = id;
			}
			static MouseButtonState fromId(int id) {
				switch (id) {
				case 0: return RELEASE;
				case 1: return PRESS;
				default: throw new IllegalArgumentException("incorrect id");
				}
			}
		}
		
		public enum Modifier {
			SHIFT(0x0001), CONTROL(0x0002), ALT(0x0004), SUPER(0x0008);
			final int value;
			Modifier(int value) {
				this.value = value;
			}
			static Modifier fromId(int id) {
				switch (id) {
				case 0x0001: return SHIFT;
				case 0x0002: return CONTROL;
				case 0x0004: return ALT;
				case 0x0008: return SUPER;
				default: throw new IllegalArgumentException("incorrect id");
				}
			}
		}
		
		public static class Modifiers {
			private final EnumSet<Modifier> values;
			public Modifiers(EnumSet<Modifier> values) {
				this.values = values.clone();
			}
			static Modifiers fromFlags(int flags) {
				EnumSet<Modifier> values = EnumSet.noneOf(Modifier.class);
				if ((flags & Modifier.SHIFT.value) != 0) values.add(Modifier.SHIFT);
				if ((flags & Modifier.CONTROL.value) != 0) values.add(Modifier.CONTROL);
				if ((flags & Modifier.ALT.value) != 0) values.add(Modifier.ALT);
				if ((flags & Modifier.SUPER.value) != 0) values.add(Modifier.SUPER);
				return new Modifiers(values);
			}
			public boolean contains(Modifier mod) {
				return values.contains(mod);
			}
			
			int toFlags() {
				int res = 0;
				for (Modifier mod : values) {
					res |= mod.value;
				}
				return res;
			}
			@Override
			public String toString() {
				if (values.size() == 0) return "()";
				Iterator<Modifier> iter = values.iterator(); 
				String s = "(" + iter.next();
				while (iter.hasNext()) {
					s += ", " + iter.next();
				}
				return s += ")";
			}
		}

		/**
		 * <p><em>This variable is used in JNI.
		 * Don't change the signature</em></p>
		 */
		private Bitmap screen;
		private float[] r, g, b;
		
		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 * Sets the virtual screen size (in points)
		 * @param pointsWidthCount new width of the screen in points
		 * @param pointsHeightCount new height of the screen in points
		 */
		void setSize(int pointsWidthCount, int pointsHeightCount) {
			r = new float[pointsWidthCount * pointsHeightCount];
			g = new float[pointsWidthCount * pointsHeightCount];
			b = new float[pointsWidthCount * pointsHeightCount];
			screen = new Bitmap(r, g, b, null, pointsWidthCount, pointsHeightCount);
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
		 * <p>Use {@link #getScreen()} to determine the current virtual 
		 * screen size</p>
		 */
		public void sizeChanged() { }

		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 * <p>This method is called by the framework each time when the user
		 * moves the mouse while the application window is active.</p>
		 * @param xPts x mouse coordinate in virtual points from the left size of
		 * the screen
		 * @param yPts y mouse coordinate in virtual points from the top side of
		 * the screen
		 */
		public void mouseMove(double xPts, double yPts) { }
		
		/**
		 * <p><em>This method is called from JNI.
		 * Don't change the signature</em></p>
		 */
		void innerMouseButton(int btn, int st, int mod) {
			mouseButton(MouseButton.fromId(btn), MouseButtonState.fromId(st), Modifiers.fromFlags(mod));
		}
		
		/**
		 * <p>This method is called by the framework each time when the user
		 * clicks or releases a mouse button in the application window.</p>
		 * @param button the button which state is changed
		 * @param state the new button state
		 * @param modifiers special keys that were pressed on the keyboard when the event occurred
		 */
		public void mouseButton(MouseButton button, MouseButtonState state, Modifiers modifiers) { }
		
		/**
		 * @return The screen bitmap. Draw here to make any changes to the screen.
		 */
		public Bitmap getScreen() {
			return screen;
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
