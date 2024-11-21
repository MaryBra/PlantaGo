package com.example.plantago.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantago.dao.HistoricoDao
import com.example.plantago.model.Historico
import kotlinx.coroutines.launch

class HistoricoViewModel(private val historicoDao: HistoricoDao) : ViewModel() {

    var listaHistoricos = mutableStateOf(listOf<Historico>())
        private set

    init {
        carregarHistoricos()
    }

    private fun carregarHistoricos() {
        viewModelScope.launch {
            listaHistoricos.value = historicoDao.buscarTodos()
        }
    }

    fun salvarHistorico(plantaId: Int, dataHora: String, rega: Boolean): String {
        if (dataHora.isBlank()) {
            return "Data e hora devem ser preenchidas!"
        }

        val historico = Historico(
            id = 0,
            plantaId = plantaId,
            dataHora = dataHora,
            rega = rega
        )

        viewModelScope.launch {
            historicoDao.inserirHistorico(historico)
            carregarHistoricos()
        }

        return "Histórico salvo com sucesso!"
    }

}