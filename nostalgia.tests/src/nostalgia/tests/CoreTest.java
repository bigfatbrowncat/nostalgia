package nostalgia.tests;

import java.util.Random;

import nostalgia.Core;
import nostalgia.Group;
import nostalgia.Handler;
import nostalgia.graphics.Bitmap;
import nostalgia.graphics.Color;
import nostalgia.graphics.Painter;

public class CoreTest {
	private static final int cW = 8, cH = 8;
	
	private static final float[] c = new float[] {
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
	};
	private static final float[] a = new float[] {
		1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
		1.0f, 0.8f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
	};
	
	private static final Bitmap cursor = new Bitmap(c, c, c, a, cW, cH);
	
	private static Random random = new Random();
	private static int mouseX = -10, mouseY = -10;

	private static Handler handler1 = new Handler() {
		
		private int ls, ts, rs, bs;
		private int clickedX, clickedY;
		
		private Group group = new Group(80, 60) {
			
			@Override
			public boolean draw(Painter p, boolean initial) {
				Bitmap bitmap = p.getBitmap();
				p.setForeground(null);
				p.setBackground(new Color(0.5f, 0.5f, 0.5f));
				p.drawRectangle(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
				
				ls = bitmap.getWidth() / 2 - 30; rs = bitmap.getWidth() / 2 + 30;
				ts = bitmap.getHeight() / 2 - 25; bs = bitmap.getHeight() / 2 + 25; 
				
				Bitmap randomPix = Bitmap.createWithoutAlpha(rs - ls, bs - ts); 
				float[] r = randomPix.getR(), g = randomPix.getG(), b = randomPix.getB();
				for (int i = 0; i < randomPix.getWidth(); i++) {
					for (int j = 0; j < randomPix.getHeight(); j++) {
						r[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
						g[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
						b[j * randomPix.getWidth() + i] = random.nextFloat() * 0.8f;
					}
				}
				p.drawBitmap(randomPix, ls, ts);
				p.drawBitmap(cursor, mouseX, mouseY);
				
				p.setForeground(new Color(0.5f, 0f, 0f, 0.5f));
				p.drawLine(10, 10, 10, 100);
				p.drawLine(40, 100, 20, 10);
				p.drawLine(40, 10, 20, 100);
				p.drawLine(20, 120, 120, 140);
				p.drawLine(20, 140, 120, 120);
				p.drawLine(60, 10, 100, 50);
				
				return true;
			}
		};
		
		public void sizeChanged() 
		{
			group.resize(getPointsWidthCount(), getPointsHeightCount());
		}

		public void frame() {
			group.display();
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
		private Group group = new Group(80, 60) {
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
				p.drawBitmap(cursor, mouseX, mouseY);
				return true;
			}
		};
		
		public void sizeChanged() 
		{
			group.resize(getPointsWidthCount(), getPointsHeightCount());
		}
		
		public void frame() {
			group.display();
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
