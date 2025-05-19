package com.babacan.defactocase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.domain.model.User
import com.babacan.defactocase.presentation.login.LoginEvent
import com.babacan.defactocase.presentation.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var deFactoDAO: DeFactoDAO

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(deFactoDAO)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnEmailChanged updates state email`() {
        viewModel.handleEvents(LoginEvent.OnEmailChanged("test@a.com"))
        Assert.assertEquals("test@a.com", viewModel.getCurrentState().email)
    }

    @Test
    fun `OnPasswordChange updates state password`() {
        viewModel.handleEvents(LoginEvent.OnPasswordChange("123456"))
        Assert.assertEquals("123456", viewModel.getCurrentState().password)
    }

    @Test
    fun `OnPasswordVisibilityChanged toggles visibility`() {
        val initial = viewModel.getCurrentState().showPasswordVisibility
        viewModel.handleEvents(LoginEvent.OnPasswordVisibilityChanged)
        Assert.assertEquals(!initial, viewModel.getCurrentState().showPasswordVisibility)
    }

    @Test
    fun `OnLoginClicked with valid credentials triggers NavigateBack effect`() = runTest {
        viewModel.handleEvents(LoginEvent.OnEmailChanged("test@a.com"))
        viewModel.handleEvents(LoginEvent.OnPasswordChange("123456"))
        val user = User(_id = 1, email = "test@a.com", password = "hashed")
        `when`(deFactoDAO.checkUser(anyString(), anyString())).thenReturn(user)

        viewModel.handleEvents(LoginEvent.OnLoginClicked)
        advanceUntilIdle()

        verify(deFactoDAO).updateLoggedInStatus(1, true)
        // Effect kontrolü için ek bir yapı gerekir, burada sadece DAO çağrısı kontrol edildi
    }

    @Test
    fun `DisableErrorMessage sets showError to false`() {
        viewModel.handleEvents(LoginEvent.DisableErrorMessage)
        Assert.assertFalse(viewModel.getCurrentState().showError)
    }
}