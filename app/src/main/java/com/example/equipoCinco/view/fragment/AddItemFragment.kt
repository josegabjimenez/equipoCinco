package com.example.equipoCinco.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.equipoCinco.databinding.FragmentAddItemBinding
import com.example.equipoCinco.model.Inventory
import com.example.equipoCinco.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
    }

    private fun controladores() {
        validarDatos()
        binding.btnSaveInventory.setOnClickListener {
            saveInventory()
        }
    }

    private fun saveInventory(){
        val id = binding.etId.text.toString()
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString()
        val quantity = binding.etQuantity.text.toString()

        if (id.isNotEmpty() && name.isNotEmpty() && price.isNotEmpty() && quantity.isNotEmpty()) {
            val inventory = Inventory(id = id.toInt(), name = name, price = price.toInt(), quantity = quantity.toInt())
            inventoryViewModel.saveInventory(inventory)
            Log.d("test",inventory.toString())
            Toast.makeText(context,"Artículo guardado !!", Toast.LENGTH_SHORT).show()
            cleanFields()
            findNavController().popBackStack()
        } else {
            Toast.makeText(context, "Llene los campos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun cleanFields() {
        binding.etId.setText("")
        binding.etName.setText("")
        binding.etPrice.setText("")
        binding.etQuantity.setText("")
    }

    private fun validarDatos() {
        val listEditText = listOf(binding.etId, binding.etName, binding.etPrice, binding.etQuantity)

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all{
                    it.text.isNotEmpty() // si toda la lista no está vacía
                }
                binding.btnSaveInventory.isEnabled = isListFull
            }
        }
    }
}