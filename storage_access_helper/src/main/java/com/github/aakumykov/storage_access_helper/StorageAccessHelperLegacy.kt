package com.github.aakumykov.storage_access_helper

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

// TODO: превратить поля activity и fragment в аргументы
class StorageAccessHelperLegacy private constructor(
    private val activity: FragmentActivity? = null,
    private val fragment: Fragment? = null
)
    : StorageAccessHelperBasic(activity, fragment)
{
    // TODO: лениво?
    private val readingStoragePermissionsRequester: PermissionsRequester
    private val writingStoragePermissionsRequester: PermissionsRequester
    private val fullStoragePermissionsRequester: PermissionsRequester


    init {
        if (null == activity && null == fragment)
            throw IllegalStateException("Both activity and fragment are null.")

        readingStoragePermissionsRequester = constructReadingPermissionRequest()
        writingStoragePermissionsRequester = constructWritingPermissionRequest()
        fullStoragePermissionsRequester = constructFullAccessPermissionRequest()
    }

    private fun constructFullAccessPermissionRequest(): PermissionsRequester {
        return activity?.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        ) ?:
        fragment?.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        )!!
    }

    private fun constructWritingPermissionRequest(): PermissionsRequester {
        return activity?.constructPermissionsRequest(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        ) ?:
        fragment?.constructPermissionsRequest(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        )!!
    }

    private fun constructReadingPermissionRequest(): PermissionsRequester {
        return activity?.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        ) ?:
        fragment?.constructPermissionsRequest(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            requiresPermission = { resultCallback?.invoke(true) },
            onPermissionDenied = { resultCallback?.invoke(false) },
            onNeverAskAgain = { resultCallback?.invoke(true) }
        )!!
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
        activity().also { fragmentActivity ->
            fragmentActivity.startActivity(IntentHelper.appSettingsIntent(fragmentActivity))
        }

/*        activity?.also {
            IntentHelper.appSettingsIntent(activity).also { intent ->
                activity.startActivity(intent)
            }
        } ?:
        fragment?.also {
            IntentHelper.appSettingsIntent(activity).also { intent ->
                activity.startActivity(intent)
            }
        }*/
    }


    private fun isAccessGranted(checkedPermission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity().checkCallingOrSelfPermission(checkedPermission)
    }
}