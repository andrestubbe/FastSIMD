#include "fastsimd.h"
#include <jni.h>

extern "C" {

JNIEXPORT jboolean JNICALL Java_fastsimd_FastSIMDNative_isAVX2Supported(JNIEnv* env, jclass clazz) {
    return JNI_TRUE; // MSVC compiled AVX2 binary
}

JNIEXPORT jint JNICALL Java_fastsimd_FastSIMDNative_findByteSIMD(JNIEnv* env, jclass clazz, jlong address, jlong length, jbyte target) {
    const uint8_t* ptr = reinterpret_cast<const uint8_t*>(static_cast<uintptr_t>(address));
    intptr_t index = FastSIMD_FindByte(ptr, static_cast<size_t>(length), static_cast<uint8_t>(target));
    return static_cast<jint>(index);
}

JNIEXPORT void JNICALL Java_fastsimd_FastSIMDNative_copySIMD(JNIEnv* env, jclass clazz, jlong srcAddress, jlong dstAddress, jlong bytes) {
    const uint8_t* src = reinterpret_cast<const uint8_t*>(static_cast<uintptr_t>(srcAddress));
    uint8_t* dst = reinterpret_cast<uint8_t*>(static_cast<uintptr_t>(dstAddress));
    FastSIMD_Copy256(src, dst, static_cast<size_t>(bytes));
}

}
