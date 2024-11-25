package com.example.plantago.view

// Importações necessárias para o funcionamento do Composable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.plantago.dao.PlantaDao
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch

// Classe para criar a tela de cadastro de uma planta.
// Utiliza o Jetpack Compose para construir a interface de forma declarativa.
@OptIn(ExperimentalMaterial3Api::class) // Permite o uso de APIs experimentais do Material3
@Composable
fun TelaCadastro(plantaDao: PlantaDao, navController: NavHostController) {
    // Variáveis de estado para armazenar os valores dos campos do formulário.
    var nome by remember { mutableStateOf("") } // Estado para o campo "Nome da Planta".
    var descricao by remember { mutableStateOf("") } // Estado para o campo "Descrição da Planta".
    var categoria by remember { mutableStateOf("") } // Estado para o campo "Categoria da Planta".
    var fotoUri by remember { mutableStateOf<Uri?>(null) } // Estado para armazenar o URI da foto selecionada.

    // Escopo de coroutine que será usado para realizar operações assíncronas.
    val coroutineScope = rememberCoroutineScope()

    // Lançador para capturar uma foto diretamente da câmera.
    val tirarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Aqui, o bitmap deveria ser salvo para gerar um URI correspondente.
            // O código atual define `Uri.EMPTY`, o que pode não funcionar corretamente.
            fotoUri = Uri.EMPTY
        }
    }

    // Lançador para selecionar uma foto da galeria.
    val selecionarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            // Atualiza o estado com o URI da foto selecionada.
            fotoUri = uri
        }
    }

    // Contêiner principal que envolve toda a tela.
    Box(
        modifier = Modifier
            .fillMaxSize() // Faz o contêiner ocupar toda a tela.
            .padding(16.dp), // Adiciona espaçamento em volta do conteúdo.
        contentAlignment = Alignment.Center // Centraliza o conteúdo dentro do Box.
    ) {
        // Coluna que organiza os elementos verticalmente.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centraliza os elementos horizontalmente.
            verticalArrangement = Arrangement.Center, // Centraliza os elementos verticalmente.
            modifier = Modifier
                .fillMaxWidth() // A coluna ocupa toda a largura disponível.
                .padding(24.dp) // Adiciona espaçamento interno.
                .wrapContentHeight() // Ajusta a altura para envolver o conteúdo.
        ) {
            // Título da tela.
            Text(
                text = "Cadastrar Planta", // Texto exibido como título.
                style = MaterialTheme.typography.headlineMedium, // Estilo de texto definido pelo tema.
                fontWeight = FontWeight.Bold, // Define o peso da fonte.
                color = MaterialTheme.colorScheme.primary, // Cor do texto com base no tema.
                modifier = Modifier.padding(bottom = 24.dp) // Adiciona espaçamento abaixo do título.
            )

            // Campo de texto para inserir o nome da planta.
            TextField(
                value = nome, // Valor atual do campo.
                onValueChange = { nome = it }, // Atualiza o estado ao alterar o texto.
                label = { Text("Nome da Planta") }, // Rótulo do campo.
                modifier = Modifier
                    .fillMaxWidth() // O campo ocupa toda a largura disponível.
                    .padding(bottom = 16.dp) // Adiciona espaçamento abaixo do campo.
            )

            // Campo de texto para inserir a descrição da planta.
            TextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Campo de texto para inserir a categoria da planta.
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Texto informativo sobre a adição de fotos.
            Text(
                text = "Adicionar Foto (Opcional)", // Texto descritivo.
                style = MaterialTheme.typography.bodyLarge, // Estilo do texto com base no tema.
                color = MaterialTheme.colorScheme.primary, // Cor do texto definida pelo tema.
                modifier = Modifier.padding(bottom = 8.dp), // Espaçamento abaixo do texto.
                textAlign = TextAlign.Center // Alinha o texto ao centro.
            )

            // Exibe a imagem selecionada, se houver.
            if (fotoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri), // Carrega a imagem a partir do URI.
                    contentDescription = "Foto da planta", // Descrição da imagem para acessibilidade.
                    modifier = Modifier
                        .size(200.dp) // Define o tamanho da imagem.
                        .padding(8.dp), // Adiciona espaçamento em volta da imagem.
                    contentScale = ContentScale.Crop // Ajusta a imagem para preencher o espaço definido.
                )
            } else {
                // Exibe um texto informando que nenhuma foto foi selecionada.
                Text(
                    text = "Nenhuma foto selecionada", // Texto exibido.
                    style = MaterialTheme.typography.bodySmall, // Estilo do texto.
                    color = Color.Gray // Cor do texto.
                )
            }

            // Linha contendo os botões para tirar foto ou importar da galeria.
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, // Distribui os botões uniformemente.
                modifier = Modifier
                    .fillMaxWidth() // A linha ocupa toda a largura disponível.
                    .padding(vertical = 16.dp) // Adiciona espaçamento acima e abaixo da linha.
            ) {
                // Botão para tirar foto.
                Button(onClick = { tirarFotoLauncher.launch(null) }) {
                    Text("Tirar Foto") // Texto exibido no botão.
                }

                // Botão para selecionar foto da galeria.
                Button(onClick = { selecionarFotoLauncher.launch("image/*") }) {
                    Text("Importar da Galeria") // Texto exibido no botão.
                }
            }

            // Botão para salvar a planta.
            Button(
                onClick = {
                    // Verifica se os campos obrigatórios estão preenchidos.
                    if (nome.isNotEmpty() && descricao.isNotEmpty() && categoria.isNotEmpty()) {
                        // Cria um novo objeto Planta com os dados fornecidos.
                        val novaPlanta = Planta(
                            nome = nome, // Nome da planta.
                            descricao = descricao, // Descrição da planta.
                            categoria = categoria, // Categoria da planta.
                            fotoUrl = fotoUri?.toString() // Converte o URI da foto para String.
                        )

                        // Lança uma coroutine para inserir a planta no banco de dados.
                        coroutineScope.launch {
                            plantaDao.inserirPlanta(novaPlanta) // Insere a planta no banco.
                            navController.popBackStack() // Volta para a tela anterior.
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth() // O botão ocupa toda a largura disponível.
                    .height(56.dp) // Define a altura do botão.
            ) {
                // Texto exibido no botão.
                Text("Salvar Planta", fontSize = 18.sp)
            }
        }
    }
}
