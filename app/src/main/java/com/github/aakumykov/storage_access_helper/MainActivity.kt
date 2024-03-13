package com.github.aakumykov.storage_access_helper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val storageAccessHelper by lazy { StorageAccessHelper.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.appPropertiesButton).setOnClickListener { openAppProperties() }

        findViewById<Button>(R.id.requestStorageReadAccessButton).setOnClickListener {
            storageAccessHelper.requestStorageAccess {
                onReadAccessGranted()
            }
        }
    }

    private fun onReadAccessGranted() {
        findViewById<TextView>(R.id.textView).text = "Доступ на чтение получен"
    }
}