package com.example.plantago.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantago.dao.HistoricoDao


// Classe que implementa a Factory para criar instâncias de HistoricoViewModel
class HistoricoViewModelFactory(
    private val historicoDao: HistoricoDao // DAO necessário para o ViewModel
) : ViewModelProvider.Factory {

    // Método que cria e retorna uma instância de ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se a classe fornecida é compatível com HistoricoViewModel
        if (modelClass.isAssignableFrom(HistoricoViewModel::class.java)) {
            // Cria e retorna uma instância de HistoricoViewModel, passando o DAO
            return HistoricoViewModel(historicoDao) as T
        }
        // Lança uma exceção se a classe não for compatível
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
