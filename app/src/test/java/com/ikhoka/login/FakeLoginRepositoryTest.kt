package com.ikhoka.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.ikhoka.core.isLoading
import com.ikhoka.core.isSuccess
import com.ikhoka.login.meta.LoginRequest
import com.ikhoka.login.meta.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertNull
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
@RunWith(JUnit4::class)
internal class FakeLoginRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeLoginRepository


    @Before
    fun setUp() {
        repository = FakeLoginRepository()
    }

    @Test
    fun `when login is requested a successfull state is returned`() = runTest {
        repository.login(LoginRequest("john", "doe")).test {
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
}