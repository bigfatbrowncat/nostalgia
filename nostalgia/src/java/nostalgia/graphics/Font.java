package nostalgia.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.LinkedHashMap;

public class Font {
	private int width, height;
	private LinkedHashMap<Character, boolean[]> letters = new LinkedHashMap<Character, boolean[]>();
	
	public Character nextSymbol(char character) {
		int cur = -1;
		Character[] chars = letters.keySet().toArray(new Character[] { });
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
		return (Character) letters.keySet().toArray()[0];
	}
	public Character getLastSymbol() {
		return (Character) letters.keySet().toArray()[letters.keySet().size() - 1];
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
	
	public void toStream(OutputStream out) throws IOException {
		out.write(width); out.write(height);
		
		Character[] keys = letters.keySet().toArray(new Character[] {});
		out.write(keys.length);
		for (int k = 0; k < keys.length; k++) {
			out.write(keys[k]);
			
			BitSet bs = new BitSet(width * height);
			boolean[] letter = letters.get(keys[k]);
			for (int i = 0; i < letter.length; i++) {
				bs.set(i, letter[i]);
			}
			byte[] letterBytes = bs.toByteArray();
			out.write(letterBytes.length);
			out.write(letterBytes);
		}
	}
	
	public static Font fromStream(InputStream in) throws IOException {
		int width = in.read();
		int height = in.read();
		Font res = new Font(width, height);
		int charsCount = in.read();
		for (int k = 0; k < charsCount; k++) {
			char key = (char) in.read();
			int letterBytesLength = in.read();
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
