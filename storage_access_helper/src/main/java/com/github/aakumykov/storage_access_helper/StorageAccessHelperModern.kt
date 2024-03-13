package com.github.aakumykov.storage_access_helper

import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

class StorageAccessHelperModern(private val activity: FragmentActivity): StorageAccessHelper {

    // TODO: лениво
    private val activityResultLauncher: ActivityResultLauncher<Unit>
    private var onResult: ((isGranted: Boolean) -> Unit)? = null


    init {
        activityResultLauncher = activity.registerForActivityResult(ManageAllFilesContract(activity.packageName)) { isGranted ->
            invokeOnResult(isGranted)
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.onResult = resultCallback
        requestFullAccess(resultCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.onResult = resultCallback
        requestFullAccess(resultCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.onResult = resultCallback

        if (hasFullAccess())
            invokeOnResult(true)
        else
            activityResultLauncher.launch(Unit)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasWriteAccess(): Boolean = hasFullAccess()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasReadAccess(): Boolean = hasFullAccess()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasFullAccess(): Boolean = Environment.isExternalStorageManager()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun openStorageAccessSettings() {
        activity.startActivity(IntentHelper.manageAllFilesIntent(activity))
    }


    private fun invokeOnResult(isGranted: Boolean) = onResult?.invoke(isGranted)
}