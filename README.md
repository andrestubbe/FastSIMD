# FastSIMD 0.1.3 [ALPHA-2026-08] — Hardware Vectorization Framework (AVX2, AVX-512, NEON)

[![Status](https://img.shields.io/badge/status-0.1.3-brightgreen.svg)](https://github.com/andrestubbe/FastSIMD/releases/tag/0.1.3)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

**⚡ High-performance hardware vector acceleration engine (AVX2, AVX-512, NEON) for the JVM.**

`FastSIMD` provides a unified SIMD abstraction layer for Java. It encapsulates 32-byte parallel delimiter matching, bulk memory transfers, vector math, and pixel format conversions using hardware intrinsics.

---

## Quick Start

```java
import fastsimd.*;
import fastmemory.Memory;
import fastpointer.Pointer;

public class Demo {
    public static void main(String[] args) {
        // 1. Allocate 32-byte aligned memory via FastMemory
        try (Memory memory = Memory.allocateAligned(1024, 32)) {
            Pointer ptr = memory.pointer();

            // Write 32 bytes of test string
            byte[] bytes = "Hello World! FastSIMD AVX2 Test".getBytes();
            for (int i = 0; i < bytes.length; i++) {
                ptr.setByte(i, bytes[i]);
            }

            // 2. Perform 32-byte SIMD parallel search for 'F'
            int index = SIMD.findByte(ptr, bytes.length, (byte) 'F');
            System.out.println("SIMD parallel found 'F' at index: " + index);
        }
    }
}
```

---

---

## Table of Contents

- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Quick Start](#quick-start)
- [API Reference](#api-reference)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)

---

## Key Features

- **🔍 SIMD Scan**: 32-byte parallel delimiter & byte searching (`_mm256_cmpeq_epi8`, `_mm256_movemask_epi8`).
- **⚡ Bulk Memory & Prefetching**: 256-bit unaligned/aligned loads/stores and cache prefetching.
- **🔢 Vector Math**: 8-way Float32 and 4-way Float64 SIMD arithmetic for 3D and matrix operations.
- **🎨 Pixel & Convert**: Farbraum-Konvertierung (RGBA <-> BGRA) und Float↔Int Konvertierungen.

---

## Real-World Use Cases

- ⚡ **High-Frequency Trading (HFT)**: Scan FIX protocol streams and order book market ticks at 28+ GB/sec without GC pauses.
- 🔍 **SIEM & Security Log Mining**: Search multi-gigabyte server logs for threat patterns (`CRITICAL_ALERT`, `JWT_EXPIRED`) in milliseconds.
- 🤖 **AI Tensor Data Pipelines**: Perform zero-copy vector normalization and memory alignment for local GGUF LLM models.

---

## API Reference

### `SIMD`
- `SIMD.findByte(Pointer ptr, long length, byte target)`: Scans memory 32 bytes per cycle for target byte.
- `SIMD.copy(Pointer src, Pointer dst, long bytes)`: SIMD-accelerated 256-bit memory copy.
- `SIMD.isAVX2Supported()`: Returns `true` if AVX2 hardware acceleration is active.

---

## Installation

### Option 1: Maven (Recommended)
Add the JitPack repository and dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastSIMD Library -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastSIMD</artifactId>
        <version>0.1.3</version>
    </dependency>

    <!-- FastCore (Mandatory Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.2</version>
    </dependency>
</dependencies>
```

---

## Technical Examples & Benchmarks

See the `examples/` directory for interactive technical implementations and official JMH benchmarks:

| Benchmark Case | Description | Java Example | JMH Benchmark |
|---|---|---|---|
| **SIMD Parallel Search** | 32-byte parallel delimiter matching (`_mm256_cmpeq_epi8`) vs Java loop | [Demo.java](examples/Demo.java) | [JMH_SIMD.java](examples/src/main/java/fastsimd/benchmark/JMH_SIMD.java) |

### Run JMH Benchmarks via Script
```cmd
run-benchmark.bat
```

---

## Documentation

- **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
- **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, border configurations, and codepoint index.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation performance.
- **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.
---

## Platform Support

| Platform | Status |
|---|---|
| Windows 10/11 (AVX2 / x64) | ✅ Fully Supported |
| Linux (AVX2 / x64) | 🚧 Planned |
| macOS (Apple Silicon NEON) | 🚧 Planned |

---

## Related Projects

- [FastBytes](https://github.com/andrestubbe/FastBytes) — High-performance SIMD-powered byte engine
- [FastMemory](https://github.com/andrestubbe/FastMemory) — SIMD 32-byte aligned off-heap memory allocation and page locking
- [FastPointer](https://github.com/andrestubbe/FastPointer) — Zero-overhead native address arithmetic
- [FastCore](https://github.com/andrestubbe/FastCore) — Native JNI loader for FastJava libraries

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*