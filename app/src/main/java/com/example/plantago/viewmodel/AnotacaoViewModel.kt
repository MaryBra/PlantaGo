package com.example.plantago.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantago.dao.AnotacaoDao
import com.example.plantago.model.Anotacao
import kotlinx.coroutines.launch

class AnotacaoViewModel(private val anotacaoDao: AnotacaoDao) : ViewModel() {

    var listaAnotacoes = mutableStateOf(listOf<Anotacao>())
        private set

    init {
        carregarAnotacoes()
    }

    private fun carregarAnotacoes() {
        viewModelScope.launch {
            listaAnotacoes.value = anotacaoDao.buscarTodas()
        }
    }

    fun salvarAnotacao(nome: String, descricao: String, data: String, plantaId: Int): String {
        if (nome.isBlank() || descricao.isBlank() || data.isBlank()) {
            return "Preencha todos os campos!"
        }

        val anotacao = Anotacao(
            id = 0,
            nome = nome,
            descricao = descricao,
            data = data,
            plantaId = plantaId
        )

        viewModelScope.launch {
            anotacaoDao.inserirAnotacao(anotacao)
            carregarAnotacoes()
        }

        return "Anotação salva com sucesso!"
    }
}