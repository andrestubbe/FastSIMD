package examples;

import fastmemory.Memory;
import fastpointer.Pointer;
import fastsimd.SIMD;

public class Demo {
    public static void main(String[] args) {
        System.out.println("=== FastSIMD 0.1.0 Interactive Demo ===");

        // 1. Allocate 1024 bytes of 32-byte SIMD-aligned memory via FastMemory
        try (Memory memory = Memory.allocateAligned(1024, 32)) {
            Pointer ptr = memory.pointer();

            // Populate memory with test string data
            byte[] testData = "Hello World! FastSIMD AVX2 Hardware Vector Engine Active.".getBytes();
            for (int i = 0; i < testData.length; i++) {
                ptr.setByte(i, testData[i]);
            }

            System.out.printf("Memory allocated at: 0x%016X%n", ptr.address());

            // 2. Perform 32-byte SIMD parallel search for 'V'
            int index = SIMD.findByte(ptr, testData.length, (byte) 'V');
            System.out.println("SIMD parallel search found target 'V' at index: " + index);

            // 3. Perform 256-bit SIMD memory copy to second buffer
            try (Memory targetMemory = Memory.allocateAligned(1024, 32)) {
                Pointer targetPtr = targetMemory.pointer();

                SIMD.copy(ptr, targetPtr, testData.length);
                System.out.println("256-bit SIMD Memory Copy completed.");

                byte copiedByte = targetPtr.getByte(index);
                System.out.printf("Verified copied byte at index %d: '%c'%n", index, (char) copiedByte);
            }

            System.out.println("=== Demo finished successfully! ===");
        }
    }
}
