package com.github.aakumykov.storage_access_helper.lagacy

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.github.aakumykov.storage_access_helper.READING_PERMISSION
import com.github.aakumykov.storage_access_helper.WRITING_PERMISSION

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