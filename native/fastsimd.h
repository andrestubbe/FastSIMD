#ifndef FASTSIMD_H
#define FASTSIMD_H

#include <immintrin.h>
#include <stdint.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

// FastSIMD Scan Engine: 32-Byte Parallel Matcher
inline intptr_t FastSIMD_FindByte(const uint8_t* data, size_t length, uint8_t target) {
    if (!data || length == 0) return -1;

    size_t i = 0;
    __m256i target_vec = _mm256_set1_epi8(static_cast<char>(target));

    for (; i + 32 <= length; i += 32) {
        __m256i chunk = _mm256_loadu_si256(reinterpret_cast<const __m256i*>(data + i));
        __m256i cmp = _mm256_cmpeq_epi8(chunk, target_vec);
        int mask = _mm256_movemask_epi8(cmp);

        if (mask != 0) {
            unsigned long bit_index;
            #ifdef _MSC_VER
            _BitScanForward(&bit_index, mask);
            #else
            bit_index = __builtin_ctz(mask);
            #endif
            return static_cast<intptr_t>(i + bit_index);
        }
    }

    // Process tail bytes
    for (; i < length; i++) {
        if (data[i] == target) return static_cast<intptr_t>(i);
    }

    return -1;
}

// FastSIMD Mem Engine: 256-Bit AVX2 Copy
inline void FastSIMD_Copy256(const uint8_t* src, uint8_t* dst, size_t bytes) {
    if (!src || !dst || bytes == 0) return;

    size_t i = 0;
    for (; i + 32 <= bytes; i += 32) {
        __m256i chunk = _mm256_loadu_si256(reinterpret_cast<const __m256i*>(src + i));
        _mm256_storeu_si256(reinterpret_cast<__m256i*>(dst + i), chunk);
    }

    for (; i < bytes; i++) {
        dst[i] = src[i];
    }
}

#ifdef __cplusplus
}
#endif

#endif // FASTSIMD_H
