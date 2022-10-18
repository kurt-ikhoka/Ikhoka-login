package com.ikhoka.login

import app.cash.turbine.test
import com.ikhoka.core.Resource
import com.ikhoka.core.isError
import com.ikhoka.core.isLoading
import com.ikhoka.core.isSuccess
import com.ikhoka.login.meta.LoginRequest
import com.ikhoka.login.meta.LoginState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.ExperimentalTime


@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
internal class LoginViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repo: LoginRepository = mockk()


    lateinit var model: LoginViewModel

    @Before
    fun setup() {
        model = LoginViewModel(repo, stateReplay = 2)
    }

    @Test
    fun `when login is successful login state changes to authenticated`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { repo.login(any()) }.returns(
            flow {
                emit(Resource.loading())
                emit(Resource.success(LoginState.AUTHENTICATED))
            }
        )

        model.login(LoginRequest("john","doe"))

        model.authState.test {
            val state1 = awaitItem()
            state1.isLoading() shouldBe true
            assertNull(state1.data)
            assertNull(state1.error)

            val state2 = awaitItem()
            state2.isSuccess() shouldBe true
            state2.data shouldBeEqualTo LoginState.AUTHENTICATED
            assertNull(state2.error)
            expectNoEvents()
        }
    }


    @Test
    fun `when login is not successful login error state is emitted`() = runTest(UnconfinedTestDispatcher()) {

        val error = Exception("failed to login")
        coEvery { repo.login(any()) }.returns(
            flow {
                emit(Resource.loading())
                emit(Resource.error(error))
            }
        )

        model.login(LoginRequest("john","doe"))

        model.authState.test {
            val state1 = awaitItem()
            state1.isLoading() shouldBe true
            assertNull(state1.data)
            assertNull(state1.error)

            val state2 = awaitItem()
            state2.isError() shouldBe true
            state2.error `should be` error
            assertNotNull(state2.error)
            expectNoEvents()
        }
    }
}