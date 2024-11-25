package com.example.plantago.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.plantago.dao.PlantaDao
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch
// Função Composable para a tela de edição de uma planta existente.
// Permite modificar as informações de uma planta, incluindo nome, descrição, categoria e foto.
@Composable
fun TelaEditarPlanta(plantaId: Int, plantaDao: PlantaDao, navController: NavHostController) {
    // Variável de estado que armazena a planta atual sendo editada.
    var planta by remember { mutableStateOf<Planta?>(null) }

    // Escopo de coroutine para realizar operações assíncronas, como acessar o banco de dados.
    val coroutineScope = rememberCoroutineScope()

    // `LaunchedEffect` é executado quando o composable é carregado ou quando `plantaId` muda.
    LaunchedEffect(plantaId) {
        // Busca a planta no banco de dados com base no ID fornecido.
        planta = plantaDao.obterPlantaPorId(plantaId)
    }

    // Verifica se a planta foi carregada antes de exibir o formulário de edição.
    if (planta != null) {
        // Estados para armazenar os valores editáveis do formulário.
        var nome by remember { mutableStateOf(planta!!.nome) } // Nome da planta.
        var descricao by remember { mutableStateOf(planta!!.descricao) } // Descrição da planta.
        var categoria by remember { mutableStateOf(planta!!.categoria) } // Categoria da planta.
        var fotoUri by remember { mutableStateOf(planta!!.fotoUrl?.let { Uri.parse(it) }) } // URI da foto, se existir.

        // Lançador para selecionar uma foto da galeria.
        val selecionarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Atualiza o estado do URI da foto selecionada.
            if (uri != null) {
                fotoUri = uri
            }
        }

        // Layout principal da tela.
        Column(
            modifier = Modifier
                .fillMaxSize() // Preenche toda a área disponível.
                .padding(16.dp), // Adiciona espaçamento interno.
            horizontalAlignment = Alignment.CenterHorizontally // Centraliza os itens horizontalmente.
        ) {
            // Título da tela.
            Text(
                text = "Editar Planta", // Texto do título.
                style = MaterialTheme.typography.headlineMedium, // Estilo de texto do título.
                fontWeight = FontWeight.Bold, // Peso da fonte.
                color = MaterialTheme.colorScheme.primary, // Cor definida pelo tema.
                modifier = Modifier.padding(bottom = 24.dp) // Espaçamento abaixo do título.
            )

            // Campo de texto para editar o nome da planta.
            TextField(
                value = nome, // Valor atual do campo.
                onValueChange = { nome = it }, // Atualiza o estado ao alterar o texto.
                label = { Text("Nome da Planta") }, // Rótulo do campo.
                modifier = Modifier
                    .fillMaxWidth() // O campo ocupa toda a largura disponível.
                    .padding(bottom = 16.dp) // Espaçamento abaixo do campo.
            )

            // Campo de texto para editar a descrição da planta.
            TextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Campo de texto para editar a categoria da planta.
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Texto indicando a seção de foto.
            Text("Foto (Opcional)", style = MaterialTheme.typography.bodyLarge)

            // Exibe a foto atual da planta, se disponível.
            if (fotoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri), // Carrega a imagem com base no URI.
                    contentDescription = "Foto da planta", // Descrição da imagem para acessibilidade.
                    modifier = Modifier
                        .size(200.dp) // Define o tamanho da imagem.
                        .padding(8.dp), // Adiciona espaçamento interno.
                    contentScale = ContentScale.Crop // Ajusta a escala da imagem para preencher o espaço.
                )
            }

            // Botão para selecionar uma nova foto.
            Button(
                onClick = { selecionarFotoLauncher.launch("image/*") }, // Abre o seletor de imagens.
                modifier = Modifier.padding(vertical = 16.dp) // Espaçamento acima e abaixo do botão.
            ) {
                Text("Selecionar Foto") // Texto exibido no botão.
            }

            // Botão para salvar as alterações feitas na planta.
            Button(
                onClick = {
                    // Verifica se os campos obrigatórios estão preenchidos.
                    if (nome.isNotEmpty() && descricao.isNotEmpty() && categoria.isNotEmpty()) {
                        // Cria uma nova planta com os valores atualizados.
                        val plantaAtualizada = planta!!.copy(
                            nome = nome, // Nome atualizado.
                            descricao = descricao, // Descrição atualizada.
                            categoria = categoria, // Categoria atualizada.
                            fotoUrl = fotoUri?.toString() // URI da foto convertido para String.
                        )

                        // Lança uma coroutine para atualizar a planta no banco de dados.
                        coroutineScope.launch {
                            plantaDao.atualizarPlanta(plantaAtualizada) // Atualiza a planta no banco.
                            navController.popBackStack() // Retorna à tela anterior após salvar.
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth() // O botão ocupa toda a largura disponível.
                    .height(56.dp) // Define a altura do botão.
            ) {
                Text("Salvar Alterações", fontSize = 18.sp) // Texto do botão.
            }
        }
    } else {
        // Exibe um indicador de carregamento enquanto a planta é buscada.
        Box(
            modifier = Modifier.fillMaxSize(), // Preenche toda a tela.
            contentAlignment = Alignment.Center // Centraliza o indicador.
        ) {
            CircularProgressIndicator() // Indicador de progresso circular.
        }
    }
}
