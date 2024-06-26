package com.github.aakumykov.storage_access_helper

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.fragment.app.Fragment
import com.github.aakumykov.storage_access_helper.lagacy.StorageAccessHelperLegacyActivity
import com.github.aakumykov.storage_access_helper.lagacy.StorageAccessHelperLegacyFragment
import com.github.aakumykov.storage_access_helper.modern.StorageAccessHelperModernActivity
import com.github.aakumykov.storage_access_helper.modern.StorageAccessHelperModernFragment

const val READING_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITING_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
val FULL_PERMISSION = arrayOf(
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
)


typealias StorageAccessCallback = (isGranted: Boolean) -> Unit


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


        fun create(activity: ComponentActivity): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModernActivity(activity)
                else -> StorageAccessHelperLegacyActivity(activity)
            }
        }

        fun create(fragment: Fragment): StorageAccessHelper {
            return when {
                isAndroidROrLater() -> StorageAccessHelperModernFragment(fragment)
                else -> StorageAccessHelperLegacyFragment(fragment)
            }
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        private fun isAndroidROrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }


    fun interface ReadingAccessRequestCallback {
        fun onReadingAccessResult(readingAccessIsGranted: Boolean)
    }

    fun interface WritingAccessRequestCallback {
        fun onWritingAccessResult(writingAccessIsGranted: Boolean)
    }

    fun interface FullAccessRequestCallback {
        fun onFullAccessResult(fullAccessIsGranted: Boolean)
    }
}