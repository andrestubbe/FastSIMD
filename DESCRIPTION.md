# FastSIMD — Hardware Vectorization Framework (AVX2, AVX-512, NEON)

> **Unified Hardware Vector Acceleration Engine for the FastJava Ecosystem.**

---

## 🎯 Zweck & Aufgabe

`FastSIMD` ist das zentrale Vektorierungs-Framework für das gesamte FastJava-Ökosystem. Es kapselt AVX2, AVX-512 und ARM NEON Befehlssätze in einer modularen C++ Header-Only Library mit JNI-Bindungen ab und eliminiert duplizierte SIMD-Intrinsics in allen Einzelmodulen.

---

## 📊 Codebase SIMD Audit (70 Intrinsics über 12 Repositories)

Basierend auf der vollständigen Analyse aller nativen C++ Quelldateien deckt `FastSIMD` genau 4 Kern-Module ab:

### 1. `FastSIMD::Scan` (Parallel Matcher & Delimiter Engine)
- **Extrahierte Intrinsics (7)**: `_mm256_cmpeq_epi8`, `_mm256_cmpgt_epi8`, `_mm256_movemask_epi8`, `_mm256_movemask_ps`, `_mm_cmpeq_epi8`, `_mm_cmpgt_epi8`, `_mm_movemask_epi8`
- **Zweck**: O(1) 32-Byte Parallel-Suche nach Trennzeichen (`"`, `:`, `\n`, `<`, `>`).
- **Verwendet in**: `FastBytes`, `FastContentChunk`, `FastJSON`, `FastScrape`, `FastSpider`, `FastString`, `FastSoftware3D`.

### 2. `FastSIMD::Mem` (Zero-Copy Bulk Load/Store & Prefetching)
- **Extrahierte Intrinsics (23)**: `_mm256_loadu_si256`, `_mm256_storeu_si256`, `_mm256_loadu_pd`, `_mm256_loadu_ps`, `_mm512_loadu_si512`, `_mm512_storeu_si512`, `_mm_prefetch`, `_mm256_set1_epi8`, `_mm256_set1_ps`, `_mm256_setzero_ps` u.a.
- **Zweck**: Fast Unaligned Loads/Stores, L1/L2 Cache-Prefetching und 512-bit AVX-512 Speicherströme.
- **Verwendet in**: `FastBytes`, `FastContentChunk`, `FastImage`, `FastJSON`, `FastMath`, `FastScrape`, `FastSoftware3D`, `FastSpider`, `FastString`.

### 3. `FastSIMD::Math` (Vector Arithmetik & Geometry Engine)
- **Extrahierte Intrinsics (24)**: `_mm256_add_ps`, `_mm256_sub_ps`, `_mm256_mul_ps`, `_mm256_div_ps`, `_mm256_mul_pd`, `_mm256_sqrt_pd`, `_mm256_min_ps`, `_mm256_max_ps`, `_mm256_mullo_epi32` u.a.
- **Zweck**: 8-way Float32 & 4-way Float64 Vektorrechnung für 3D-Rasterisierung, SSAA Downsampling & Matrix-Multiplikation.
- **Verwendet in**: `FastImage`, `FastMath`, `FastSoftware3D`, `FastString`.

### 4. `FastSIMD::Pixel` (Media Processing, Bitwise & Type Conversions)
- **Extrahierte Intrinsics (16)**: Bitwise (`_mm256_and_si256`, `_mm256_or_si256`, `_mm512_xor_si512`), Shifting (`_mm256_srli_epi32`, `_mm256_slli_epi32`), Conversions (`_mm256_cvtepi32_ps`, `_mm256_cvtps_epi32`)
- **Zweck**: RGBA <-> BGRA Farbraum-Konvertierung, SIMD Pixel Shifting, Bitwise Maskierung & Float<->Int Konvertierung.
- **Verwendet in**: `FastImage`, `FastCamera`, `FastSoftware3D`, `FastBytes`, `FastContentChunk`.

---

## 🔗 Wer bindet sich an `FastSIMD`?

Alle **12 Module mit direktem SIMD-Code** binden sich an `FastSIMD`:
- `FastBytes`
- `FastCamera`
- `FastCompress`
- `FastContentChunk`
- `FastFloat`
- `FastImage`
- `FastJSON`
- `FastMath`
- `FastScrape`
- `FastSoftware3D`
- `FastSpider`
- `FastString`

---

## 🔄 Die Zero-Copy Pipeline

```
FastSharedMemory (Teilt Framebuffer / Token-Stream prozessübergreifend)
  └── FastMemory (Hält & sichert 32-Byte aligned RAM ohne GC-Overhead)
        └── FastPointer (Zeigt direkt auf die RAM-Adresse)
              └── FastSIMD (Verarbeitet Bytes mit AVX2 / AVX-512 / NEON)
```
