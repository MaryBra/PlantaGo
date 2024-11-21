package com.example.plantago.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantago.dao.CategoriaDao
import com.example.plantago.model.Categoria
import kotlinx.coroutines.launch

class CategoriaViewModel(private val categoriaDao: CategoriaDao) : ViewModel() {

    var listaCategorias = mutableStateOf(listOf<Categoria>())
        private set

    init {
        carregarCategorias()
    }

    private fun carregarCategorias() {
        viewModelScope.launch {
            listaCategorias.value = categoriaDao.buscarTodas()
        }
    }

    fun salvarCategoria(nome: String, codigo: String): String {
        if (nome.isBlank() || codigo.isBlank()) {
            return "Preencha todos os campos!"
        }

        val categoria = Categoria(id = 0, nome = nome, codigo = codigo)

        viewModelScope.launch {
            categoriaDao.inserirCategoria(categoria)
            carregarCategorias()
        }

        return "Categoria salva com sucesso!"
    }
}
