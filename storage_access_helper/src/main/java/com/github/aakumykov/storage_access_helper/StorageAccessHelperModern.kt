package com.github.aakumykov.storage_access_helper

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class StorageAccessHelperModern(
    activity: FragmentActivity? = null,
    fragment: Fragment? = null
)
    : StorageAccessHelperBasic(activity, fragment)
{
    // TODO: разобраться с этими конструкторами
    constructor(fragmentActivity: FragmentActivity): this(activity = fragmentActivity, fragment = null)
    constructor(fragment: Fragment): this(activity = null, fragment = fragment)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        requestFullAccess(resultCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.resultCallback = resultCallback
        requestFullAccess(resultCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.resultCallback = resultCallback

        if (hasFullAccess())
            invokeOnResult(true)
        else
            activityResultLauncher?.launch(Unit)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasWriteAccess(): Boolean = hasFullAccess()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasReadAccess(): Boolean = hasFullAccess()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun hasFullAccess(): Boolean = Environment.isExternalStorageManager()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun openStorageAccessSettings() {
        activity().also {
            it.startActivity(IntentHelper.manageAllFilesIntent(it))
        }
    }
}