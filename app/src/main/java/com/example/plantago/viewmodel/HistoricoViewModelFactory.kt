package com.example.plantago.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantago.dao.HistoricoDao

class HistoricoViewModelFactory(
    private val historicoDao: HistoricoDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricoViewModel::class.java)) {
            return HistoricoViewModel(historicoDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}