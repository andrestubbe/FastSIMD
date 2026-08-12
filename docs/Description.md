# FastSIMD Architecture & Description

`FastSIMD` is the hardware vector acceleration engine for the FastJava ecosystem.

## Core Capabilities
- **`FastSIMD::Scan`**: 32-byte parallel delimiter & string matching (`_mm256_cmpeq_epi8`, `_mm256_movemask_epi8`).
- **`FastSIMD::Mem`**: 256-bit AVX2 bulk memory transfers & L1/L2 prefetching (`_mm_prefetch`).
- **`FastSIMD::Math`**: 8-way Float32 and 4-way Float64 vector math arithmetic.
- **`FastSIMD::Pixel`**: Farbraum-Konvertierung (RGBA <-> BGRA), SIMD Shifting & Float↔Int conversion.
