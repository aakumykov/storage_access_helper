package com.github.aakumykov.storage_access_helper_demo.screens

import com.github.aakumykov.storage_access_helper_demo.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KButton

object MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val manageAllFilesButton = KButton { withId(R.id.requestStorageFullAccessButton) }
}