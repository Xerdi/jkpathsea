#include <jni.h>
#include "jkpathsea.h"
#include <kpathsea/kpathsea.h>
#include <stdlib.h>

// Structure to hold the native KPathSea instance
typedef struct {
    kpathsea kpse;
} NativeKPathSea;

JNIEXPORT void JNICALL Java_com_xerdi_jkpathsea_KPathSea_nativeInit(JNIEnv *env, jobject obj) {
    // Allocate memory for the native instance
    NativeKPathSea* nativeInstance = (NativeKPathSea*) malloc(sizeof(NativeKPathSea));
    if (nativeInstance == NULL) {
        // Handle memory allocation failure
        jclass exceptionClass = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
        (*env)->ThrowNew(env, exceptionClass, "Failed to allocate native memory");
        return;
    }

    // Initialize the native instance
    nativeInstance->kpse = kpathsea_new();

    // Store the native instance in the Java object
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "nativeHandle", "J");
    (*env)->SetLongField(env, obj, fid, (jlong) nativeInstance);
}

JNIEXPORT void JNICALL Java_com_xerdi_jkpathsea_KPathSea_setProgramName(JNIEnv *env, jobject obj, jstring invocationName, jstring programName) {
    // Retrieve the native instance from the Java object
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "nativeHandle", "J");
    NativeKPathSea* nativeInstance = (NativeKPathSea*) (*env)->GetLongField(env, obj, fid);

    if (nativeInstance == NULL) {
        // Handle null native instance
        jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalStateException");
        (*env)->ThrowNew(env, exceptionClass, "Native KPathSea instance is not initialized");
        return;
    }

    const char *nativeInvocationName = (*env)->GetStringUTFChars(env, invocationName, 0);
    const char *nativeProgramName = (*env)->GetStringUTFChars(env, programName, 0);

    kpathsea_set_program_name(nativeInstance->kpse, nativeInvocationName, nativeProgramName);

    (*env)->ReleaseStringUTFChars(env, invocationName, nativeInvocationName);
    (*env)->ReleaseStringUTFChars(env, programName, nativeProgramName);
}

JNIEXPORT jstring JNICALL Java_com_xerdi_jkpathsea_KPathSea_findFile(JNIEnv *env, jobject obj, jstring filename) {
    // Retrieve the native instance from the Java object
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "nativeHandle", "J");
    NativeKPathSea* nativeInstance = (NativeKPathSea*) (*env)->GetLongField(env, obj, fid);

    if (nativeInstance == NULL) {
        // Handle null native instance
        jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalStateException");
        (*env)->ThrowNew(env, exceptionClass, "Native KPathSea instance is not initialized");
        return NULL;
    }

    const char *nativeFilename = (*env)->GetStringUTFChars(env, filename, 0);

    // Use a valid path. For example, search for TeX files:
    const char *result = kpathsea_find_file(nativeInstance->kpse, nativeFilename, kpse_tex_format, true);
    (*env)->ReleaseStringUTFChars(env, filename, nativeFilename);

    if (result == NULL) {
        return NULL; // File not found
    } else {
        return (*env)->NewStringUTF(env, result);
    }
}

JNIEXPORT void JNICALL Java_com_xerdi_jkpathsea_KPathSea_nativeCleanup(JNIEnv *env, jobject obj) {
    // Retrieve the native instance from the Java object
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "nativeHandle", "J");
    NativeKPathSea* nativeInstance = (NativeKPathSea*) (*env)->GetLongField(env, obj, fid);

    if (nativeInstance != NULL) {
        // Clean up the native instance
        kpathsea_finish(nativeInstance->kpse);
        free(nativeInstance);

        // Clear the native handle in the Java object
        (*env)->SetLongField(env, obj, fid, (jlong) 0);
    }
}