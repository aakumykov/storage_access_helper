package com.github.aakumykov.storage_access_helper

import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class StorageAccessHelperBasic protected constructor(
    activity: FragmentActivity? = null,
    fragment: Fragment? = null
): StorageAccessHelper {


    constructor(fragmentActivity: FragmentActivity): this(activity = fragmentActivity, fragment = null)
    constructor(fragment: Fragment): this(activity = null, fragment = fragment)


    private var activity: FragmentActivity? = null
    private var fragment: Fragment? = null


    protected var activityResultLauncher: ActivityResultLauncher<Unit>? = null
    protected var resultCallback: ((isGranted: Boolean) -> Unit)? = null


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

    override fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit) {
        this.resultCallback = resultCallback
    }

    protected fun invokeOnResult(isGranted: Boolean) = resultCallback?.invoke(isGranted)

    protected fun activity(): FragmentActivity {
        return activity ?: fragment?.requireActivity()!!
    }
}