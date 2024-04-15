package com.github.aakumykov.storage_access_helper_demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.github.aakumykov.storage_access_helper.StorageAccessNeverAskAgainChecker
import com.github.aakumykov.storage_access_helper_demo.databinding.FragmentStartBinding

class StartingFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private lateinit var storageAccessHelper: StorageAccessHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStartBinding.bind(view)

        storageAccessHelper = StorageAccessHelper.create(this)

        binding.appPropertiesButton.setOnClickListener {
            StorageAccessHelper.openStorageAccessSettings(this)
        }

        binding.requestStorageReadAccessButton.setOnClickListener {
            storageAccessHelper.requestReadAccess { displayStorageAccessState() }
        }

        binding.shouldShowReadAccessRationale.setOnClickListener {
            showToast(
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) "Обоснование нужно"
                else "Обоснование не нужно"
            )
        }

        binding.requestStorageWriteAccessButton.setOnClickListener {
            storageAccessHelper.requestWriteAccess { displayStorageAccessState() }
        }

        binding.requestStorageFullAccessButton.setOnClickListener {
            storageAccessHelper.requestFullAccess { displayStorageAccessState() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        displayStorageAccessState()
    }


    private fun displayStorageAccessState() {
        val neverAskAgainChecker = StorageAccessNeverAskAgainChecker.create()
        binding.textView.text = getString(
            when {
                neverAskAgainChecker.isNeverAskAgainRead(requireActivity()) -> R.string.never_ask_again_read
                neverAskAgainChecker.isNeverAskAgainWrite(requireActivity()) -> R.string.never_ask_again_write
                neverAskAgainChecker.isNeverAskAgainFullAccess(requireActivity()) -> R.string.never_ask_again_full

                storageAccessHelper.hasFullAccess() -> R.string.full_storage_access
                storageAccessHelper.hasReadAccess() -> R.string.reading_storage_access
                storageAccessHelper.hasWriteAccess() -> R.string.writing_storage_access

                else -> R.string.no_storage_access
            }
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun create(): StartingFragment = StartingFragment()
    }
}