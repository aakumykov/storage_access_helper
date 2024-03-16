package com.github.aakumykov.storage_access_helper

import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class StorageAccessHelperModern private constructor(
    activity: FragmentActivity? = null,
    fragment: Fragment? = null
): StorageAccessHelper {

    constructor(fragmentActivity: FragmentActivity): this(activity = fragmentActivity, fragment = null)
    constructor(fragment: Fragment): this(activity = null, fragment = fragment)

    private var activity: FragmentActivity? = null
    private var fragment: Fragment? = null

    // TODO: лениво
    private var activityResultLauncher: ActivityResultLauncher<Unit>? = null
    private var onResult: ((isGranted: Boolean) -> Unit)? = null


    init {
        if (null != activity) {
            activityResultLauncher =
                activity.registerForActivityResult(ManageAllFilesContract(activity.packageName)) { isGranted ->
                    invokeOnResult(isGranted)
                }
        }
        else if (null != fragment) {
            fragment.registerForActivityResult(ManageAllFilesContract(fragment.requireActivity().packageName)) { isGranted ->
                invokeOnResult(isGranted)
            }
        }
        else {
            throw IllegalStateException("There is no both activity and fragment: you must pass one of its to constructor.")
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


    private fun activity(): FragmentActivity {
        return activity ?: fragment?.requireActivity()!!
    }

    private fun invokeOnResult(isGranted: Boolean) = onResult?.invoke(isGranted)
}