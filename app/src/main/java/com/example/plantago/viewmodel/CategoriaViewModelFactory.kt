package com.example.plantago.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantago.dao.CategoriaDao

class CategoriaViewModelFactory(
    private val categoriaDao: CategoriaDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
            return CategoriaViewModel(categoriaDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
