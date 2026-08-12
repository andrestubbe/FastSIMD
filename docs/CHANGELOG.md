# FastSIMD Changelog

## [0.1.0] - 2026-08-12
### Added
- Initial release of FastSIMD vector acceleration engine.
- 32-byte AVX2 parallel byte matcher (`_mm256_cmpeq_epi8`, `_mm256_movemask_epi8`).
- 256-bit AVX2 bulk memory copy (`_mm256_loadu_si256`, `_mm256_storeu_si256`).
