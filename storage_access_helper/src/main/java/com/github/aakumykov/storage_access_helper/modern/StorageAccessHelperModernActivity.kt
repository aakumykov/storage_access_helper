package com.github.aakumykov.storage_access_helper.modern

import android.content.Context
import androidx.activity.ComponentActivity
import com.github.aakumykov.storage_access_helper.ManageAllFilesContract

class StorageAccessHelperModernActivity(private val activity: ComponentActivity)
    : StorageAccessHelperModernBasic()
{
    override fun getContext(): Context = activity

    override fun packageName(): String = activity.packageName


    override fun prepareForReadAccess() {
        prepareForFullAccess()
    }

    override fun prepareForWriteAccess() {
        prepareForFullAccess()
    }

    override fun prepareForFullAccess() {
        activityResultLauncher = activity.registerForActivityResult(
            ManageAllFilesContract(packageName())
        ) { isGranted ->
            invokeOnResult(isGranted)
        }
    }
}