package fastsimd;

import fastcore.FastCore;

/**
 * FastSIMDNative — JNI Native Loader using FastCore.
 */
public final class FastSIMDNative {

    private static boolean loaded = false;

    static {
        try {
            FastCore.loadLibrary("FastSIMD", FastSIMDNative.class);
            loaded = true;
        } catch (Throwable t) {
            loaded = false;
        }
    }

    public static boolean isNativeLoaded() {
        return loaded;
    }

    public static native boolean isAVX2Supported();
    public static native int findByteSIMD(long address, long length, byte target);
    public static native void copySIMD(long srcAddress, long dstAddress, long bytes);
}
