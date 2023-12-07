package com.example.equipoCinco.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoCinco.R

import androidx.databinding.DataBindingUtil
import com.example.equipoCinco.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
    }
}