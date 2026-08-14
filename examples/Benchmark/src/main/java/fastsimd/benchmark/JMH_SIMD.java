package fastsimd.benchmark;

import fastmemory.Memory;
import fastpointer.Pointer;
import fastsimd.SIMD;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class JMH_SIMD {

    private Memory srcMem;
    private Memory dstMem;
    private Pointer srcPtr;
    private Pointer dstPtr;

    @Setup
    public void setup() {
        srcMem = Memory.allocate(1024 * 1024);
        dstMem = Memory.allocate(1024 * 1024);
        srcPtr = Pointer.of(srcMem.address());
        dstPtr = Pointer.of(dstMem.address());
    }

    @TearDown
    public void tearDown() {
        srcMem.free();
        dstMem.free();
    }

    @Benchmark
    public int testSIMDFindByteScan() {
        return SIMD.findByte(srcPtr, 1024 * 1024, (byte) 0xFF);
    }

    @Benchmark
    public void testSIMDVectorCopy() {
        SIMD.copy(srcPtr, dstPtr, 1024 * 1024);
    }
}
