package nostalgia.tests;

import java.util.Random;

import nostalgia.Core;
import nostalgia.Group;
import nostalgia.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public class CoreTest {
	
	private static Random random = new Random();
	private static int mouseX = -10, mouseY = -10;

	private static Group noiseGroup = new Group(80, 60) {
		@Override
		public boolean draw(Painter p, boolean initial) {
			Bitmap bitmap = p.getBitmap();
			p.setForeground(null);
			p.setBackground(new Color(0.5f, 0.5f, 0.5f));
			p.drawRectangle(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
			
			Bitmap randomPix = Bitmap.createWithoutAlpha(bitmap.getWidth(), bitmap.getHeight()); 
			float[] r = randomPix.getR(), g = randomPix.getG(), b = randomPix.getB();
			for (int i = 0; i < randomPix.getWidth(); i++) {
				for (int j = 0; j < randomPix.getHeight(); j++) {
					r[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
					g[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
					b[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
				}
			}
			p.drawBitmap(randomPix, 0, 0);
			return true;
		}
	};
	
	private static Group cursorGroup = new Group(8, 8) {
		
		private static final int cW = 8, cH = 8;
		
		private final float[] c = new float[] {
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
		};
		private final float[] a = new float[] {
			1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
			1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
		};
		
		private final Bitmap cursor = new Bitmap(c, c, c, a, cW, cH);

		public boolean draw(Painter painter, boolean forced) {
			painter.drawBitmap(cursor, 0, 0);
			return true;
		}
	};
	
	private static Handler handler1 = new Handler() {
		
		private int ls, ts, rs, bs;
		private int clickedX, clickedY;
		
		private Group group = new Group(80, 60) {
			
			@Override
			public boolean draw(Painter p, boolean forced) {
				if (forced) {
					Bitmap bitmap = p.getBitmap();
					p.setForeground(null);
					p.setBackground(new Color(0.5f, 0.5f, 0.5f));
					p.drawRectangle(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
					
					p.setForeground(new Color(0.5f, 0f, 0f, 0.5f));
					p.drawLine(10, 10, 10, 100);
					p.drawLine(40, 100, 20, 10);
					p.drawLine(40, 10, 20, 100);
					p.drawLine(20, 120, 120, 140);
					p.drawLine(20, 140, 120, 120);
					p.drawLine(60, 10, 100, 50);
					
					return true;
				}
				else {
					return false;
				}
			}
		};
		
		
		public void sizeChanged() 
		{
			
			ls = getPointsWidthCount() / 2 - 30; rs = getPointsWidthCount() / 2 + 30;
			ts = getPointsHeightCount() / 2 - 25; bs = getPointsHeightCount() / 2 + 25; 
			
			noiseGroup.resize(rs - ls, bs - ts);
			group.resize(getPointsWidthCount(), getPointsHeightCount());
		}

		public void frame() {
			group.display(0, 0);
			noiseGroup.display(ls, ts);
			cursorGroup.display(mouseX, mouseY);
		}
		
		public void mouseMove(double xPts, double yPts) {
			mouseX = (int) Math.round(xPts);
			mouseY = (int) Math.round(yPts);
		}
		
		@Override
		public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
			if (state == MouseButtonState.PRESS) {
				clickedX = mouseX;
				clickedY = mouseY;
			}
			
			if (state == MouseButtonState.RELEASE && clickedX > ls && clickedX < rs && clickedY > ts && clickedY < bs) {
				Core.setHandler(handler2);
			}
		}
	};
	
	private static Handler handler2 = new Handler() {

		
		public void sizeChanged() 
		{
			noiseGroup.resize(getPointsWidthCount(), getPointsHeightCount());
		}
		
		public void frame() {
			noiseGroup.display(0, 0);
			cursorGroup.display(mouseX, mouseY);
		}
		
		public void mouseMove(double xPts, double yPts) {
			mouseX = (int) Math.round(xPts);
			mouseY = (int) Math.round(yPts);
		}
		
		@Override
		public void mouseButton(MouseButton button,	MouseButtonState state, Modifiers modifiers) {
			if (state == MouseButtonState.RELEASE) {
				Core.setHandler(handler1);
			}
		}
	};
	
	public static void main(String[] args) {
		
		if (Core.open("JNILayerTest MainWindow", 800, 600, 2)) {
			Core.setCursorVisibility(false);
			Core.setHandler(handler1);
			
			boolean res = Core.run();
			
			if (res) {
				System.out.println("mainLoop exited successfully");
			} else {
				System.out.println("mainLoop failed");
			}
		} else {
			System.out.println("createWindow failed");
		}
	}
}
