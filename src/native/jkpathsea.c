#include <jni.h>
#include <kpathsea/kpathsea.h>
#include "jkpathsea.h"
#include <stdint.h>
#include <stdlib.h>

/*
 * Class:     com_xerdi_jkpathsea_KPathSea
 * Method:    init
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_xerdi_jkpathsea_KPathSea_init(JNIEnv *env, jobject obj) {
    kpathsea kpse = kpathsea_new();
    return (jlong) (intptr_t) kpse;
}

/*
 * Class:     com_xerdi_jkpathsea_KPathSea
 * Method:    set_program_name
 * Signature: (JLjava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_xerdi_jkpathsea_KPathSea_set_1program_1name(JNIEnv *env, jobject obj, jlong handle, jstring invocation_name, jstring program_name) {
    kpathsea kpse = (kpathsea) (intptr_t) handle;
    const char *invocation = NULL;
    const char *program = NULL;

    // Check if kpse pointer is valid
    if (!kpse) {
        fprintf(stderr, "Invalid kpathsea instance handle\n");
        return;
    }

    // Get UTF-8 strings from Java strings
    invocation = (*env)->GetStringUTFChars(env, invocation_name, NULL);
    program = (*env)->GetStringUTFChars(env, program_name, NULL);

    // Check if string retrieval was successful
    if (!invocation || !program) {
        fprintf(stderr, "Failed to retrieve invocation or program name\n");
        // Release obtained strings if partially retrieved
        if (invocation) (*env)->ReleaseStringUTFChars(env, invocation_name, invocation);
        if (program) (*env)->ReleaseStringUTFChars(env, program_name, program);
        return;
    }

    // Set program name using kpathsea function
    kpathsea_set_program_name(kpse, invocation, program);

    // Release UTF-8 strings
    (*env)->ReleaseStringUTFChars(env, invocation_name, invocation);
    (*env)->ReleaseStringUTFChars(env, program_name, program);
}

/*
 * Class:     com_xerdi_jkpathsea_KPathSea
 * Method:    find_file
 * Signature: (JLjava/lang/String;IZ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_xerdi_jkpathsea_KPathSea_find_1file(JNIEnv *env, jobject obj, jlong handle, jstring filename, jint format, jboolean must_exist) {
    kpathsea kpse = (kpathsea) (intptr_t) handle;
    const char *file_name = (*env)->GetStringUTFChars(env, filename, NULL);
    string result = kpathsea_find_file(kpse, file_name, (int) format, (int) must_exist);
    (*env)->ReleaseStringUTFChars(env, filename, file_name);
    if (result) {
        jstring jresult = (*env)->NewStringUTF(env, result);
        free(result); // Free the result as it was allocated by kpathsea_find_file
        return jresult;
    } else {
        return NULL;
    }
}

/*
 * Class:     com_xerdi_jkpathsea_KPathSea
 * Method:    version
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_xerdi_jkpathsea_KPathSea_version
  (JNIEnv *env, jobject obj) {
    const char *result = kpathsea_version_string;

    if (result == NULL) {
        return NULL;
    } else {
        return (*env)->NewStringUTF(env, result);
    }
}

/*
 * Class:     com_xerdi_jkpathsea_KPathSea
 * Method:    destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_xerdi_jkpathsea_KPathSea_destroy(JNIEnv *env, jobject obj, jlong handle) {
    kpathsea kpse = (kpathsea) (intptr_t) handle;
    kpathsea_finish(kpse);
}
