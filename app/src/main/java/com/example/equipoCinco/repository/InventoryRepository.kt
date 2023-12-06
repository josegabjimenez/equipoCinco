package com.example.equipoCinco.repository
import com.example.equipoCinco.data.InventoryDao
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.model.Product
import com.example.equipoCinco.webservice.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InventoryRepository  @Inject constructor(
    private val inventoryDao: InventoryDao,
    private val apiService: ApiService
){
     suspend fun saveInventory(inventory:Inventory){
         withContext(Dispatchers.IO){
             inventoryDao.saveInventory(inventory)
         }
     }

    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            inventoryDao.getListInventory()
        }
    }

    suspend fun deleteInventory(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.deleteInventory(inventory)
        }
    }

    suspend fun updateRepositoy(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.updateInventory(inventory)
        }
    }

    suspend fun getProducts(): MutableList<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts()
                response
            } catch (e: Exception) {

                e.printStackTrace()
                mutableListOf()
            }
        }
    }
}