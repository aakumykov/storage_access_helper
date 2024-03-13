package com.github.aakumykov.storage_access_helper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Activity.openAppProperties() {
    val uri = Uri.parse("package:$packageName")
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
    if (intent.resolveActivity(packageManager) != null) { startActivity(intent) }
}