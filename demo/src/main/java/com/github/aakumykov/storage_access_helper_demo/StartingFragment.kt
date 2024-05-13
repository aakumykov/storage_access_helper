package com.github.aakumykov.storage_access_helper_demo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.github.aakumykov.storage_access_helper_demo.databinding.FragmentStartBinding

class StartingFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var storageAccessHelper: StorageAccessHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageAccessHelper = StorageAccessHelper.create(this).also {
            it.prepareForReadAccess()
            it.prepareForWriteAccess()
            it.prepareForFullAccess()
        }

        binding.appPropertiesButton.setOnClickListener {
            StorageAccessHelper.openStorageAccessSettings(this)
        }

        binding.requestStorageReadAccessButton.setOnClickListener {
            storageAccessHelper.requestReadAccess { displayStorageAccessState() }
        }

        binding.requestStorageWriteAccessButton.setOnClickListener {
            storageAccessHelper.requestWriteAccess { displayStorageAccessState() }
        }

        binding.requestStorageFullAccessButton.setOnClickListener {
            storageAccessHelper.requestFullAccess { displayStorageAccessState() }
        }
    }


    override fun onResume() {
        super.onResume()
        displayStorageAccessState()
    }


    private fun displayStorageAccessState() {
        binding.textView.setText(when {
            storageAccessHelper.hasFullAccess() -> R.string.full_storage_access
            storageAccessHelper.hasReadAccess() -> R.string.reading_storage_access
            storageAccessHelper.hasWriteAccess() -> R.string.writing_storage_access
            else -> R.string.no_storage_access
        })
    }


    companion object {
        fun create(): StartingFragment = StartingFragment()
    }
}