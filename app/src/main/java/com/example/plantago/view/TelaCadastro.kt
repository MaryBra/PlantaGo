package com.example.plantago.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plantago.dao.PlantaDao
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch

@Composable
fun TelaCadastro(plantaDao: PlantaDao, navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text("Espécie da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isNotEmpty() && especie.isNotEmpty() && categoria.isNotEmpty()) {
                    val novaPlanta = Planta(nome = nome, especie = especie, categoria = categoria)
                    coroutineScope.launch {
                        plantaDao.inserirPlanta(novaPlanta)
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Planta")
        }
    }
}
