package com.example.equipoCinco.repository
import com.example.equipoCinco.data.InventoryDao
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.model.Product
import com.example.equipoCinco.webservice.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class InventoryRepository  @Inject constructor(
    private val inventoryDao: InventoryDao,
    private val apiService: ApiService,
    private val db: FirebaseFirestore,
    private val db_auth: FirebaseAuth = FirebaseAuth.getInstance()
){
     suspend fun saveInventory(inventory:Inventory){
         withContext(Dispatchers.IO){
             db.collection("inventory").document(inventory.id.toString()).set(
                 hashMapOf(
                     "id" to inventory.id,
                     "name" to inventory.name,
                     "price" to inventory.price,
                     "quantity" to inventory.quantity
                 )
             )
         }

         /*withContext(Dispatchers.IO){
             inventoryDao.saveInventory(inventory)
         }*/
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

     fun registerUser(email: String, pass:String, isRegisterComplete: (Boolean)->Unit){
        if(email.isNotEmpty() && pass.isNotEmpty()){
            db_auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isRegisterComplete(true)
                    } else {
                        isRegisterComplete(false)
                    }
                }
        }else{
            isRegisterComplete(false)
        }
    }

}


