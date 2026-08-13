package fastsimd.benchmark;

import fastmemory.Memory;
import fastpointer.Pointer;
import fastsimd.SIMD;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class JMH_SIMD {

    @Param({"10485760"}) // 10 MB
    private int size;

    private Memory memory;
    private Pointer pointer;
    private byte target = (byte) 'V';

    @Setup
    public void setup() {
        memory = Memory.allocateAligned(size, 32);
        pointer = memory.pointer();
        byte[] testData = "Hello World! FastSIMD AVX2 Hardware Vector Engine Active.".getBytes();
        for (int i = 0; i < size; i++) {
            pointer.setByte(i, testData[i % testData.length]);
        }
        pointer.setByte(size - 1, target);
    }

    @TearDown
    public void tearDown() {
        if (memory != null) {
            memory.free();
        }
    }

    @Benchmark
    public int testJavaSearch() {
        for (int i = 0; i < size; i++) {
            if (pointer.getByte(i) == target) return i;
        }
        return -1;
    }

    @Benchmark
    public int testFastSIMDParallelSearch() {
        return SIMD.findByte(pointer, size, target);
    }
}
