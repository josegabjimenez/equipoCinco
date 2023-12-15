package com.example.equipoCinco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.equipoCinco.R
import com.example.equipoCinco.databinding.FragmentItemEditBinding
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemEditFragment : Fragment() {
    private lateinit var binding: FragmentItemEditBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInventory()
        controladores()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.etName.addTextChangedListener(textWatcher)
        binding.etPrice.addTextChangedListener(textWatcher)
        binding.etQuantity.addTextChangedListener(textWatcher)

    }

    private fun controladores(){
        binding.btnEdit.setOnClickListener {
            updateInventory()
        }
    }

    private fun dataInventory(){
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("dataInventory") as Inventory
        binding.etName.setText(receivedInventory.name)
        binding.etPrice.setText(receivedInventory.price.toString())
        binding.etQuantity.setText(receivedInventory.quantity.toString())
        binding.ID.text = "Id: ${receivedInventory.id}"

    }

    private fun updateInventory(){
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString().toInt()
        val quantity = binding.etQuantity.text.toString().toInt()
        val inventory = Inventory(receivedInventory.id, name,price,quantity)
        inventoryViewModel.updateInventory(inventory)
        findNavController().navigate(R.id.action_itemEditFragment_to_homeInventoryFragment)

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No se usa, pero se debe implementar
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Verificar si alguno de los campos está vacío
            val isNameEmpty = binding.etName.text.toString().isEmpty()
            val isPriceEmpty = binding.etPrice.text.toString().isEmpty()
            val isQuantityEmpty = binding.etQuantity.text.toString().isEmpty()

            // Habilitar o deshabilitar el botón según las condiciones
            binding.btnEdit.isEnabled = !isNameEmpty && !isPriceEmpty && !isQuantityEmpty
        }

        override fun afterTextChanged(s: Editable?) {
            // No se usa, pero se debe implementar
        }
    }
}