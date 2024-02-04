package viemodel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import android.os.Looper
import androidx.work.WorkManager
import com.example.gpstracker.ui.track.TrackerState
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class TrackViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockContext: Context


    private lateinit var trackViewModel: TrackViewModel
    private val mockApplication = Application()

    @MockK
    private val mockWorkManager: WorkManager = mockk()

    @Before
    fun setUp() {

        mockkStatic(Looper::class)

        val mockMainLooper = mockk<Looper>(relaxed = true)

        every { Looper.getMainLooper() } returns mockMainLooper
        every { mockMainLooper.getThread() } returns Thread.currentThread()


        trackViewModel = TrackViewModel(mockApplication, mockWorkManager)
    }

    @Test
    fun `state is initialized to OFF`() {
        val state = trackViewModel.getStateLiveData().value
        assertEquals(TrackerState.OFF, state)
    }


/*    @Test
    fun `starting tracking service updates state`() {
        trackViewModel.startTrackingService()
        val state = trackViewModel.getStateLiveData().value
        assertEquals(TrackerState.ON, state)
    }*/
}

/*
@RunWith(AndroidJUnit4::class)
class TrackViewModelTest {
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

    private val mockApplication = Application()
    private val mockWorkManager: WorkManager = mockk()
    private val trackViewModel = TrackViewModel(mockApplication, mockWorkManager)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        every {
            mockWorkManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>())
        } returns mockk()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun startTrackingServiceTest() {

        UiThreadStatement.runOnUiThread {
            trackViewModel.startTrackingService()
            dispatcher.scheduler.advanceUntilIdle()
            //assert(trackViewModel.getStateLiveData().value == TrackerState.ON)
            trackViewModel.getStateLiveData().observeForever { state ->
                assert(state == TrackerState.ON)
            }

        }
    }
}*/
