package com.github.aakumykov.storage_access_helper

import android.app.Activity
import androidx.core.app.ActivityCompat

abstract class StorageAccessNeverAskAgainChecker {

    abstract fun isNeverAskAgainRead(activity: Activity): Boolean
    abstract fun isNeverAskAgainWrite(activity: Activity): Boolean
    abstract fun isNeverAskAgainFullAccess(activity: Activity): Boolean

    companion object {
        fun create(): StorageAccessNeverAskAgainChecker {
            return if (isAndroidROrLater()) StorageAccessNeverAskAgainCheckerModern()
            else StorageAccessNeverAskAgainCheckerLegacy()
        }
    }
}


class StorageAccessNeverAskAgainCheckerLegacy : StorageAccessNeverAskAgainChecker() {

    override fun isNeverAskAgainRead(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun isNeverAskAgainWrite(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun isNeverAskAgainFullAccess(activity: Activity): Boolean {
        return isNeverAskAgainRead(activity) && isNeverAskAgainWrite(activity)
    }
}


class StorageAccessNeverAskAgainCheckerModern : StorageAccessNeverAskAgainChecker() {

    override fun isNeverAskAgainRead(activity: Activity): Boolean {
        return false
    }

    override fun isNeverAskAgainWrite(activity: Activity): Boolean {
        return false
    }

    override fun isNeverAskAgainFullAccess(activity: Activity): Boolean {
        return false
    }
}