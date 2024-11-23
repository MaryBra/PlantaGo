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
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta

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
            route = "detalhes/{plantaId}",
            arguments = listOf(navArgument("plantaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getLong("plantaId")
            if (plantaId != null) {
                TelaDetalhes(plantaId = plantaId)
            } else {
                // Caso de falha no argumento
                Text("Erro ao carregar os detalhes da planta.")
            }
        }
        composable("cadastro") {
            TelaCadastro(plantaDao = plantaDao, navController = navController)
        }
    }
}

@Composable
fun TelaDetalhes(plantaId: Long) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Detalhes da Planta", style = MaterialTheme.typography.titleLarge)
        Text(text = "ID da Planta: $plantaId", style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
fun MainScreen(plantaDao: PlantaDao, navController: NavHostController) {
    var plantasList by remember { mutableStateOf<List<Planta>>(emptyList()) }

    // Carrega a lista de plantas ao iniciar
    LaunchedEffect(Unit) {
        plantasList = plantaDao.buscarTodas()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { navController.navigate("cadastro") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar Nova Planta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ListagemPlantas(plantasList) { planta ->
            navController.navigate("detalhes/${planta.id}")
        }
    }
}

@Composable
fun ListagemPlantas(plantasList: List<Planta>, onPlantaClick: (Planta) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(plantasList) { planta ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlantaClick(planta) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nome: ${planta.nome}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Espécie: ${planta.especie}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Categoria: ${planta.categoria}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
