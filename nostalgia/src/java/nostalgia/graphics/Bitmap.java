package nostalgia.graphics;

public class Bitmap {
	private final float[] r, g, b, a;
	private final int width, height, offset, stride;
	public static Bitmap createWithAlpha(int width, int height) {
		return new Bitmap(
				new float[width * height], 
				new float[width * height], 
				new float[width * height], 
				new float[width * height], width, height);
	}
	public static Bitmap createWithoutAlpha(int width, int height) {
		return new Bitmap(
				new float[width * height], 
				new float[width * height], 
				new float[width * height], 
				null, width, height);
	}
	
	public Bitmap(float[] r, float[] g, float[] b, float[] a, int width, int height) {
		this(r, g, b, a, width, height, 0, width);
	}
	public Bitmap(float[] r, float[] g, float[] b, float[] a, int width, int height, int offset, int stride) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.width = width;
		this.height = height;
		this.offset = offset;
		this.stride = stride;
		
		// Checking integrity
		if (r == null) throw new IllegalArgumentException("r shouldn't be null");
		if (g == null) throw new IllegalArgumentException("g shouldn't be null");
		if (b == null) throw new IllegalArgumentException("b shouldn't be null");
		
		if (r.length != g.length || r.length != b.length) new IllegalArgumentException("r, g and b lengths should be equal");
		if (a != null && a.length != r.length) new IllegalArgumentException("length of a should be equal to r, g and b lengths");
		
		if (width <= 0) throw new IllegalArgumentException("width should be positive");
		if (height <= 0) throw new IllegalArgumentException("height should be positive");
		if (offset < 0) throw new IllegalArgumentException("offset shouldn't be negative");
		if (stride < width) throw new IllegalArgumentException("stride shouldn't be smaller than width");
		
		int back = offset + stride * height;
		if (r.length < back) throw new IllegalArgumentException("length of color arrays should be greater than offset + stride * height");
	}
	
	/**
	 * This method paints this bitmap inside another one. 
	 * It checks any problems with edges (for example if you try
	 * to paint your image outside the other's boundaries, no
	 * out-of-range errors will occur. If alpha channel is presented
	 * 
	 * @param other 
	 * @param x0
	 * @param y0
	 */
	public void blit(Bitmap other, int x0, int y0) {
		// Cutting edges
		int newOffset = offset, newWidth = width, newHeight = height;
		
		if (x0 < 0) {
			newOffset -= x0;
			newWidth += x0;
			x0 = 0;
		}
		
		if (x0 + width > other.width) {
			newWidth -= x0 + width - other.width;
		}
		
		if (y0 < 0) {
			newOffset -= y0 * stride;
			newHeight += y0;
			y0 = 0;
		}
		
		if (y0 + height > other.height) {
			newHeight -= y0 + height - other.height;
		}
		
		// Checking if we cut the whole image away
		if (newWidth <= 0 || newHeight <= 0 || newOffset + stride * newHeight > r.length) return;
		
		Bitmap tmpBmp;
		if (newOffset != offset || newWidth != width || newHeight != height) {
			tmpBmp = new Bitmap(r, g, b, a, newWidth, newHeight, newOffset, stride);
		} else {
			tmpBmp = this;
		}
		
		int index = tmpBmp.offset;
		for (int j = 0; j < tmpBmp.height; j++) { 
			for (int i = 0; i < tmpBmp.width; i++) {
				if (tmpBmp.a != null) {
					other.setPixel(x0 + i, y0 + j, tmpBmp.r[index + i], tmpBmp.g[index + i], tmpBmp.b[index + i], tmpBmp.a[index + i]);
				} else {
					other.setPixel(x0 + i, y0 + j, tmpBmp.r[index + i], tmpBmp.g[index + i], tmpBmp.b[index + i]);
				}
			}
			index += tmpBmp.stride;
		}
	}
	public void setPixel(int i, int j, float r, float g, float b) {
		if (i >= 0 && j >= 0 && i < width && j < height) {
			int index = offset + j * stride + i;
			if (this.a == null) {
				this.r[index] = r;
				this.g[index] = g;
				this.b[index] = b;
			} else if (this.a != null) {
				this.r[index] = r;
				this.g[index] = g;
				this.b[index] = b;
				this.a[index] = 1.0f;
			}
		}
	}

	public void setPixel(int i, int j, float r, float g, float b, float a) {
		if (i >= 0 && j >= 0 && i < width && j < height) {
			int index = offset + j * stride + i;
			if (this.a == null) {
				this.r[index] = r * a + this.r[index] * (1.0f - a);
				this.g[index] = g * a + this.g[index] * (1.0f - a);
				this.b[index] = b * a + this.b[index] * (1.0f - a);
			} else if (this.a != null) {
				// This sophisticated formula could be obtained if
				// you write blitting of 2 images to a background 
				// consequentially and merge the two blitting procedures to one
				this.a[index] = this.a[index] + a - this.a[index] * a;
				this.r[index] = this.r[index] - a * (this.r[index] - r) / this.a[index];
				this.g[index] = this.g[index] - a * (this.g[index] - g) / this.a[index];
				this.b[index] = this.b[index] - a * (this.b[index] - b) / this.a[index];
			} else {
				throw new RuntimeException("Strange case");
			}
		}
	}
	
	public void setPixel(int i, int j, Color c) {
		if (c.getAlpha() == null) {
			setPixel(i, j, c.getRed(), c.getGreen(), c.getBlue());
		} else {
			setPixel(i, j, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		}
	}
	
	/** 
	 * @return the image width
	 */
	public int getWidth() {
		return width;
	}

	/** 
	 * @return the image height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * @return the raw red channel of the image. Use it carefully.
	 */
	public float[] getR() {
		return r;
	}
	/**
	 * @return the raw green channel of the image. Use it carefully.
	 */
	public float[] getG() {
		return g;
	}
	/**
	 * @return the raw blue channel of the image. Use it carefully.
	 */
	public float[] getB() {
		return b;
	}
	/**
	 * @return the raw alpha channel of the image.
	 * If the image doesn't contain alpha channel, 
	 * it will be <code>null</code>.
	 */
	public float[] getA() {
		return a;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getStride() {
		return stride;
	}
}
