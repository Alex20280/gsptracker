package usecase

import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import repository.FakeLocationSavingRepository

class FirebaseDatabaseUseCaseTest {

    private lateinit var fakeRepo: FakeLocationSavingRepository

    @Before
    fun setUp() {
        fakeRepo = FakeLocationSavingRepository()
    }

    @Test
    fun `should check if location is saved`() = runBlocking {

        // Create a test instance of FakeLocationSavingRepository
        val fakeRepo = FakeLocationSavingRepository()

        // Test data
        val testLat = 1.0
        val testLong = 2.0
        val testTime = 12345L

        // Use case
        val useCase = FirebaseDatabaseUseCase(fakeRepo)

        // Execute
        useCase.isLocationSavedFromRoomDatabase(testLat, testLong, testTime)

        // Assert that the location was added to locationsSaved
        assertTrue(fakeRepo.locationsSaved.isNotEmpty()) // Ensure a location was added
        assertEquals(testLat, fakeRepo.locationsSaved.first().latitude) // Validate latitude
        assertEquals(testLong, fakeRepo.locationsSaved.first().longitude)
    }
}