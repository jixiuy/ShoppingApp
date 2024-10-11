package com.example.shoppingapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.repository.ProductRepository

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    val productListLiveData = MutableLiveData<Result<List<Product>>>()

    fun listProducts(driverId: String, page: Int, size: Int) {
        repository.listProducts(driverId, page, size) { result ->
            productListLiveData.value = result
        }
    }
}