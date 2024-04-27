package com.github.aakumykov.storage_access_helper

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class StorageAccessHelperLegacyActivity(private val activity: ComponentActivity) : StorageAccessHelperBasic2() {

    override fun getContext(): Context = activity


    override fun prepareForReadAccess() {
        readingStorageRequestLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted -> readingResultCallback?.invoke(isGranted) }
    }


    override fun prepareForWriteAccess() {
        writingStorageRequestLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted -> writingResultCallback?.invoke(isGranted) }
    }


    override fun prepareForFullAccess() {
        fullStorageRequestLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results: Map<String,Boolean> ->
            results[READING_PERMISSION]?.let { isReadingAllowed ->
                results[WRITING_PERMISSION]?.let { isWritingAllowed ->
                    fullAccessResultCallback?.invoke(isReadingAllowed && isWritingAllowed)
                }
            }
        }
    }
}