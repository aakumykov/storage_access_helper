package com.github.aakumykov.storage_access_helper_demo

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.github.aakumykov.storage_access_helper_demo.databinding.FragmentStartBinding
import java.io.File

class StartingFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var storageAccessHelper: StorageAccessHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStartBinding.bind(view)

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

        binding.mkdirButton.setOnClickListener {
            storageAccessHelper.requestWriteAccess {
                if (dirName.isEmpty())
                    binding.dirName.error = getString(R.string.cannot_be_empty)
                else
                    createDir(dirName)
            }
        }
    }

    private fun createDir(dirName: String) {

        val dir = File(Environment.getExternalStorageDirectory(), dirName)
        val isExistsText = if (dir.exists()) getString(R.string.exists) else getString(R.string.not_exists)

        if (dir.mkdirs())
            showInfo(getString(R.string.dir_existance_and_was_created, dirName, isExistsText))
        else
            showInfo(getString(R.string.dir_existance_and_not_created, dirName, isExistsText))
    }

    private fun showInfo(msg: String) {
        binding.infoView.apply {
            text = msg
            visibility = View.VISIBLE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private val dirName: String get() = binding.dirName.text.toString()
    private val overwriteIfExists: Boolean get() = binding.overwriteCheckBox.isChecked

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