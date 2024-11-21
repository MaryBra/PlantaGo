package com.example.plantago.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantago.dao.AnotacaoDao

class AnotacaoViewModelFactory(
    private val anotacaoDao: AnotacaoDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnotacaoViewModel::class.java)) {
            return AnotacaoViewModel(anotacaoDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
