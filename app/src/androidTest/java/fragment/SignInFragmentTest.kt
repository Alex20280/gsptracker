package com.example.gpstracker.fragment

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import com.example.gpstracker.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.gpstracker.MainActivity
import org.hamcrest.CoreMatchers.not

@RunWith(AndroidJUnit4::class)
class SignInFragmentTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLoginButtonDisabledWithOnlyLoginInput() { // Fail
        val validLogin = "user@example.com"

        // Input valid login
        onView(ViewMatchers.withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(validLogin), ViewActions.closeSoftKeyboard())


        // Check if the login button is disabled
        onView(ViewMatchers.withId(R.id.button))
            .check(ViewAssertions.matches(not(ViewMatchers.isEnabled())))
    }

    @Test
    fun testLoginButtonDisabledWithWrongLoginInput() { //Fail
        val validLogin = "user"
        val validPassword = "dscdscd25fvd265"

        // Input valid login
        onView(ViewMatchers.withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(validLogin), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(validPassword), ViewActions.closeSoftKeyboard())

        // Check if the login button is disabled
        onView(ViewMatchers.withId(R.id.button))
            .check(ViewAssertions.matches(not(ViewMatchers.isEnabled())))
    }

    @Test
    fun testLoginButtonDisabledWithOnlyPasswordInput() { //Fail
        val validPassword = "dscdscd25fvd265"

        // Input valid login
        onView(ViewMatchers.withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(validPassword), ViewActions.closeSoftKeyboard())

        // Check if the login button is disabled
        onView(ViewMatchers.withId(R.id.button))
            .check(ViewAssertions.matches(not(ViewMatchers.isEnabled())))
    }

    @Test
    fun testLoginButtonEnabledWithCorrectLoginInput() {
        val validLogin = "user@example.com"
        val validPassword = "dscdscd25fvd265"

        // Input valid login
        onView(ViewMatchers.withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(validLogin), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(validPassword), ViewActions.closeSoftKeyboard())

        // Check if the login button is disabled
        onView(ViewMatchers.withId(R.id.button))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }
}