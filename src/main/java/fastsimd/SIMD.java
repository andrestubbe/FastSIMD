package fastsimd;

import fastpointer.Pointer;

/**
 * SIMD — High-Level Java Vector Acceleration Engine (AVX2, AVX-512, NEON).
 */
public final class SIMD {

    private SIMD() {}

    public static boolean isAVX2Supported() {
        if (FastSIMDNative.isNativeLoaded()) {
            return FastSIMDNative.isAVX2Supported();
        }
        return false;
    }

    /**
     * Scans memory 32 bytes per cycle for the target byte using AVX2 SIMD instructions.
     * Returns the 0-based offset where byte was found, or -1 if not found.
     */
    public static int findByte(Pointer ptr, long length, byte target) {
        if (ptr == null || ptr.isNull() || length <= 0) return -1;

        if (FastSIMDNative.isNativeLoaded()) {
            return FastSIMDNative.findByteSIMD(ptr.address(), length, target);
        }

        // Fallback scalar search
        for (int i = 0; i < length; i++) {
            if (ptr.getByte(i) == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Performs a 256-bit SIMD bulk memory copy.
     */
    public static void copy(Pointer src, Pointer dst, long bytes) {
        if (src == null || dst == null || src.isNull() || dst.isNull() || bytes <= 0) return;

        if (FastSIMDNative.isNativeLoaded()) {
            FastSIMDNative.copySIMD(src.address(), dst.address(), bytes);
        } else {
            for (long i = 0; i < bytes; i++) {
                dst.setByte(i, src.getByte(i));
            }
        }
    }
}
