package com.github.aakumykov.storage_access_helper

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var storageAccessHelper: StorageAccessHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageAccessHelper = StorageAccessHelper.create(this)

        findViewById<ExtendedFloatingActionButton>(R.id.appPropertiesButton).setOnClickListener { storageAccessHelper.openStorageAccessSettings() }

        findViewById<Button>(R.id.requestStorageReadAccessButton).setOnClickListener {
            storageAccessHelper.requestReadAccess { findViewById<TextView>(R.id.textView).text = "Доступ на чтение получен" }
        }

        findViewById<Button>(R.id.requestStorageWriteAccessButton).setOnClickListener {
            storageAccessHelper.requestWriteAccess { findViewById<TextView>(R.id.textView).text = "Доступ на запись получен" }
        }

        findViewById<Button>(R.id.requestStorageFullAccessButton).setOnClickListener {
            storageAccessHelper.requestFullAccess { findViewById<TextView>(R.id.textView).text = "Полный доступ получен" }
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.textView).setText(when {
            storageAccessHelper.hasFullAccess() -> R.string.full_storage_access
            storageAccessHelper.hasReadAccess() -> R.string.reading_storage_access
            storageAccessHelper.hasWriteAccess() -> R.string.writing_storage_access
            else -> R.string.no_storage_access
        })
    }
}