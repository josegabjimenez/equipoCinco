package com.example.equipoCinco.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.equipoCinco.R
import com.example.equipoCinco.databinding.ActivityLoginBinding
import com.example.equipoCinco.viewmodel.InventoryViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: InventoryViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sesion()
        setup()
    }
    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }
    private fun registerUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.registerUser(email,pass) { isRegister ->
            if (isRegister) {
                goToHome()
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }

        }
    }
    /*
    private fun goToHome(email: String?){
        val intent = Intent (this, MainActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
        finish()
    }
    */
    private fun goToHome(){
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun loginUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.loginUser(email,pass){ isLogin ->
            if (isLogin){
                goToHome()
            }else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sesion(){
        val email = sharedPreferences.getString("email",null)
        InventoryViewModel.sesion(email){ isEnableView ->
            if (isEnableView){
                binding.clContenedor.visibility = View.INVISIBLE
                goToHome()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}