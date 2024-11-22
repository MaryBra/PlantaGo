package com.example.plantago.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch

class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "planta_database").build()
        val plantaDao = db.plantaDao()

        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController, plantaDao = plantaDao)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, plantaDao: PlantaDao) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(plantaDao = plantaDao, navController = navController)
        }
        composable(
            "detalhes/{plantaId}",
            arguments = listOf(navArgument("plantaId") { type = NavType.StringType }) // O tipo aqui precisa ser String ou Long dependendo do ID
        ) { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getString("plantaId")?.toLong() // Conversão para Long, se necessário
            if (plantaId != null) {
                TelaDetalhes(plantaId = plantaId)
            }
        }
    }
}

@Composable
fun MainScreen(plantaDao: PlantaDao, navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var plantasList by remember { mutableStateOf<List<Planta>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Carrega a lista de plantas ao iniciar
    LaunchedEffect(Unit) {
        plantasList = plantaDao.buscarTodas()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Campo Nome
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Espécie
        TextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text("Espécie da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Categoria
        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botão para adicionar planta
        Button(
            onClick = {
                if (nome.isNotEmpty() && especie.isNotEmpty() && categoria.isNotEmpty()) {
                    val novaPlanta = Planta(
                        nome = nome,
                        especie = especie,
                        categoria = categoria
                    )
                    coroutineScope.launch {
                        plantaDao.inserirPlanta(novaPlanta)
                        plantasList = plantaDao.buscarTodas()
                    }
                    nome = ""
                    especie = ""
                    categoria = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Planta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de plantas cadastradas
        ListagemPlantas(plantasList) { planta ->
            navController.navigate("detalhes/${planta.id}")
        }
    }
}
@Composable
fun ListagemPlantas(plantasList: List<Planta>, onPlantaClick: (Planta) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Plantas Cadastradas:",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(plantasList) { planta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPlantaClick(planta) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Nome: ${planta.nome}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Espécie: ${planta.especie}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Categoria: ${planta.categoria}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun TelaDetalhes(plantaId: Long?) {
    // Simulação de carregamento dos detalhes (substituir por lógica real)
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Detalhes da Planta", style = MaterialTheme.typography.titleLarge)
        plantaId?.let {
            Text(text = "ID da Planta: $it", style = MaterialTheme.typography.bodyLarge)
            // Adicione mais informações conforme necessário
        } ?: Text(text = "Planta não encontrada.", style = MaterialTheme.typography.bodyMedium)
    }
}
