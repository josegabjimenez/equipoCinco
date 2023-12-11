package com.example.equipoCinco.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoCinco.R

import androidx.databinding.DataBindingUtil
import com.example.equipoCinco.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}