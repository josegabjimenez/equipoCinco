package com.example.equipoCinco.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.equipoCinco.R
import com.example.equipoCinco.databinding.FragmentItemDetailsBinding
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {
    private lateinit var binding: FragmentItemDetailsBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.contentToolbar.toolbar
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Detalle del producto"

        val backButton = toolbar.findViewById<ImageView>(R.id.backBtn)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        dataInventory()
        controladores()


    }

    private fun controladores() {
        binding.btnDelete.setOnClickListener {
            deleteInventory()
        }

        binding.fbEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataInventory", receivedInventory)
            findNavController().navigate(R.id.action_itemDetailsFragment_to_itemEditFragment, bundle)
        }
    }

    private fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES"))
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        return numberFormat.format(price)
    }


    private fun dataInventory() {
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("clave") as Inventory
        binding.tvItem.text = "${receivedInventory.name}"

        val formattedPrice = formatPrice(receivedInventory.price.toDouble())
        binding.tvPrice.text = "$ ${formattedPrice}"
        binding.tvQuantity.text = "${receivedInventory.quantity}"

        val total = inventoryViewModel.totalProducto(
            receivedInventory.price,
            receivedInventory.quantity
        )

        val formattedTotal = formatPrice(total)

        binding.txtTotal.text = "$ ${formattedTotal}"
    }

    private fun deleteInventory(){
        inventoryViewModel.deleteInventory(receivedInventory)
        inventoryViewModel.getListInventory()
        findNavController().popBackStack()
    }



}