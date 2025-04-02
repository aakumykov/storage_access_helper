package com.github.aakumykov.storage_access_helper_demo

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.aakumykov.storage_access_helper.StorageAccessHelper
import com.github.aakumykov.storage_access_helper_demo.screens.MainScreen
import com.github.aakumykov.storage_access_helper_demo.screens.ManageAllFilesScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class ManageAllFilesTest : TestCase() {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @After
    fun delayAfterTest() =TimeUnit.SECONDS.sleep(2)

    @Test
    fun manageAllFilesOnOff() = run {
        step("Открыть экран управления всея файлами") {
            MainScreen {
                manageAllFilesButton {
                    isVisible()
                    isClickable()
                    click()
                }
            }

            ManageAllFilesScreen {
                /*switch {
                    isClickable()
                    click()
                }*/
                title {
                    isDisplayed()
                    click()
                }
            }
        }
    }
}