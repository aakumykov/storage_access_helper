package com.github.aakumykov.storage_access_helper

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface StorageAccessHelper {

    fun prepareForReadAccess()
    fun prepareForWriteAccess()
    fun prepareForFullAccess()

    fun requestReadAccess(resultCallback: (isGranted: Boolean) -> Unit)
    fun requestWriteAccess(resultCallback: (isGranted: Boolean) -> Unit)
    fun requestFullAccess(resultCallback: (isGranted: Boolean) -> Unit)


    fun hasReadAccess(): Boolean
    fun hasWriteAccess(): Boolean
    fun hasFullAccess(): Boolean


    companion object {

        fun openStorageAccessSettings(activity: Activity) {
            activity.startActivity(
                if (isAndroidROrLater()) IntentHelper.manageAllFilesIntent(activity)
                else IntentHelper.appSettingsIntent(activity)
            )
        }

        fun openStorageAccessSettings(fragment: Fragment) {
            fragment.startActivity(
                if (isAndroidROrLater()) IntentHelper.manageAllFilesIntent(fragment.requireContext())
                else IntentHelper.appSettingsIntent(fragment.requireContext())
            )
        }


        fun create(fragmentActivity: FragmentActivity): StorageAccessHelper {
            throw RuntimeException()
            /*return when {
                isAndroidROrLater() -> StorageAccessHelperModern(fragmentActivity)
                else -> StorageAccessHelperLegacyFragment(fragmentActivity)
            }*/
        }

        fun create(fragment: Fragment): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModern(fragment)
                else -> StorageAccessHelperLegacyFragment(fragment)
            }
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        private fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

}