package com.github.aakumykov.storage_access_helper.modern

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import com.github.aakumykov.storage_access_helper.StorageAccessHelper

abstract class StorageAccessHelperModernBasic : StorageAccessHelper {

    protected var activityResultLauncher: ActivityResultLauncher<Unit>? = null
    protected var resultCallback: ((isGranted: Boolean) -> Unit)? = null


    abstract fun getContext(): Context
    abstract fun packageName(): String


    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit) {

        this.resultCallback = resultCallback

        if (hasFullAccess())
            invokeOnResult(true)
        else
            activityResultLauncher?.launch(Unit)
    }


    protected fun invokeOnResult(isGranted: Boolean) {
        resultCallback?.invoke(isGranted)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        requestFullAccess(resultCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        requestFullAccess(resultCallback)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasWriteAccess(): Boolean = hasFullAccess()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasReadAccess(): Boolean = hasFullAccess()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasFullAccess(): Boolean = Environment.isExternalStorageManager()

}
