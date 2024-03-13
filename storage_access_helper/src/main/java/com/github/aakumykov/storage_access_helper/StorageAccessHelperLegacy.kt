package com.github.aakumykov.storage_access_helper

import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class StorageAccessHelperLegacy(private val activity: FragmentActivity): StorageAccessHelper {

    // TODO: лениво
    private val readingStoragePermissionsRequester: PermissionsRequester
    private val writingStoragePermissionsRequester: PermissionsRequester
    private val fullStoragePermissionsRequester: PermissionsRequester

    private var resultCallback: ((isGranted: Boolean) -> Unit)? = null // TODO: в интерфейс...


    init {
        readingStoragePermissionsRequester = activity.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        )

        writingStoragePermissionsRequester = activity.constructPermissionsRequest(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        )

        fullStoragePermissionsRequester = activity.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        )
    }


    override fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.resultCallback = resultCallback
        readingStoragePermissionsRequester.launch()
    }

    override fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.resultCallback = resultCallback
        writingStoragePermissionsRequester.launch()
    }

    override fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.resultCallback = resultCallback
        fullStoragePermissionsRequester.launch()
    }


    override fun hasReadAccess(): Boolean = isAccessGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun hasWriteAccess(): Boolean = isAccessGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun hasFullAccess(): Boolean {
        return isAccessGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                && isAccessGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun openStorageAccessSettings() {
        IntentHelper.appSettingsIntent(activity)
    }


    private fun isAccessGranted(checkedPermission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity.checkCallingOrSelfPermission(checkedPermission)
    }
}