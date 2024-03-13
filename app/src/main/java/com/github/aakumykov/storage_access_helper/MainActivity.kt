package com.github.aakumykov.storage_access_helper

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val storageAccessHelper by lazy { StorageAccessHelper.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.appPropertiesButton).setOnClickListener { openAppProperties() }

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
}