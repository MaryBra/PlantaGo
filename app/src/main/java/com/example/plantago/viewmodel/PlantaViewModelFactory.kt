package com.example.plantago.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantago.dao.PlantaDao

// Classe que implementa o padrão de fábrica (Factory) para criar instâncias de PlantaViewModel.
class PlantaViewModelFactory(
    private val plantaDao: PlantaDao // Dependência necessária para criar PlantaViewModel.
) : ViewModelProvider.Factory {

    // Método responsável por criar e retornar uma instância do ViewModel solicitado.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se o ViewModel solicitado é do tipo PlantaViewModel.
        if (modelClass.isAssignableFrom(PlantaViewModel::class.java)) {
            // Cria e retorna uma nova instância de PlantaViewModel com o DAO injetado.
            return PlantaViewModel(plantaDao) as T
        }
        // Caso a classe solicitada não seja compatível, lança uma exceção.
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
