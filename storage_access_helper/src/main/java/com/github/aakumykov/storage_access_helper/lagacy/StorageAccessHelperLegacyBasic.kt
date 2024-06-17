package com.github.aakumykov.storage_access_helper.lagacy

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.PermissionChecker
import com.github.aakumykov.storage_access_helper.FULL_PERMISSION
import com.github.aakumykov.storage_access_helper.READING_PERMISSION
import com.github.aakumykov.storage_access_helper.StorageAccessCallback
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.github.aakumykov.storage_access_helper.WRITING_PERMISSION

abstract class StorageAccessHelperLegacyBasic : StorageAccessHelper {

    protected var readingStorageRequestLauncher: ActivityResultLauncher<String>? = null
    protected var writingStorageRequestLauncher: ActivityResultLauncher<String>? = null
    protected var fullStorageRequestLauncher: ActivityResultLauncher<Array<String>>? = null

    protected var readingResultCallback: StorageAccessCallback? = null
    protected var writingResultCallback: StorageAccessCallback? = null
    protected var fullAccessResultCallback: StorageAccessCallback? = null


    abstract fun getContext(): Context


    override fun requestReadAccess(resultCallback: StorageAccessCallback) {
        this.readingResultCallback = resultCallback
        readingStorageRequestLauncher?.launch(READING_PERMISSION)
            ?: throw IllegalStateException(workNotPreparedMessage("prepareForReadAccess"))
    }


    override fun requestWriteAccess(resultCallback: StorageAccessCallback) {
        this.writingResultCallback = resultCallback
        writingStorageRequestLauncher?.launch(WRITING_PERMISSION)
            ?: throw IllegalStateException(workNotPreparedMessage("prepareForWriteAccess"))
    }


    override fun requestFullAccess(resultCallback: StorageAccessCallback) {
        this.fullAccessResultCallback = resultCallback
        fullStorageRequestLauncher?.launch(FULL_PERMISSION)
            ?: throw IllegalStateException(workNotPreparedMessage("prepareForFullAccess"))
    }


    override fun hasReadAccess(): Boolean = isAccessGranted(READING_PERMISSION)
    override fun hasWriteAccess(): Boolean = isAccessGranted(WRITING_PERMISSION)
    override fun hasFullAccess(): Boolean = isAccessGranted(READING_PERMISSION) && isAccessGranted(
        WRITING_PERMISSION
    )


    private fun isAccessGranted(checkedPermission: String): Boolean {
        return PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkCallingOrSelfPermission(
            getContext(),
            checkedPermission
        )
    }


    private fun workNotPreparedMessage(methodName: String): String {
        return "You must call '${methodName}()' method before requesting storage access."
    }

    companion object {
        val TAG: String = StorageAccessHelperLegacyBasic::class.java.simpleName
    }
}
