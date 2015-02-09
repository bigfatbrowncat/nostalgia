package nostalgia.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

public class Font {
	private int width, height;
	private HashMap<Character, boolean[]> letters = new HashMap<Character, boolean[]>();
	
	public Character nextSymbol(char character) {
		int cur = -1;
		Character[] chars = letters.keySet().toArray(new Character[] { });
		Arrays.sort(chars);
		for (int i = 0; i < chars.length; i++) {
			if (chars[i].equals(character)) cur = i;
		}

		if (cur == -1) return null;
		if (cur < chars.length - 1) {
			return chars[cur + 1];
		} else {
			return null;
		}
	}

	public Character previousSymbol(char character) {
		int cur = -1;
		Character[] chars = letters.keySet().toArray(new Character[] { });
		Arrays.sort(chars);
		for (int i = 0; i < chars.length; i++) {
			if (chars[i].equals(character)) cur = i;
		}

		if (cur == -1) return null;
		if (cur > 0) {
			return chars[cur - 1];
		} else {
			return null;
		}
	}

	public Character getFirstSymbol() {
		Character[] chars = letters.keySet().toArray(new Character[] { });
		Arrays.sort(chars);
		if (chars.length > 0) {
			return chars[0];
		} else {
			return null;
		}
	}
	public Character getLastSymbol() {
		Character[] chars = letters.keySet().toArray(new Character[] { });
		Arrays.sort(chars);
		if (chars.length > 0) {
			return chars[chars.length - 1];
		} else {
			return null;
		}
	}
	
	public Font(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public boolean addSymbol(char c) {
		if (letters.get(c) == null) {
			letters.put(c, new boolean[width * height]);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean[] getSymbol(char c) {
		return letters.get(c);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private static void writeInt(OutputStream out, int val) throws IOException {
		int b1 = val & 0xff;
		int b2 = (val >> 8) & 0xff;
		int b3 = (val >> 16) & 0xff;
		int b4 = val >> 24;
		out.write(b1);
		out.write(b2);
		out.write(b3);
		out.write(b4);
	}
	
	private static int readInt(InputStream in) throws IOException {
		int b1 = in.read();
		int b2 = in.read();
		int b3 = in.read();
		int b4 = in.read();
		int res = b4;
		res <<= 8;
		res += b3;
		res <<= 8;
		res += b2;
		res <<= 8;
		res += b1;
		return res;
	}
	
	public void toStream(OutputStream out) throws IOException {
		writeInt(out, width); writeInt(out, height);
		//out.write(width); out.write(height);
		
		Character[] keys = letters.keySet().toArray(new Character[] {});
		writeInt(out, keys.length);
		//out.write(keys.length);
		for (int k = 0; k < keys.length; k++) {
			writeInt(out, (int)(keys[k]));
			//out.write((int)(keys[k]));
			
			BitSet bs = new BitSet(width * height);
			boolean[] letter = letters.get(keys[k]);
			for (int i = 0; i < letter.length; i++) {
				bs.set(i, letter[i]);
			}
			byte[] letterBytes = bs.toByteArray();
			writeInt(out, letterBytes.length);
			//out.write(letterBytes.length);
			out.write(letterBytes);
		}
	}
	
	public static Font fromStream(InputStream in) throws IOException {
		int width = readInt(in);
		int height = readInt(in);
		Font res = new Font(width, height);
		int charsCount = readInt(in);
		for (int k = 0; k < charsCount; k++) {
			char key = (char) readInt(in);
			int letterBytesLength = readInt(in);
			byte[] letterBytes = new byte[letterBytesLength];
			in.read(letterBytes);
			BitSet bs = BitSet.valueOf(letterBytes);
			boolean[] letter = new boolean[width * height];
			for (int i = 0; i < letter.length; i++) {
				letter[i] = bs.get(i);
			}
			res.letters.put(key, letter);
		}
		return res;
	}
}
