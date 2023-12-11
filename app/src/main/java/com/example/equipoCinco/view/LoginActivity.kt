package com.example.equipoCinco.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.equipoCinco.R
import com.example.equipoCinco.databinding.ActivityLoginBinding
import com.example.equipoCinco.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)

        checkSession()
        sesion()
        setup()
        setupPasswordVisibilityToggle()
        //disabledButton()

        tilPassword = findViewById(R.id.tilPass)
        etPassword = findViewById(R.id.etPass)
        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.tvRegister)

        val emailText = etEmail.text.toString()
        val passwordText = etPassword.text.toString()

        btnLogin.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
        btnRegister.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
        // Agregar TextWatcher para el campo de email
        etEmail.addTextChangedListener(createTextWatcher())

        // Agregar TextWatcher para el campo de contraseña
        etPassword.addTextChangedListener(createTextWatcher())


        // Agregar TextWatcher para el campo de email

        etPassword.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        // Agregar un TextWatcher al EditText para realizar la validación en tiempo real
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                validatePassword(editable.toString())
            }
        })
    }
    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun saveSession(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
    private fun registerUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.registerUser(email,pass) { isRegister ->
            if (isRegister) {
                saveSession(email)
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
                saveSession(email)
                goToHome()
            }else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordVisibilityToggle() {
        val etPass = findViewById<EditText>(R.id.etPass)

        etPass.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (etPass.right - etPass.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Cambiar la visibilidad de la contraseña
                    val inputType = etPass.inputType
                    if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        etPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        etPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ojo_alternancia_2, 0)
                    } else {
                        etPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        etPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ojo_alternancia_1, 0)
                    }

                    // Mueve el cursor al final del texto
                    etPass.setSelection(etPass.text.length)

                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun validatePassword(password: String) {
        // Validar longitud mínima y máxima
        if (password.length < 6 || password.length > 10) {
            tilPassword.error = "Mínimo 6 y máximo 10 dígitos"
            tilPassword.isErrorEnabled = true
            tilPassword.boxStrokeColor = resources.getColor(R.color.red) // Color rojo
        } else {
            tilPassword.error = null
            tilPassword.isErrorEnabled = false
            tilPassword.boxStrokeColor = resources.getColor(R.color.white) // Color blanco
        }
    }



    private fun sesion(){
        val email = sharedPreferences.getString("email",null)
        loginViewModel.sesion(email){ isEnableView ->
            if (isEnableView){
                binding.clContenedor.visibility = View.INVISIBLE
                goToHome()
            }
        }
    }



    private fun checkSession() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // El usuario ya ha iniciado sesión, navega a la actividad principal u otra actividad necesaria.
            goToHome()
        } else {
            // El usuario no ha iniciado sesión, muestra la interfaz de inicio de sesión.
        }
    }

            private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                // Verificar si ambos campos están vacíos para desactivar el botón
                val emailText = etEmail.text.toString()
                val passwordText = etPassword.text.toString()

                btnLogin.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
                btnRegister.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}