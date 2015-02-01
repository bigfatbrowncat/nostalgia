package nostalgia;

public class JNILayerTest {
	public static void main(String[] args) {
		int res = JNILayer.mainLoop("JNILayerTest MainWindow", 400, 300, 8);
		System.out.println("mainLoop exited with code " + res);
	}
}
