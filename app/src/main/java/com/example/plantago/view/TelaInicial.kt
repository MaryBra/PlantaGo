package com.example.plantago.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta
import com.example.plantago.view.ui.theme.PlantaGoTheme

class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "planta_database").build()
        val plantaDao = db.plantaDao()

        setContent {
            PlantaGoTheme { // Envolva o conteúdo no tema
                val navController = rememberNavController()
                AppNavHost(navController = navController, plantaDao = plantaDao)
            }
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
            arguments = listOf(navArgument("plantaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getInt("plantaId")
            if (plantaId != null) {
                TelaDetalhes(plantaId = plantaId, plantaDao = plantaDao)
            } else {
                // Caso de falha no argumento
                Text(
                    text = "Erro ao carregar os detalhes da planta.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        composable("cadastro") {
            TelaCadastro(plantaDao = plantaDao, navController = navController)
        }
    }
}


@Composable
fun TelaDetalhes(plantaId: Int, plantaDao: PlantaDao) {
    var planta by remember { mutableStateOf<Planta?>(null) }

    LaunchedEffect(plantaId) {
        planta = plantaDao.obterPlantaPorId(plantaId)
    }

    if (planta != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🌱 Detalhes da Planta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Divider(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            planta?.let {
                if (it.fotoUrl != null) {
                    AsyncImage(
                        model = it.fotoUrl,
                        contentDescription = "Foto da planta",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Nome: ${it.nome}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Espécie: ${it.especie}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Categoria: ${it.categoria}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {
        // Tela de carregamento amigável
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Carregando informações da planta...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}


@Composable
fun MainScreen(plantaDao: PlantaDao, navController: NavHostController) {
    var plantasList by remember { mutableStateOf<List<Planta>>(emptyList()) }

    // Carrega a lista de plantas ao iniciar
    LaunchedEffect(Unit) {
        plantasList = plantaDao.buscarTodas()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título centralizado
        Column {
            Text(
                text = "Minhas Plantas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            if (plantasList.isEmpty()) {
                Text(
                    text = "Nenhuma planta cadastrada!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                ListagemPlantas(plantasList) { planta ->
                    navController.navigate("detalhes/${planta.id}")
                }
            }
        }

        // Botão flutuante no canto inferior direito
        FloatingActionButton(
            onClick = { navController.navigate("cadastro") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
            PlantaCard(planta = planta, onClick = { onPlantaClick(planta) })
        }
    }
}

@Composable
fun PlantaCard(planta: Planta, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        if (isPressed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                },
                onClickLabel = "Ver detalhes da planta"
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = planta.nome,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Espécie: ${planta.especie}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Categoria: ${planta.categoria}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}