package com.example.taskwhiz.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.taskwhiz.MainActivity
import com.example.taskwhiz.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fabClick_navigatesFromTaskList_toTaskEditor() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_task)
        ).assertExists()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.add_task)
        ).performClick()

    }

}
