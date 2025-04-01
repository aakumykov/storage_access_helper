package com.github.aakumykov.storage_access_helper_demo.screens

import com.kaspersky.components.kautomator.component.switch.UiSwitch
import com.kaspersky.components.kautomator.screen.UiScreen

object ManageAllFilesScreen : UiScreen<ManageAllFilesScreen>() {

    const val PACKAGE_NAME = "com.android.settings"

    override val packageName: String
        get() = PACKAGE_NAME

    val switch = UiSwitch {
        withId(this@ManageAllFilesScreen.PACKAGE_NAME,"switch_widget")
    }
}