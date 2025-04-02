package com.github.aakumykov.storage_access_helper_demo.screens

import com.kaspersky.components.kautomator.component.switch.UiSwitch
import com.kaspersky.components.kautomator.component.text.UiTextView
import com.kaspersky.components.kautomator.screen.UiScreen

object ManageAllFilesScreen : UiScreen<ManageAllFilesScreen>() {

    const val PACKAGE_NAME = "com.android.settings"

    override val packageName: String
        get() = PACKAGE_NAME

    val title = UiTextView {
//        withId(this@ManageAllFilesScreen.PACKAGE_NAME, "title")
        withText("Allow access to manage all files")
    }

    val switch = UiSwitch {
        withId(this@ManageAllFilesScreen.PACKAGE_NAME,"widget_frame")
    }
}