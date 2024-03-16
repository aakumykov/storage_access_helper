package com.github.aakumykov.storage_access_helper

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class StartingFragment : Fragment(R.layout.fragment_start) {

    private lateinit var storageAccessHelper: StorageAccessHelper
    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.rootView = view

        storageAccessHelper = StorageAccessHelper.create(this)

        view.findViewById<ExtendedFloatingActionButton>(R.id.appPropertiesButton).setOnClickListener {
            storageAccessHelper.openStorageAccessSettings()
        }

        view.findViewById<Button>(R.id.requestStorageReadAccessButton).setOnClickListener {
            storageAccessHelper.requestReadAccess { displayStorageAccessState() }
        }

        view.findViewById<Button>(R.id.requestStorageWriteAccessButton).setOnClickListener {
            storageAccessHelper.requestWriteAccess { displayStorageAccessState() }
        }

        view.findViewById<Button>(R.id.requestStorageFullAccessButton).setOnClickListener {
            storageAccessHelper.requestFullAccess { displayStorageAccessState() }
        }
    }


    override fun onResume() {
        super.onResume()
        displayStorageAccessState()
    }


    private fun displayStorageAccessState() {
        rootView.findViewById<TextView>(R.id.textView).setText(when {
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