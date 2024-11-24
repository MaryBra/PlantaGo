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

@Composable
fun TelaEditarPlanta(plantaId: Int, plantaDao: PlantaDao, navController: NavHostController) {
    var planta by remember { mutableStateOf<Planta?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Carrega a planta para edição
    LaunchedEffect(plantaId) {
        planta = plantaDao.obterPlantaPorId(plantaId)
    }

    if (planta != null) {
        var nome by remember { mutableStateOf(planta!!.nome) }
        var descricao by remember { mutableStateOf(planta!!.descricao) }
        var categoria by remember { mutableStateOf(planta!!.categoria) }
        var fotoUri by remember { mutableStateOf(planta!!.fotoUrl?.let { Uri.parse(it) }) }

        val selecionarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                fotoUri = uri
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editar Planta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

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
                label = { Text("Descrição da Planta") },
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

            Text("Foto (Opcional)", style = MaterialTheme.typography.bodyLarge)
            if (fotoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri),
                    contentDescription = "Foto da planta",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Button(
                onClick = { selecionarFotoLauncher.launch("image/*") },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text("Selecionar Foto")
            }

            Button(
                onClick = {
                    if (nome.isNotEmpty() && descricao.isNotEmpty() && categoria.isNotEmpty()) {
                        val plantaAtualizada = planta!!.copy(
                            nome = nome,
                            descricao = descricao,
                            categoria = categoria,
                            fotoUrl = fotoUri?.toString()
                        )
                        coroutineScope.launch {
                            plantaDao.atualizarPlanta(plantaAtualizada)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Salvar Alterações", fontSize = 18.sp)
            }
        }
    } else {
        // Tela de carregamento
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
