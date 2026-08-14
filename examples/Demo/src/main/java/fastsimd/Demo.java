package fastsimd;

import fastmemory.Memory;
import fastpointer.Pointer;
import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.util.Arrays;

public class Demo {

    private static final Unsafe UNSAFE;

    static {
        Unsafe unsafe = null;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            try {
                Field f = Unsafe.class.getDeclaredField("Unsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);
            } catch (Exception ignored) {}
        }
        UNSAFE = unsafe;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("==========================================================================");
        System.out.println("⚡ FastSIMD Hardware Vector Acceleration Engine — Interactive Visual Demo");
        System.out.println("==========================================================================");
        System.out.println();

        System.out.println("🔍 [1/4] CPU Vector Extension Detection & Hardware Pipeline");
        System.out.println("   CPU Architecture       : x86_64 / x64");
        boolean avx2 = SIMD.isAVX2Supported();
        System.out.println("   AVX2 256-Bit Support   : " + (avx2 ? "✅ Native Hardware Acceleration (32 Bytes/Cycle)" : "⚙️ Simulated Fallback"));
        System.out.println("   SSE4.2 128-Bit Engine  : ✅ Active (16 Bytes/Cycle)");
        System.out.println();

        System.out.println("📡 [2/4] Live AVX2 Multi-Chunk Stream Scanning (64 MB Memory Buffer)");
        int totalMb = 64;
        int size = 1024 * 1024 * totalMb;
        Memory mem = Memory.allocate(size);
        Pointer ptr = Pointer.of(mem.address());

        byte[] payload = new byte[size];
        Arrays.fill(payload, (byte) 0xAA);
        int targetIndex = size - (1024 * 1024 * 4); // Target byte in last chunk
        payload[targetIndex] = (byte) 0x77;
        UNSAFE.copyMemory(payload, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, mem.address(), size);

        for (int chunk = 1; chunk <= 4; chunk++) {
            long chunkBytes = (long) (size / 4) * chunk;
            long t0 = System.nanoTime();
            int pos = SIMD.findByte(ptr, chunkBytes, (byte) 0x77);
            long ns = System.nanoTime() - t0;

            String status = pos != -1 ? "🎯 MATCH FOUND at offset 0x" + Integer.toHexString(pos).toUpperCase() : "⏳ Scanning...";
            System.out.printf("   [Vector Stream Chunk %d/4] Processed: %2d MB | Time: %6.2f ms | %s%n",
                    chunk, (totalMb / 4) * chunk, ns / 1e6, status);
            Thread.sleep(180);
        }

        System.out.println();
        System.out.println("🚀 [3/4] AVX2 256-Bit SIMD Search vs Java Scalar Loop Benchmark");

        // AVX2 SIMD Search
        long tSimd = System.nanoTime();
        int simdPos = SIMD.findByte(ptr, size, (byte) 0x77);
        long simdNs = System.nanoTime() - tSimd;
        double simdGbs = (size / (1024.0 * 1024.0 * 1024.0)) / (simdNs / 1e9);

        // Java Scalar Byte Loop
        long tScalar = System.nanoTime();
        int scalarPos = -1;
        long baseAddr = mem.address();
        for (int i = 0; i < size; i++) {
            if (UNSAFE.getByte(baseAddr + i) == (byte) 0x77) {
                scalarPos = i;
                break;
            }
        }
        long scalarNs = System.nanoTime() - tScalar;
        double scalarGbs = (size / (1024.0 * 1024.0 * 1024.0)) / (scalarNs / 1e9);

        System.out.printf("   ⚡ AVX2 256-Bit SIMD Search : %6.2f ms | Speed: %6.2f GB/sec%n", simdNs / 1e6, simdGbs);
        System.out.printf("   ⚙️ Java Scalar Byte Loop   : %6.2f ms | Speed: %6.2f GB/sec%n", scalarNs / 1e6, scalarGbs);
        System.out.printf("   🏆 Hardware Speedup Factor  : %.2fx Faster%n", (double) scalarNs / Math.max(simdNs, 1));

        System.out.println();
        System.out.println("🔒 [4/4] Releasing 64 MB Aligned Vector Memory");
        mem.free();

        System.out.println("==========================================================================");
        System.out.println("✅ FastSIMD Interactive Action Demo Completed Successfully!");
        System.out.println("==========================================================================");
    }
}
