package com.github.aakumykov.storage_access_helper

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface StorageAccessHelper {

    fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit)
    fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit)
    fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit)


    fun hasReadAccess(): Boolean
    fun hasWriteAccess(): Boolean
    fun hasFullAccess(): Boolean

    fun openStorageAccessSettings()


    companion object {

        fun create(fragmentActivity: FragmentActivity): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(fragmentActivity)
                else -> StorageAccessHelperLegacy(fragmentActivity)
            }
        }

        fun create(fragment: Fragment): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(fragment)
                else -> StorageAccessHelperLegacy(fragment)
            }
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        private fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

}