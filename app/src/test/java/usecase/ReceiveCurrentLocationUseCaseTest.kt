package usecase

import com.example.gpstracker.ui.track.LocationData
import com.example.gpstracker.usecase.ReceiveCurrentLocationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import repository.FakeLocationTrackingRepository

class ReceiveCurrentLocationUseCaseTest {

    private lateinit var mockRepo: FakeLocationTrackingRepository

    @Before
    fun setUp(){
        mockRepo = mockk<FakeLocationTrackingRepository>()
    }

    @Test
    fun `should receive current location`() = runBlocking {

        // test coordinates
        val testLat = 1.0
        val testLong = 1.0

        // mock repository
        val mockRepo = mockk<FakeLocationTrackingRepository>()
        coEvery { mockRepo.getCurrentLocation(any()) } answers {
            firstArg<((LocationData?) -> Unit)>()(
                LocationData(testLat, testLong)
            )
        }

        // use case
        val useCase = ReceiveCurrentLocationUseCase(mockRepo)

        // execute
        var result: LocationData? = null
        useCase.getCurrentLocation { location ->
            result = location
        }

        // verify
        coVerify(exactly = 1) {
            mockRepo.getCurrentLocation(any())
        }

        assertEquals(testLat, result?.latitude)
        assertEquals(testLong, result?.longitude)
    }


}