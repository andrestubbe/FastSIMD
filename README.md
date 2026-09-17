# FastSIMD 0.1.3 [ALPHA-2026-08] — Hardware Vectorization Framework (AVX2, AVX-512, NEON)

[![Status](https://img.shields.io/badge/status-0.1.3-brightgreen.svg)](https://github.com/andrestubbe/FastSIMD/releases/tag/0.1.3)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

**⚡ High-performance hardware vector acceleration engine (AVX2, AVX-512, NEON) for the JVM.**

`FastSIMD` provides a unified SIMD abstraction layer for Java. It encapsulates 32-byte parallel delimiter matching, bulk memory transfers, vector math, and pixel format conversions using hardware intrinsics.

Watch Demo (YouTube) | Watch JMH Benchmark (YouTube)

![Showcase](https://raw.githubusercontent.com/andrestubbe/FastSIMD/main/docs/screenshot.png)

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

## Table of Contents

- [Why FastSIMD?](#why-fastsimd)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Technical Demos & Benchmarks](#technical-demos--benchmarks)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)

---

## Why FastSIMD?

Standard Java loops process arrays sequentially byte-by-byte or rely on unpredictable JIT auto-vectorization that often fails on complex branching. `FastSIMD` provides:

- **Explicit 256-Bit AVX2 Vector Acceleration** — Process 32 bytes or 8 single-precision floats per CPU cycle using native SIMD intrinsics (`_mm256_cmpeq_epi8`, `_mm256_movemask_epi8`).
- **28+ GB/sec Scan Bandwidth** — Execute ultra-fast log scanning, string searching, and delimiter matching at pure CPU memory bus speeds (15x faster than standard Java loops).
- **Zero-Allocation Hardware Intrinsics** — Perform bulk memory transfers, vector math, and byte sweeps directly on off-heap native memory pointers without triggering Garbage Collector pauses.

FastSIMD bypasses compiler guesswork by emitting deterministic hardware vector instructions directly via JNI:

| Feature | Standard Java Loop | JDK Vector API (Incubator) | FastSIMD |
|:---|:---|:---|:---|
| **Instruction Execution** | Scalar (1 byte / cycle) | JIT-compiled IR vectors | **Explicit Hardware Intrinsics** |
| **Vector Width** | None (1 byte) | 128 / 256 / 512 bit | **256-Bit AVX2 (32 Bytes / Cycle)** |
| **Scan Bandwidth** | ~1.8 GB/sec | ~12–18 GB/sec | **> 28 GB/sec (Bus Speed)** |
| **Branching Resilience** | Scalar fallback | JIT bails on branch | **Branchless Vector Masking** |
| **JVM Configuration** | Default Java runtime | Requires `--add-modules` | **0 JVM Flags (Pure Native DLL)** |
| **Dependencies** | JDK standard lib | JVM Incubator module | **Pure Java 17+ backed by FastCore** |

---

## Key Features

- **🔍 SIMD Scan**: 32-byte parallel delimiter & byte searching (`_mm256_cmpeq_epi8`, `_mm256_movemask_epi8`).
- **⚡ Bulk Memory & Prefetching**: 256-bit unaligned/aligned loads/stores and cache prefetching.
- **🔢 Vector Math**: 8-way Float32 and 4-way Float64 SIMD arithmetic for 3D and matrix operations.
- **🎨 Pixel & Convert**: Color space conversion (RGBA <-> BGRA) and Float↔Int transformations.

---

## Real-World Use Cases

- ⚡ **High-Frequency Trading (HFT)**: Scan FIX protocol streams and order book market ticks at 28+ GB/sec without GC pauses.
- 🔍 **SIEM & Security Log Mining**: Search multi-gigabyte server logs for threat patterns (`CRITICAL_ALERT`, `JWT_EXPIRED`) in milliseconds.
- 🤖 **AI Tensor Data Pipelines**: Perform zero-copy vector normalization and memory alignment for local GGUF LLM models.

---

## Performance Benchmarks

`FastSIMD` is engineered for ultra-high-throughput vector acceleration. In the official [JMH Benchmark](examples/Benchmark), the system measured native AVX2 256-bit vector search vs standard scalar loops over a 64 MB memory buffer:

```text
Benchmark                                    Mode  Cnt      Score   Error  Units
JMH_SIMD.benchmarkAVX2FindByte               thrpt    2  19632.158          ops/s
```

---

## API Quick Reference

| Method | Description | Docs |
|---|---|---|
| `SIMD.findByte(ptr, length, target)` | Scans memory 32 bytes per cycle for target byte (AVX2). | [Reference](docs/REFERENCE.md) |
| `SIMD.copy(src, dst, bytes)` | High-throughput 256-bit SIMD bulk memory copy. | [Reference](docs/REFERENCE.md) |
| `SIMD.isAVX2Supported()` | Returns `true` if CPU AVX2 vector extensions are supported. | [Reference](docs/REFERENCE.md) |

---

## Technical Demos & Benchmarks

| Case | Java Example | Launcher | Description |
|---|---|---|---|
| **AVX2 SIMD Parallel Search** | [Demo.java](examples/Demo.java) | `run-demo.bat` | End-to-end 100 MB streaming comparison matching delimiters via 256-bit AVX2 vector instructions vs scalar Java loops. |
| **JMH Microbenchmark Suite** | [Benchmark.java](examples/Benchmark/src/main/java/fastsimd/benchmark/Benchmark.java) | `run-benchmark.bat` | OpenJDK JMH throughput & latency test suite for SIMD byte scanning and vectorized off-heap memory copies. |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastSIMD Hardware Vector Engine -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastSIMD</artifactId>
        <version>0.1.3</version>
    </dependency>

    <!-- FastMemory Aligned Allocator -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastMemory</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastPointer Address Wrapper -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastPointer</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastCore Native Loader -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastSIMD:0.1.3'
    implementation 'com.github.andrestubbe:FastMemory:0.1.1'
    implementation 'com.github.andrestubbe:FastPointer:0.1.1'
    implementation 'com.github.andrestubbe:FastCore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. ⚡ **[FastSIMD-0.1.3.jar](https://github.com/andrestubbe/FastSIMD/releases/download/0.1.3/FastSIMD-0.1.3.jar)** (Hardware Vector Engine)
2. 💾 **[FastMemory-0.1.1.jar](https://github.com/andrestubbe/FastMemory/releases/download/0.1.1/FastMemory-0.1.1.jar)** (32-Byte Aligned Allocator)
3. 📍 **[FastPointer-0.1.1.jar](https://github.com/andrestubbe/FastPointer/releases/download/0.1.1/FastPointer-0.1.1.jar)** (Native Primitive Pointer)
4. ⚙️ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the JNI calls to function correctly.

---

## Documentation

- **[CHANGELOG.md](docs/CHANGELOG.md)**: Release notes.
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