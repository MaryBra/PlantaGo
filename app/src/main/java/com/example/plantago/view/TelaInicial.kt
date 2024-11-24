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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.plantago.dao.HistoricoDao
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Historico
import com.example.plantago.model.Planta
import com.example.plantago.view.ui.theme.PlantaGoTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialização do banco de dados
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "planta_database"
        ).build()

        // DAOs
        val plantaDao = db.plantaDao()
        val historicoDao = db.historicoDao() // Adiciona o HistoricoDao

        setContent {
            PlantaGoTheme { // Envolva o conteúdo no tema
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    plantaDao = plantaDao,
                    historicoDao = historicoDao // Passe o HistoricoDao para o AppNavHost
                )
            }
        }
    }
}


@Composable
fun AppNavHost(navController: NavHostController, plantaDao: PlantaDao, historicoDao: HistoricoDao) {
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
                TelaDetalhes(
                    plantaId = plantaId,
                    plantaDao = plantaDao,
                    historicoDao = historicoDao, // Passe o historicoDao aqui
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Erro ao carregar os detalhes da planta.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        composable("cadastro") {
            TelaCadastro(plantaDao = plantaDao, navController = navController)
        }
        composable(
            route = "editar/{plantaId}",
            arguments = listOf(navArgument("plantaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getInt("plantaId")
            if (plantaId != null) {
                TelaEditarPlanta(
                    plantaId = plantaId,
                    plantaDao = plantaDao,
                    navController = navController
                )
            }
        }
    }
}




@Composable
fun TelaDetalhes(
    plantaId: Int,
    plantaDao: PlantaDao,
    historicoDao: HistoricoDao,
    navController: NavHostController
) {
    var planta by remember { mutableStateOf<Planta?>(null) }
    var historicoList by remember { mutableStateOf<List<Historico>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Carrega a planta e o histórico ao iniciar
    LaunchedEffect(plantaId) {
        planta = plantaDao.obterPlantaPorId(plantaId)
        historicoList = historicoDao.obterHistoricosPorPlanta(plantaId)
    }

    if (planta != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp), // Espaço para os botões inferiores
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "🌱 ${planta!!.nome}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Divider(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    thickness = 1.dp
                )

                // Foto da planta
                if (planta!!.fotoUrl != null) {
                    AsyncImage(
                        model = planta!!.fotoUrl,
                        contentDescription = "Foto da planta",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentScale = ContentScale.Crop
                    )
                }

                // Detalhes da planta
                Text(
                    text = planta!!.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Categoria: ${planta!!.categoria}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                // Histórico de regas
                Text(
                    text = "💧 Histórico de Regas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (historicoList.isEmpty()) {
                    Text(
                        text = "Nenhuma rega registrada.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    historicoList.forEach { historico ->
                        val formattedDate = historico.dataHora.substring(0, 16).replace("T", " ")
                        Text(
                            text = "🗓️ $formattedDate",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // Botões flutuantes na parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão de editar
                FloatingActionButton(
                    onClick = { navController.navigate("editar/$plantaId") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Planta"
                    )
                }

                // Botão de rega
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            val now = LocalDateTime.now().toString()
                            val historico = Historico(
                                plantaId = plantaId,
                                dataHora = now,
                                rega = true
                            )
                            historicoDao.inserirHistorico(historico)
                            historicoList = historicoDao.obterHistoricosPorPlanta(plantaId)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Registrar Rega"
                    )
                }

                // Botão de excluir
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            plantaDao.deletarPlantaPorId(plantaId)
                            navController.popBackStack()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir Planta"
                    )
                }
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
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
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
                text = "Detalhes: ${planta.descricao}",
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