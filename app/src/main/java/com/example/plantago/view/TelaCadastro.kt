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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastro(plantaDao: PlantaDao, navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Launchers para capturar ou selecionar imagens
    val tirarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Converta o bitmap para Uri se necessário
            fotoUri = Uri.EMPTY // Ajuste com sua lógica para salvar e obter Uri
        }
    }
    val selecionarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            fotoUri = uri
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .wrapContentHeight()
        ) {
            // Título da tela
            Text(
                text = "Cadastrar Planta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campos de texto
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            TextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Detalhes da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria da Planta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Seção para adicionar foto
            Text(
                text = "Adicionar Foto (Opcional)",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            if (fotoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri),
                    contentDescription = "Foto da planta",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "Nenhuma foto selecionada",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Button(onClick = { tirarFotoLauncher.launch(null) }) {
                    Text("Tirar Foto")
                }
                Button(onClick = { selecionarFotoLauncher.launch("image/*") }) {
                    Text("Importar da Galeria")
                }
            }

            // Botão para salvar a planta
            Button(
                onClick = {
                    if (nome.isNotEmpty() && descricao.isNotEmpty() && categoria.isNotEmpty()) {
                        val novaPlanta = Planta(
                            nome = nome,
                            descricao = descricao,
                            categoria = categoria,
                            fotoUrl = fotoUri?.toString() // Salva a URI como String
                        )
                        coroutineScope.launch {
                            plantaDao.inserirPlanta(novaPlanta)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Salvar Planta", fontSize = 18.sp)
            }
        }
    }
}
