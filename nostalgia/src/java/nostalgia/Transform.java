package nostalgia;

public class Transform {
	private static native long createNativeRotate(float angle, float vx, float vy, float vz);
	private static native long createNativeScale(float sx, float sy, float sz);
	private static native long createNativeTranslate(float dx, float dy, float dz);
	private static native long createNativeMultiply(long ptr1, long ptr2);
	private static native void destroyNative(long pointer);

	private final long nativePointer;

	protected Transform(long nativePointer) {
		this.nativePointer = nativePointer;
	}
	
	public static Transform rotate(float angle, float vx, float vy, float vz) {
		return new Transform(createNativeRotate(angle, vx, vy, vz));
	}
	public static Transform scale(float sx, float sy, float sz) {
		return new Transform(createNativeScale(sx, sy, sz));
	}
	public static Transform translate(float dx, float dy, float dz) {
		return new Transform(createNativeTranslate(dx, dy, dz));
	}
	public static Transform multiply(Transform t1, Transform t2) {
		return new Transform(createNativeMultiply(t1.nativePointer, t2.nativePointer));
	}
	
	@Override
	protected void finalize() throws Throwable {
		destroyNative(nativePointer);
		super.finalize();
	}
}
