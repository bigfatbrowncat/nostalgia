package letterplot;

import java.io.IOException;

import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Font;

public final class Constants {
	private static int cW = 8, cH = 8;
	
	private static float[] c = new float[] {
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
	};
	private static float[] a = new float[] {
		1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
	};
	
	public static final Bitmap ARROW_CURSOR = new Bitmap(c, c, c, a, cW, cH);

	public static final Color FORE_COLOR = new Color(0.85f, 0.85f, 0.8f); 
	public static final Color BACK_COLOR = new Color(0.2f, 0.2f, 0.2f); 
	public static final Color FORE_HIGHLIGHT_COLOR = new Color(0.5f, 0.85f, 0.6f); 
	public static final Color BACK_HIGHLIGHT_COLOR = new Color(0.25f, 0.5f, 0.3f); 
	public static final Color BACK_SCREEN_COLOR = new Color(0.3f, 0.3f, 0.35f);
	public static final Color DIALOG_BACK_COLOR = new Color(0.2f, 0.2f, 0.25f, 0.85f); 

	public static final Font SYSTEM_FONT;
	
	static {
		Font systemFont = null;
		
		try {
			systemFont = Font.fromStream(Font.class.getResource("font6x8.dat").openStream());
		} catch (IOException e) {
			System.err.println("Can't load the default system font");
			e.printStackTrace();
		}
		
		SYSTEM_FONT = systemFont;
	}

}
