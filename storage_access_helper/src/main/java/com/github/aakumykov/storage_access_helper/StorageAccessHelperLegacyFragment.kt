package com.github.aakumykov.storage_access_helper

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.fragment.app.Fragment


class StorageAccessHelperLegacyFragment(
    private val fragment: Fragment
) : StorageAccessHelper
{
    private var readingStorageRequestLauncher: ActivityResultLauncher<String>? = null
    private var writingStorageRequestLauncher: ActivityResultLauncher<String>? = null
    private var fullStorageRequestLauncher: ActivityResultLauncher<Array<String>>? = null

    private var readingResultCallback: StorageAccessCallback? = null
    private var writingResultCallback: StorageAccessCallback? = null
    private var fullAccessResultCallback: StorageAccessCallback? = null


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


    override fun requestReadAccess(resultCallback: StorageAccessCallback) {
        this.readingResultCallback = resultCallback
        readingStorageRequestLauncher?.launch(READING_PERMISSION)
    }


    override fun requestWriteAccess(resultCallback: StorageAccessCallback) {
        this.writingResultCallback = resultCallback
        writingStorageRequestLauncher?.launch(WRITING_PERMISSION)
    }


    override fun requestFullAccess(resultCallback: StorageAccessCallback) {
        this.fullAccessResultCallback = resultCallback
        fullStorageRequestLauncher?.launch(FULL_PERMISSION)
    }


    override fun hasReadAccess(): Boolean = isAccessGranted(READING_PERMISSION)
    override fun hasWriteAccess(): Boolean = isAccessGranted(WRITING_PERMISSION)
    override fun hasFullAccess(): Boolean = isAccessGranted(READING_PERMISSION) && isAccessGranted(WRITING_PERMISSION)


    private fun isAccessGranted(checkedPermission: String): Boolean {
        return PermissionChecker.PERMISSION_GRANTED == checkCallingOrSelfPermission(fragment.requireContext(), checkedPermission)
    }
}