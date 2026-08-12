# FastSIMD API Reference

## Class `fastsimd.SIMD`

### Operations
- `SIMD.findByte(Pointer ptr, long length, byte target)`: Scans memory 32 bytes per cycle. Returns 0-based offset or -1.
- `SIMD.copy(Pointer src, Pointer dst, long bytes)`: Performs 256-bit AVX2 bulk memory copy.
- `SIMD.isAVX2Supported()`: Returns `true` if AVX2 hardware acceleration is enabled.
