package com.example.equipoCinco.webservice

import com.example.equipoCinco.model.Product
import com.example.equipoCinco.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getProducts(): MutableList<Product>
}