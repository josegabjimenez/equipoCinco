package com.example.equipoCinco.repository
import com.appmovil.loginfirestore.view.widget.InventoryAppWidget
import com.example.equipoCinco.data.InventoryDao
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.model.Product
import com.example.equipoCinco.webservice.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class InventoryRepository  @Inject constructor(
    private val inventoryDao: InventoryDao,
    private val apiService: ApiService,
    private val db: FirebaseFirestore,
) {

    private fun calculateTotalEarnings() {
        // Reset accumulated value
        InventoryAppWidget.totalSum = 0

        db.collection("inventory").get()
            .addOnSuccessListener { querySnapshot ->

                for (document in querySnapshot.documents) {
                    val price = document.getLong("price") ?: 0
                    val quantity = document.getLong("quantity") ?: 0

                    val productTotal = price.toInt() * quantity.toInt()
                    InventoryAppWidget.totalSum += productTotal
                }

            }
            .addOnFailureListener { exception ->
                // Handle failures
                println("Error: $exception")
            }
    }
    suspend fun saveInventory(inventory: Inventory) {
        withContext(Dispatchers.IO) {
            db.collection("inventory").document(inventory.id.toString()).set(
                hashMapOf(
                    "id" to inventory.id,
                    "name" to inventory.name,
                    "price" to inventory.price,
                    "quantity" to inventory.quantity
                )
            )
            calculateTotalEarnings()
        }

        /*withContext(Dispatchers.IO){
             inventoryDao.saveInventory(inventory)
         }*/
    }

    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            try {
                val snapshot = db.collection("inventory").get().await()
                val inventoryList = mutableListOf<Inventory>()
                for (document in snapshot.documents) {
                    val id = document.getLong("id")?.toInt() ?: 0
                    val name = document.getString("name") ?: ""
                    val price = document.getLong("price")?.toInt() ?: 0
                    val quantity = document.getLong("quantity")?.toInt() ?: 0

                    val item = Inventory(id, name, price, quantity)
                    inventoryList.add(item)
                }

                inventoryList
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        }
    }

    suspend fun deleteInventory(inventory: Inventory){
        withContext(Dispatchers.IO) {
                // Eliminar el documento en Firebase Firestore
                db.collection("inventory").document(inventory.id.toString()).delete()
                calculateTotalEarnings()
                // También puedes eliminar el objeto localmente si es necesario
                // inventoryDao.deleteInventory(inventory)
        }
    }

    suspend fun updateRepositoy(inventory: Inventory) {
        withContext(Dispatchers.IO) {
            db.collection("inventory").document(inventory.id.toString()).set(
                hashMapOf(
                    "id" to inventory.id,
                    "name" to inventory.name,
                    "price" to inventory.price,
                    "quantity" to inventory.quantity
                )
            )
            calculateTotalEarnings()
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




