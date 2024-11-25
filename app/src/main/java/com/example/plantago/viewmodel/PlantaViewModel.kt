package com.example.plantago.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantago.dao.PlantaDao
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch

// Classe que gerencia o estado e a lógica de negócios para a entidade Planta.
class PlantaViewModel(private val plantaDao: PlantaDao) : ViewModel() {

    // Variável que armazena a lista de plantas observável pela interface do usuário.
    // Usamos `mutableStateOf` para que o Jetpack Compose observe mudanças automaticamente.
    var listaPlantas = mutableStateOf(listOf<Planta>())
        private set // Encapsula a variável para que só o ViewModel possa alterá-la diretamente.

    // Bloco de inicialização: executa assim que o ViewModel é criado.
    init {
        carregarPlantas() // Chama o método para carregar todas as plantas do banco de dados.
    }

    // Função privada para carregar todas as plantas do banco de dados.
    private fun carregarPlantas() {
        // Usa viewModelScope para realizar operações assíncronas sem bloquear a interface.
        viewModelScope.launch {
            // Atualiza a lista de plantas com os dados retornados pelo DAO.
            listaPlantas.value = plantaDao.buscarTodas()
        }
    }

    // Função para salvar uma nova planta no banco de dados.
    // Recebe os dados necessários para criar a planta.
    fun salvarPlanta(nome: String, descricao: String, fotoUrl: String?, categoria: String): String {
        // Valida se os campos obrigatórios (nome, descrição, categoria) foram preenchidos.
        if (nome.isBlank() || descricao.isBlank() || categoria.isBlank()) {
            return "Preencha todos os campos obrigatórios!" // Retorna uma mensagem de erro.
        }

        // Cria um novo objeto Planta com os dados fornecidos.
        val planta = Planta(
            id = 0, // O ID será gerado automaticamente pelo banco de dados.
            nome = nome,
            descricao = descricao,
            fotoUrl = fotoUrl, // Foto é opcional, pode ser nula.
            categoria = categoria // Categoria agora é uma String.
        )

        // Lança uma coroutine para salvar a planta no banco de dados.
        viewModelScope.launch {
            plantaDao.inserirPlanta(planta) // Insere a planta no banco usando o DAO.
            carregarPlantas() // Atualiza a lista de plantas após a inserção.
        }

        return "Planta salva com sucesso!" // Retorna uma mensagem de sucesso.
    }

    // Função para excluir uma planta do banco de dados.
    fun excluirPlanta(planta: Planta) {
        // Lança uma coroutine para deletar a planta com base no ID.
        viewModelScope.launch {
            plantaDao.deletarPlantaPorId(planta.id) // Deleta a planta usando seu ID.
            carregarPlantas() // Atualiza a lista de plantas após a exclusão.
        }
    }

    // Função para atualizar os dados de uma planta existente.
    // Recebe o ID da planta, além dos novos valores para nome, descrição, foto e categoria.
    fun atualizarPlanta(id: Int, nome: String, desc: String, fotoUrl: String?, categoria: String): String {
        // Valida se os campos obrigatórios (nome, descrição, categoria) foram preenchidos.
        if (nome.isBlank() || desc.isBlank() || categoria.isBlank()) {
            return "Preencha todos os campos obrigatórios!" // Retorna uma mensagem de erro.
        }

        // Busca a planta na lista atual pelo ID fornecido.
        val planta = listaPlantas.value.find { it.id == id } ?: return "Erro ao atualizar planta"
        // Cria uma cópia da planta com os novos valores.
        val plantaAtualizada = planta.copy(
            nome = nome,
            descricao = desc,
            fotoUrl = fotoUrl, // Atualiza a URL da foto, se fornecida.
            categoria = categoria // Atualiza a categoria como String.
        )

        // Lança uma coroutine para atualizar a planta no banco de dados.
        viewModelScope.launch {
            plantaDao.atualizarPlanta(plantaAtualizada) // Atualiza a planta no banco usando o DAO.
            carregarPlantas() // Atualiza a lista de plantas após a modificação.
        }

        return "Planta atualizada com sucesso!" // Retorna uma mensagem de sucesso.
    }
}
