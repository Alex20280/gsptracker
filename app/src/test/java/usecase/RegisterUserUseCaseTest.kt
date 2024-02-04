package usecase

import com.example.gpstracker.network.ErrorResponse
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.usecase.RegisterUserUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.runBlocking
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import repository.FakeAuthenticationRepository

class RegisterUserUseCaseTest {

    private lateinit var mockRepo: FakeAuthenticationRepository

    @Before
    fun setUp(){
        mockRepo = mockk<FakeAuthenticationRepository>()
    }

    @Test
    fun `should return registration success result`() = runBlocking {

        // Stub coordinates
        val expectedAuthResult = mockk<Task<AuthResult>>() // Mocking an AuthResult for testing purposes
        val expectedLoginResult = RequestResult.Success(expectedAuthResult)

        val emailAddress = "test@gmail.com"
        val password = "sacscsdcds"

        coEvery { mockRepo.registerUser(emailAddress, password) } returns expectedLoginResult

        // Use case with mock repository
        val useCase = RegisterUserUseCase(mockRepo)

        // Execute
        val result = useCase.registerUser(emailAddress, password)

        // Verify and assert
        coVerify(exactly = 1) {
            mockRepo.registerUser(emailAddress, password)
        }
        assertEquals(expectedLoginResult, result)
    }

    @Test
    fun `should return registration fail result`() = runBlocking {

        // Stub coordinates
        val expectedLoginResult = RequestResult.Error(ErrorResponse.Default("Login problem"), 0)

        val emailAddress = "test@gmail.com"
        val password = "sacscsdcds"

        coEvery { mockRepo.registerUser(emailAddress, password) } returns expectedLoginResult

        // Use case with mock repository
        val useCase = RegisterUserUseCase(mockRepo)

        // Execute
        val result = useCase.registerUser(emailAddress, password)

        // Verify and assert
        coVerify(exactly = 1) {
            mockRepo.registerUser(emailAddress, password)
        }
        assertEquals(expectedLoginResult, result)
    }
}