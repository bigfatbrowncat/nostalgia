package nostalgia;

import java.util.EnumSet;
import java.util.Iterator;

import nostalgia.graphics.Bitmap;

/**
 * <p><em>This class is called from JNI.
 * Don't change the signature</em></p>
 * <p>This class is used for handling events of the framework main event loop.</p>
 * <p>You should subclass it, create an instance (you can do it inline) and pass this
 * instance to {@link Core#mainLoop JNILayer.mainLoop()}</p>
 * @see {@link Core#mainLoop JNILayer.mainLoop()}
 */
public abstract class Handler {
	static {
		System.loadLibrary("nostalgia");
	}

	private boolean freed = false;
	private long nativeAddress;
	private native long createNative();
	private native void destroyNative(long nativeAddress);
	
	public Handler() {
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
			for (MouseButton v : MouseButton.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Keyboard key value
	 */
	public enum Key {
		UNKNOWN            (-1),

		/* Printable keys */
		SPACE              (32),
		APOSTROPHE         (39),  /* ' */
		COMMA              (44),  /* , */
		MINUS              (45),  /* - */
		PERIOD             (46),  /* . */
		SLASH              (47),  /* / */
		TOP_0              (48),
		TOP_1              (49),
		TOP_2              (50),
		TOP_3              (51),
		TOP_4              (52),
		TOP_5              (53),
		TOP_6              (54),
		TOP_7              (55),
		TOP_8              (56),
		TOP_9              (57),
		SEMICOLON          (59),  /* ; */
		EQUAL              (61),  /* = */
		A                  (65),
		B                  (66),
		C                  (67),
		D                  (68),
		E                  (69),
		F                  (70),
		G                  (71),
		H                  (72),
		I                  (73),
		J                  (74),
		K                  (75),
		L                  (76),
		M                  (77),
		N                  (78),
		O                  (79),
		P                  (80),
		Q                  (81),
		R                  (82),
		S                  (83),
		T                  (84),
		U                  (85),
		V                  (86),
		W                  (87),
		X                  (88),
		Y                  (89),
		Z                  (90),
		LEFT_BRACKET       (91),  /* [ */
		BACKSLASH          (92),  /* \ */
		RIGHT_BRACKET      (93),  /* ] */
		GRAVE_ACCENT       (96),  /* ` */
		WORLD_1            (161), /* non-US #1 */
		WORLD_2            (162), /* non-US #2 */

		/* Function keys */
		ESCAPE             (256),
		ENTER              (257),
		TAB                (258),
		BACKSPACE          (259),
		INSERT             (260),
		DELETE             (261),
		RIGHT              (262),
		LEFT               (263),
		DOWN               (264),
		UP                 (265),
		PAGE_UP            (266),
		PAGE_DOWN          (267),
		HOME               (268),
		END                (269),
		CAPS_LOCK          (280),
		SCROLL_LOCK        (281),
		NUM_LOCK           (282),
		PRINT_SCREEN       (283),
		PAUSE              (284),
		F1                 (290),
		F2                 (291),
		F3                 (292),
		F4                 (293),
		F5                 (294),
		F6                 (295),
		F7                 (296),
		F8                 (297),
		F9                 (298),
		F10                (299),
		F11                (300),
		F12                (301),
		F13                (302),
		F14                (303),
		F15                (304),
		F16                (305),
		F17                (306),
		F18                (307),
		F19                (308),
		F20                (309),
		F21                (310),
		F22                (311),
		F23                (312),
		F24                (313),
		F25                (314),
		KP_0               (320),
		KP_1               (321),
		KP_2               (322),
		KP_3               (323),
		KP_4               (324),
		KP_5               (325),
		KP_6               (326),
		KP_7               (327),
		KP_8               (328),
		KP_9               (329),
		KP_DECIMAL         (330),
		KP_DIVIDE          (331),
		KP_MULTIPLY        (332),
		KP_SUBTRACT        (333),
		KP_ADD             (334),
		KP_ENTER           (335),
		KP_EQUAL           (336),
		LEFT_SHIFT         (340),
		LEFT_CONTROL       (341),
		LEFT_ALT           (342),
		LEFT_SUPER         (343),
		RIGHT_SHIFT        (344),
		RIGHT_CONTROL      (345),
		RIGHT_ALT          (346),
		RIGHT_SUPER        (347),
		MENU               (348);
		
		final int id;
		Key(int id) {
			this.id = id;
		}
		static Key fromId(int id) {
			for (Key v : Key.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
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
			for (MouseButtonState v : MouseButtonState.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Keyboard key states
	 */
	public enum KeyState {
		RELEASE(0), PRESS(1), REPEAT(2);
		final int id;
		KeyState(int id) {
			this.id = id;
		}
		static KeyState fromId(int id) {
			for (KeyState v : KeyState.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
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
		public boolean isEmpty() {
			return values.isEmpty();
		}
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Modifiers) {
				if (((Modifiers) obj).values.size() == values.size()) {
					for (Modifier v : values) {
						if (!((Modifiers) obj).values.contains(v)) return false;
					}
					return true;
				}
			}
			return false;
		}
		
		public static Modifiers of(Modifier mod) {
			return new Modifiers(EnumSet.of(mod));
		}
		public static Modifiers of(Modifier mod1, Modifier... rest) {
			return new Modifiers(EnumSet.of(mod1, rest));
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
	private int pointsWidthCount, pointsHeightCount;
	
	/**
	 * <p><em>This method is called from JNI.
	 * Don't change the signature</em></p>
	 * Sets the virtual screen size (in points)
	 * @param pointsWidthCount new width of the screen in points
	 * @param pointsHeightCount new height of the screen in points
	 */
	void innerResize(int pointsWidthCount, int pointsHeightCount) {
		this.pointsWidthCount = pointsWidthCount;
		this.pointsHeightCount = pointsHeightCount;

		sizeChanged();
	}
	
	/**
	 * <p><em>This method is called from JNI.
	 * Don't change the signature</em></p>
	 * <p>This method is called by the framework each frame and should be
	 * implemented by the user of {@link Core#mainLoop} 
	 * method to prepare the new screen contents.</p>
	 */
	public abstract void frame();

	/**
	 * <p>This method is called by the framework each time when the
	 * screen size is changed and could be implemented in subclass.</p>
	 * <p>Use {@link #getBitmap()} to determine the current virtual 
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
	 * <p><em>This method is called from JNI.
	 * Don't change the signature</em></p>
	 */
	void innerKey(int key, int scancode, int action, int mods) {
		key(Key.fromId(key), scancode, KeyState.fromId(action), Modifiers.fromFlags(mods));
	}
	
	/**
	 * <p><em>This method is called from JNI.
	 * Don't change the signature</em></p>
	 */
	void innerCharacter(char character, int mods) {
		character(character, Modifiers.fromFlags(mods));
	}
	
	/**
	 * <p>This method is called by the framework each time when the user
	 * clicks or releases a mouse button in the application window.</p>
	 * @param button the button which state is changed
	 * @param state the new button state
	 * @param modifiers special keys that were pressed on the keyboard when the event occurred
	 */
	public void mouseButton(MouseButton button, MouseButtonState state, Modifiers modifiers) { }
	
	public void key(Key key, int scancode, KeyState state, Modifiers modifiers) { }
	
	public void character(char character, Modifiers modifiers) { }

	public int getPointsWidthCount() {
		return pointsWidthCount;
	}
	public int getPointsHeightCount() {
		return pointsHeightCount;
	}
}
