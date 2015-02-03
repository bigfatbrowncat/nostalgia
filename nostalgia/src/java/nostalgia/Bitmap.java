package nostalgia;

public class Bitmap {
	private final float[] r, g, b, a;
	private final int width, height, offset, stride;
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
		int otherIndex = other.offset + y0 * other.stride + x0;
		for (int j = 0; j < tmpBmp.height; j++) { 
			for (int i = 0; i < tmpBmp.width; i++) {
				if (other.a == null && tmpBmp.a == null) {
					other.r[otherIndex + i] = tmpBmp.r[index + i];
					other.g[otherIndex + i] = tmpBmp.g[index + i];
					other.b[otherIndex + i] = tmpBmp.b[index + i];
				} else if (other.a == null && tmpBmp.a != null) {
					float ai = tmpBmp.a[index + i];
					other.r[otherIndex + i] = tmpBmp.r[index + i] * ai + other.r[otherIndex + i] * (1.0f - ai);
					other.g[otherIndex + i] = tmpBmp.g[index + i] * ai + other.g[otherIndex + i] * (1.0f - ai);
					other.b[otherIndex + i] = tmpBmp.b[index + i] * ai + other.b[otherIndex + i] * (1.0f - ai);
				} else if (other.a != null && tmpBmp.a == null) {
					other.r[otherIndex + i] = tmpBmp.r[index + i];
					other.g[otherIndex + i] = tmpBmp.g[index + i];
					other.b[otherIndex + i] = tmpBmp.b[index + i];
					other.a[otherIndex + i] = 1.0f;
				} else {
					throw new RuntimeException("Blitting an image with alphs channel to another image with alpha channel isn't supported yet");
				}
			}
			index += tmpBmp.stride;
			otherIndex += other.stride;
		}
	}
}
