package com.github.aakumykov.storage_access_helper.modern

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.aakumykov.storage_access_helper.ManageAllFilesContract

class StorageAccessHelperModernFragment (private val fragment: Fragment)
    : StorageAccessHelperModernBasic()
{
    override fun getContext(): Context = fragment.requireContext()

    override fun packageName(): String = fragment.requireActivity().packageName


    override fun prepareForReadAccess() {
        prepareForFullAccess()
    }

    override fun prepareForWriteAccess() {
        prepareForFullAccess()
    }

    override fun prepareForFullAccess() {
        activityResultLauncher = fragment.registerForActivityResult(
            ManageAllFilesContract(packageName())) { isGranted ->
                invokeOnResult(isGranted)
            }
    }
}