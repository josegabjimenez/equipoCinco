package com.example.equipoCinco.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.model.Product
import com.example.equipoCinco.repository.InventoryRepository

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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class InventoryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule() //código que involucra LiveData y ViewModel
    private lateinit var inventoryViewModel: InventoryViewModel
    private lateinit var inventoryRepository: InventoryRepository

    @Before
    fun setUp() {
        inventoryRepository = mock(InventoryRepository::class.java)
        inventoryViewModel = InventoryViewModel(inventoryRepository)
    }

    @Test
    fun `test método totalProducto`(){
        //given (qué necesitamos:condiciones previas necesarias para que la equipoCinco se ejecute correctamente)
        val price = 10
        val quantity = 5
        val expectedResult = (price * quantity).toDouble()

        //when (Aquí, ejecutas el código o la función que estás evaluando.)
        val resul = inventoryViewModel.totalProducto(price, quantity)

        //Then (lo que tiene que pasar:resultados esperados )
        assertEquals(expectedResult, resul,0.0)
    }

    @Test
    fun `test método getProducts`() = runBlocking {
        //given
        // es responsable de ejecutar tareas en el hilo principal, necesitamos simular ese proceso
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Configurar el comportamiento del repositorio simulado
        val mockProducts = mutableListOf(
            Product(0, "zapatos", "hola como estas")
        )
        `when`(inventoryRepository.getProducts()).thenReturn(mockProducts)

        // Llamar a la función que queremos probar
        //when
        inventoryViewModel.getProducts()

        // Asegurarse de que la LiveData de productos se haya actualizado correctamente
        //then
        assertEquals(inventoryViewModel.listProducts.value, mockProducts)
// son utilizados para controlar y simular la ejecución de coroutines en el hilo principal durante las pruebas unitarias
        Dispatchers.resetMain()
    }

    @Test
    fun testSaveInventory_success() = runBlocking {
        //given
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val inventory= Inventory(id = 1, name = "Item1", price = 10, quantity = 5)
        `when`(inventoryRepository.saveInventory(inventory))
            .thenAnswer { invocation ->
                val inventoryArgument = invocation.getArgument<Inventory>(0)//inventoryArgument contendrá el valor del primer argumento que se pasó al método
                inventoryArgument
            }

        // Llamamos al método que queremos probar
        inventoryViewModel.saveInventory(inventory)

        // Verificamos que el estado de progreso sea falso después de la operación
        verify(inventoryRepository).saveInventory(inventory)
        Dispatchers.resetMain()
    }
}