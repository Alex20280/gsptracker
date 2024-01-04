package fragment

import android.Manifest
import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.example.gpstracker.MainActivity
import com.example.gpstracker.ui.track.TrackFragment
import com.example.gpstracker.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackFragmentTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    val someGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun testStartButtonClicked() {

       val validLogin = "aleksandrbasanets2012@gmail.com"
        val validPassword = "cdcdF*25TT"

        onView(withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(validLogin), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(validPassword), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.button)).perform(click())

        Thread.sleep(5000)

        val scenario = launchFragmentInContainer<TrackFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.startButton)).perform(click())

    }

}