package com.example.plantago.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantago.dao.HistoricoDao
import com.example.plantago.model.Historico
import kotlinx.coroutines.launch

// Classe que define o ViewModel para gerenciar o histórico de regas
class HistoricoViewModel(private val historicoDao: HistoricoDao) : ViewModel() {

    // Variável que armazena a lista de históricos e pode ser observada pela interface do usuário.
    var listaHistoricos = mutableStateOf(listOf<Historico>())
        private set // Torna a variável apenas legível de fora da classe para manter a encapsulação.

    // Bloco de inicialização que é executado quando a ViewModel é criada.
    init {
        carregarHistoricos() // Chama a função para carregar os históricos do banco de dados.
    }

    // Função privada para carregar os históricos de regas do banco de dados.
    private fun carregarHistoricos() {
        viewModelScope.launch {
            // Lança uma coroutine no escopo do ViewModel para realizar a operação de forma assíncrona.
            listaHistoricos.value = historicoDao.buscarTodos()
            // Atualiza o estado com a lista de históricos recuperada do DAO.
        }
    }

    // Função para salvar um novo histórico de rega.
    // Recebe o ID da planta, a data/hora e um booleano indicando se foi regada.
    fun salvarHistorico(plantaId: Int, dataHora: String, rega: Boolean): String {
        // Valida se a data/hora foi preenchida.
        if (dataHora.isBlank()) {
            return "Data e hora devem ser preenchidas!" // Retorna uma mensagem de erro se inválido.
        }

        // Cria um objeto Histórico com os dados fornecidos.
        val historico = Historico(
            id = 0, // O ID será gerado automaticamente pelo banco de dados.
            plantaId = plantaId, // ID da planta associada.
            dataHora = dataHora, // Data e hora do evento.
            rega = rega // Indica se foi um registro de rega.
        )

        // Lança uma coroutine para salvar o histórico no banco de dados.
        viewModelScope.launch {
            historicoDao.inserirHistorico(historico) // Chama o DAO para inserir o histórico.
            carregarHistoricos() // Atualiza a lista de históricos após a inserção.
        }

        return "Histórico salvo com sucesso!" // Retorna uma mensagem de sucesso.
    }
}