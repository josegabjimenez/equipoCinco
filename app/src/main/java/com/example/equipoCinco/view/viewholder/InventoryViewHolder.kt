package com.example.equipoCinco.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.equipoCinco.R
import com.example.equipoCinco.databinding.ItemInventoryBinding
import com.example.equipoCinco.model.Inventory
import java.text.NumberFormat
import java.util.*

class InventoryViewHolder(binding: ItemInventoryBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController
    fun setItemInventory(inventory: Inventory) {
        val formattedPrice = formatPrice(inventory.price.toDouble())
        bindingItem.tvName.text = inventory.name
        bindingItem.tvPrice.text = "$ $formattedPrice"
        bindingItem.tvId.text = "Id: ${inventory.id}"

        bindingItem.cvInventory.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", inventory)
            navController.navigate(R.id.action_homeInventoryFragment_to_itemDetailsFragment, bundle)
        }

    }

    private fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES"))
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        return numberFormat.format(price)
    }
}