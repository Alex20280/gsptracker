package usecase

import com.example.gpstracker.usecase.GetUserUidUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import repository.FakeAuthenticationRepository

class GetUserUidUseCaseTest {

    private lateinit var mockRepo: FakeAuthenticationRepository

    @Before
    fun setUp(){
        mockRepo = mockk<FakeAuthenticationRepository>()
    }

    @Test
    fun `should receive client uid`() = runBlocking {

        // Stub coordinates
        val uid = "hcbdshjcvbsdlcjdnskjcld"

        coEvery { mockRepo.getUserUd() } returns uid

        // Use case with mock repository
        val useCase = GetUserUidUseCase(mockRepo)

        // Execute
        val result = useCase.getUserUid()

        // Verify and assert
        coVerify(exactly = 1) {
            mockRepo.getUserUd()
        }
        Assert.assertEquals(result,uid)
    }
}