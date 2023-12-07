package com.example.equipoCinco.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.model.Product
import com.example.equipoCinco.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {


    private val _listInventory = MutableLiveData<MutableList<Inventory>>()
    val listInventory: LiveData<MutableList<Inventory>> get() = _listInventory

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    //para almacenar una lista de productos
    private val _listProducts = MutableLiveData<MutableList<Product>>()
    val listProducts: LiveData<MutableList<Product>> = _listProducts

    fun saveInventory(inventory: Inventory) {
        viewModelScope.launch {

            _progresState.value = true
            try {
                inventoryRepository.saveInventory(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListInventory() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listInventory.value = inventoryRepository.getListInventory()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun deleteInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                inventoryRepository.deleteInventory(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun updateInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                inventoryRepository.updateRepositoy(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listProducts.value = inventoryRepository.getProducts()
                _progresState.value = false

            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun totalProducto(price: Int, quantity: Int): Double {
        val total = price * quantity
        return total.toDouble()
    }

    fun registerUser(email: String, pass: String, isRegister: (Boolean) -> Unit) {
        inventoryRepository.registerUser(email, pass) { response ->
            isRegister(response)
        }
    }

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isLogin(true)
                    } else {
                        isLogin(false)
                    }
                }
        } else {
            isLogin(false)
        }
    }

    fun sesion(email: String?, isEnableView: (Boolean) -> Unit) {
        if (email != null) {
            isEnableView(true)
        } else {
            isEnableView(false)
        }
    }
}


