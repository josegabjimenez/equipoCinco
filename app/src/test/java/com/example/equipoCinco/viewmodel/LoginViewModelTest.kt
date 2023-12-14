package com.example.equipoCinco.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipoCinco.repository.LoginRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class LoginViewModelTest {

    @Mock
    lateinit var firebaseAuth: FirebaseAuth

    @get:Rule
    val rule = InstantTaskExecutorRule() //código que involucra LiveData y ViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository
    @Captor
    private lateinit var isRegisterCaptor: ArgumentCaptor<(Boolean) -> Unit>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginRepository = mock(LoginRepository::class.java)
        loginViewModel = LoginViewModel(loginRepository,firebaseAuth)
        println("LoginRepository instance in test: $loginRepository")
        println("LoginRepository instance in ViewModel: ${loginViewModel.getRepository()}")
    }

    @Test
    fun testRegisterUser() {
        val email = "test@example.com"
        val pass = "123456"

        // Configuración del comportamiento simulado del repository
        Mockito.`when`(loginRepository.registerUser(eq(email), eq(pass), any()))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<(Boolean) -> Unit>(2)
                // Simular que la respuesta es exitosa
                callback.invoke(true)
            }

        // Llamada al método que estás probando
        loginViewModel.registerUser(email, pass) { isRegister ->
            // Verificación de resultados usando assertions
            assertTrue(isRegister)
        }
    }

    @Test
    fun testLoginUser() {
        val email = "roncancio.juan@correounivalle.edu.co"
        val pass = "123456"

        // Configuración del comportamiento simulado de FirebaseAuth
        val successfulTask = Tasks.forResult<AuthResult>(Mockito.mock(AuthResult::class.java))
        val failedTask = Tasks.forException<AuthResult>(Exception("Simulated Failure"))

        val mockFirebaseAuth = mock(FirebaseAuth::class.java)
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(email, pass)).thenReturn(successfulTask)
        whenever(mockFirebaseAuth.signInWithEmailAndPassword("", "")).thenReturn(failedTask)

        /*
        // Llamada al método que estás probando
            loginViewModel.loginUser(email, pass) { isLogin ->
            // Verificación de resultados usando assertions
            assertTrue(isLogin)
        }
*/
        // Llamada al método que estás probando con valores vacíos
        loginViewModel.loginUser("", "") { isLogin ->
            // Verificación de resultados usando assertions
            assertFalse(isLogin)
        }
    }
}



