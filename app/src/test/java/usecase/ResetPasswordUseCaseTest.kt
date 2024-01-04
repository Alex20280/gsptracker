package usecase

import com.example.gpstracker.network.ErrorResponse
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.usecase.ResetPasswordUseCase
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import repository.FakeAuthenticationRepository

class ResetPasswordUseCaseTest {

    private lateinit var mockRepo: FakeAuthenticationRepository

    @Before
    fun setUp(){
        mockRepo = mockk<FakeAuthenticationRepository>()
    }

    @Test
    fun `should return reset password success result`() = runBlocking {

        // Stub coordinates
        val expectedResetResult = RequestResult.Success(Unit)
        val emailAddress = "test@gmail.com"

        val fakeResetResultTask: Task<RequestResult<Unit>> = Tasks.forResult(expectedResetResult)
        coEvery { mockRepo.resetPassword(emailAddress) } returns fakeResetResultTask

        // Use case with mock repository
        val useCase = ResetPasswordUseCase(mockRepo)

        // Execute
        val result = useCase.resetPassword(emailAddress)

        // Verify and assert
        coVerify(exactly = 1) {
            mockRepo.resetPassword(emailAddress)
        }
        assertEquals(result.await(), expectedResetResult)
    }

    @Test
    fun `should return reset password fail result`() = runBlocking {

        val expectedResetResult = RequestResult.Error(ErrorResponse.Default("Reset problem"), 0)
        val emailAddress = "test@gmail.com"

        val fakeResetResultTask: Task<RequestResult<Unit>> = Tasks.forResult(expectedResetResult)
        coEvery { mockRepo.resetPassword(emailAddress) } returns fakeResetResultTask

        // Use case with mock repository
        val useCase = ResetPasswordUseCase(mockRepo)

        // Execute
        val result = useCase.resetPassword(emailAddress)

        // Verify and assert
        coVerify(exactly = 1) {
            mockRepo.resetPassword(emailAddress)
        }
        Assert.assertEquals(fakeResetResultTask, result)
    }

}