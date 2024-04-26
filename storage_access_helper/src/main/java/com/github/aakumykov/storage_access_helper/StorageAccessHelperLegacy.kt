package com.github.aakumykov.storage_access_helper

import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.aakumykov.easypermissions.EasyPermissions
import com.github.aakumykov.easypermissions.models.PermissionRequest
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

private const val READ_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
private const val READ_PERMISSION_CODE = 10

class StorageAccessHelperLegacy private constructor(
    private val activity: FragmentActivity? = null,
    private val fragment: Fragment? = null
)
    : StorageAccessHelperBasic(activity, fragment)
{
    // TODO: разобраться с этими конструкторами
    constructor(fragmentActivity: FragmentActivity): this(activity = fragmentActivity, fragment = null)
    constructor(fragment: Fragment): this(activity = null, fragment = fragment)


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
        super.requestReadAccess(resultCallback)

        val permissionRequest = PermissionRequest.Builder(context())
            .code(READ_PERMISSION_CODE)
            .perms(arrayOf(READ_PERMISSION))
            .build()

        if (null != fragment)
            EasyPermissions.requestPermissions(fragment, permissionRequest)
        else if (null != activity)
            EasyPermissions.requestPermissions(activity, permissionRequest)
    }

    private fun context(): Context {
        return when {
            null != activity -> activity
            null != fragment -> fragment.requireContext()
            else -> throw IllegalStateException("Both activity and fragment properties are null.")
        }
    }

    override fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        super.requestWriteAccess(resultCallback)
        writingStoragePermissionsRequester.launch()
    }

    override fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        super.requestFullAccess(resultCallback)
        fullStoragePermissionsRequester.launch()
    }


    override fun hasReadAccess(): Boolean = isAccessGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun hasWriteAccess(): Boolean = isAccessGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun hasFullAccess(): Boolean {
        return isAccessGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                && isAccessGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    private fun isAccessGranted(checkedPermission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity().checkCallingOrSelfPermission(checkedPermission)
    }
}