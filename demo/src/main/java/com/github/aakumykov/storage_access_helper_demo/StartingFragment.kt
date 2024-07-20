package com.github.aakumykov.storage_access_helper_demo

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
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
            storageAccessHelper.requestReadAccess { displayStorageAccessState(R.string.read_access) }
        }

        binding.requestStorageWriteAccessButton.setOnClickListener {
            storageAccessHelper.requestWriteAccess { displayStorageAccessState(R.string.writing_access) }
        }

        binding.requestStorageFullAccessButton.setOnClickListener {
            storageAccessHelper.requestFullAccess { displayStorageAccessState(R.string.full_access) }
        }

        binding.mkdirButton.setOnClickListener {
            storageAccessHelper.requestWriteAccess {
                if (dirName.isEmpty())
                    binding.dirName.error = getString(R.string.cannot_be_empty)
                else
                    createDir(dirName)
            }
        }

        binding.readDirButton.setOnClickListener {
            readStorageDir()
        }
    }

    private fun readStorageDir() {

        "/storage".also { readedPath ->

            binding.readDirButton.text = readedPath

            storageAccessHelper.requestFullAccess {
                File(readedPath).list()
                    ?.sortedBy { it }
                    ?.also { list ->
                    list.joinToString("\n") { it }.also { joinedList ->
                        Log.d(TAG, joinedList)
                        showInfo("${Build.MANUFACTURER}, ${Build.MODEL}\n\n${joinedList}")
                    }
                }?.forEach { pathInStorage ->
                    Log.d(TAG, pathInStorage)
                }
                    ?: showInfo(getString(R.string.listring_of_dir_returns_empty_result, "/storage"))
            }
        }
    }

    private fun createDir(dirName: String) {

        val dir = File(Environment.getExternalStorageDirectory(), dirName)
        val dirPath = dir.absolutePath

        if (dir.exists()) {
            showInfo(getString(R.string.dir_already_exists, dirPath))
            return
        }

        if (dir.mkdirs())
            showInfo(getString(R.string.dir_was_created, dirPath))
        else
            showInfo(getString(R.string.dir_was_not_created, dirPath))
    }

    private fun showInfo(msg: String) {
        binding.infoView.apply {
            text = msg
            visibility = View.VISIBLE
        }
    }

    private fun showInfo(@StringRes msgRes: Int) {
        getString(msgRes).also {
            showInfo(it)
            showToast(it)
        }
    }

    private fun showToast(@StringRes stringRes: Int) {
        showToast(getString(stringRes))
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private val dirName: String get() = binding.dirName.text.toString()
    private val overwriteIfExists: Boolean get() = binding.overwriteCheckBox.isChecked

    private fun displayStorageAccessState(@StringRes msgRes: Int) {
        when {
            storageAccessHelper.hasFullAccess() -> R.string.full_storage_access
            storageAccessHelper.hasReadAccess() -> R.string.reading_storage_access
            storageAccessHelper.hasWriteAccess() -> R.string.writing_storage_access
            else -> R.string.no_storage_access
        }.also { textRes ->
            val prefix = getString(msgRes)
            val result = getString(textRes)
            val msg = "$prefix: $result"
            showInfo(msg)
            showToast(msg)
        }
    }


    companion object {
        val TAG: String = StartingFragment::class.java.simpleName
        fun create(): StartingFragment = StartingFragment()
    }
}