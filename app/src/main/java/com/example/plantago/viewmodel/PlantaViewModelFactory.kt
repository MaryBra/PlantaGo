package com.example.plantago.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantago.dao.PlantaDao

class PlantaViewModelFactory(
    private val plantaDao: PlantaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantaViewModel::class.java)) {
            return PlantaViewModel(plantaDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}