package com.ikhoka.ui


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ikhoka.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Test
    fun whenScreenIsLaunched_LoginComponentIsShown() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.login_component))
            .check(matches(isDisplayed()))
        activityScenario.close()
    }

    @Test
    fun whenScreenIsLaunched_LoadingComponentIsNotShown() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.loading_component))
            .check(matches(not(isDisplayed())))
        activityScenario.close()
    }


    fun whenScreenIsLaunched_ErrorComponentIsNotShown() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.error_component))
            .check(matches(not(isDisplayed())))
        activityScenario.close()
    }


    @Test
    fun whenLoginButtonClicked_LoadingComponentIsShown() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.loading_component))
            .check(matches(isDisplayed()))
        activityScenario.close()
    }
}
