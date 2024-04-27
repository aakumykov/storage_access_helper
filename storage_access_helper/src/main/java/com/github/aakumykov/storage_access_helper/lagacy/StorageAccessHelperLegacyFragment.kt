package com.github.aakumykov.storage_access_helper.lagacy

import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.github.aakumykov.storage_access_helper.READING_PERMISSION
import com.github.aakumykov.storage_access_helper.WRITING_PERMISSION


class StorageAccessHelperLegacyFragment(
    private val fragment: Fragment
) : StorageAccessHelperLagacyBasic()
{
    override fun getContext(): Context = fragment.requireContext()


    override fun prepareForReadAccess() {
        readingStorageRequestLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted -> readingResultCallback?.invoke(isGranted) }
    }


    override fun prepareForWriteAccess() {
        writingStorageRequestLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted -> writingResultCallback?.invoke(isGranted) }
    }


    override fun prepareForFullAccess() {
        fullStorageRequestLauncher = fragment.registerForActivityResult(
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