package com.example.plantago.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantago.dao.PlantaDao
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch

class PlantaViewModel(private val plantaDao: PlantaDao) : ViewModel() {

    var listaPlantas = mutableStateOf(listOf<Planta>())
        private set

    init {
        carregarPlantas()
    }

    private fun carregarPlantas() {
        viewModelScope.launch {
            listaPlantas.value = plantaDao.buscarTodas()
        }
    }

    fun salvarPlanta(nome: String, especie: String, fotoUrl: String?, categoriaId: Int): String {
        if (nome.isBlank() || especie.isBlank()) {
            return "Preencha todos os campos obrigatórios!"
        }

        val planta = Planta(
            id = 0,
            nome = nome,
            especie = especie,
            fotoUrl = fotoUrl,
            categoriaId = categoriaId
        )

        viewModelScope.launch {
            plantaDao.inserirPlanta(planta)
            carregarPlantas()
        }

        return "Planta salva com sucesso!"
    }

    fun excluirPlanta(planta: Planta) {
        viewModelScope.launch {
            plantaDao.deletarPlantaPorId(planta.id) // Passando o id da planta em vez do objeto
            carregarPlantas()
        }
    }

    fun atualizarPlanta(id: Int, nome: String, especie: String, fotoUrl: String?, categoriaId: Int): String {
        if (nome.isBlank() || especie.isBlank()) {
            return "Preencha todos os campos obrigatórios!"
        }

        val planta = listaPlantas.value.find { it.id == id } ?: return "Erro ao atualizar planta"
        val plantaAtualizada = planta.copy(
            nome = nome,
            especie = especie,
            fotoUrl = fotoUrl,
            categoriaId = categoriaId
        )

        viewModelScope.launch {
            plantaDao.atualizarPlanta(plantaAtualizada)
            carregarPlantas()
        }

        return "Planta atualizada com sucesso!"
    }
}
