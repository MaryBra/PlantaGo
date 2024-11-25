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

// Classe principal da aplicação, estendendo ComponentActivity para usar Jetpack Compose
class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialização do banco de dados Room com a instância do AppDatabase
        val db = Room.databaseBuilder(
            applicationContext, // Contexto da aplicação
            AppDatabase::class.java, // Classe que define o banco de dados
            "planta_database" // Nome do arquivo do banco de dados
        ).build()

        // Criação dos DAOs para acessar as tabelas do banco de dados
        val plantaDao = db.plantaDao() // DAO para plantas
        val historicoDao = db.historicoDao() // DAO para históricos de rega

        // Define o conteúdo da tela usando Jetpack Compose
        setContent {
            PlantaGoTheme { // Aplica o tema personalizado da aplicação
                val navController = rememberNavController() // Controlador de navegação
                AppNavHost(
                    navController = navController, // Passa o controlador de navegação
                    plantaDao = plantaDao, // Passa o DAO de plantas
                    historicoDao = historicoDao // Passa o DAO de históricos
                )
            }
        }
    }
}

// Função composable que configura as rotas de navegação da aplicação
@Composable
fun AppNavHost(navController: NavHostController, plantaDao: PlantaDao, historicoDao: HistoricoDao) {
    // Define o NavHost, que gerencia as telas e as rotas
    NavHost(navController = navController, startDestination = "main") { // Define "main" como rota inicial
        // Rota para a tela principal
        composable("main") {
            MainScreen(plantaDao = plantaDao, navController = navController) // Tela principal
        }
        // Rota para exibir os detalhes de uma planta
        composable(
            route = "detalhes/{plantaId}", // Rota com parâmetro
            arguments = listOf(navArgument("plantaId") { type = NavType.IntType }) // Define o tipo do parâmetro
        ) { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getInt("plantaId") // Obtém o ID da planta da navegação
            if (plantaId != null) {
                TelaDetalhes(
                    plantaId = plantaId, // Passa o ID da planta
                    plantaDao = plantaDao, // Passa o DAO de plantas
                    historicoDao = historicoDao, // Passa o DAO de históricos
                    navController = navController // Passa o controlador de navegação
                )
            } else {
                // Mostra uma mensagem de erro caso o ID seja nulo
                Box(
                    modifier = Modifier.fillMaxSize(), // Preenche a tela
                    contentAlignment = Alignment.Center // Centraliza o conteúdo
                ) {
                    Text(
                        text = "Erro ao carregar os detalhes da planta.", // Mensagem de erro
                        color = MaterialTheme.colorScheme.error, // Cor de erro definida no tema
                        style = MaterialTheme.typography.bodyLarge, // Estilo de texto
                        modifier = Modifier.padding(16.dp) // Adiciona espaçamento ao redor do texto
                    )
                }
            }
        }
        // Rota para a tela de cadastro de uma nova planta
        composable("cadastro") {
            TelaCadastro(plantaDao = plantaDao, navController = navController)
        }
        // Rota para editar uma planta existente
        composable(
            route = "editar/{plantaId}", // Rota com parâmetro
            arguments = listOf(navArgument("plantaId") { type = NavType.IntType }) // Define o tipo do parâmetro
        ) { backStackEntry ->
            val plantaId = backStackEntry.arguments?.getInt("plantaId") // Obtém o ID da planta da navegação
            if (plantaId != null) {
                TelaEditarPlanta(
                    plantaId = plantaId, // Passa o ID da planta
                    plantaDao = plantaDao, // Passa o DAO de plantas
                    navController = navController // Passa o controlador de navegação
                )
            }
        }
    }
}

// Função composable que exibe os detalhes de uma planta específica
@Composable
fun TelaDetalhes(
    plantaId: Int, // ID da planta a ser exibida
    plantaDao: PlantaDao, // DAO para acesso aos dados da planta
    historicoDao: HistoricoDao, // DAO para acesso ao histórico de regas
    navController: NavHostController // Controlador de navegação
) {
    // Variável de estado para armazenar a planta
    var planta by remember { mutableStateOf<Planta?>(null) }
    // Variável de estado para armazenar o histórico de regas
    var historicoList by remember { mutableStateOf<List<Historico>>(emptyList()) }
    // Escopo de coroutine para operações assíncronas
    val coroutineScope = rememberCoroutineScope()

    // Efeito lançado quando a tela é carregada
    LaunchedEffect(plantaId) {
        planta = plantaDao.obterPlantaPorId(plantaId) // Busca a planta pelo ID
        historicoList = historicoDao.obterHistoricosPorPlanta(plantaId) // Busca o histórico de regas
    }

    if (planta != null) {
        // Layout principal para exibir os detalhes
        Box(
            modifier = Modifier
                .fillMaxSize() // Preenche toda a tela
                .padding(16.dp) // Adiciona espaçamento interno
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize() // Preenche toda a altura
                    .verticalScroll(rememberScrollState()) // Permite rolar o conteúdo verticalmente
                    .padding(bottom = 100.dp), // Adiciona espaço para os botões inferiores
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaça os elementos
            ) {
                // Título da planta
                Text(
                    text = "🌱 ${planta!!.nome}", // Nome da planta com um emoji
                    style = MaterialTheme.typography.headlineMedium, // Estilo do texto
                    fontWeight = FontWeight.Bold, // Peso da fonte
                    color = MaterialTheme.colorScheme.primary // Cor definida no tema
                )
                // Linha divisória
                Divider(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), // Cor com transparência
                    thickness = 1.dp // Espessura da linha
                )

                // Exibe a foto da planta, se disponível
                if (planta!!.fotoUrl != null) {
                    AsyncImage(
                        model = planta!!.fotoUrl, // URL da imagem
                        contentDescription = "Foto da planta", // Descrição para acessibilidade
                        modifier = Modifier
                            .size(200.dp) // Tamanho da imagem
                            .clip(RoundedCornerShape(16.dp)) // Arredonda os cantos da imagem
                            .border(
                                width = 2.dp, // Largura da borda
                                color = MaterialTheme.colorScheme.primary, // Cor da borda
                                shape = RoundedCornerShape(16.dp) // Formato da borda
                            ),
                        contentScale = ContentScale.Crop // Ajusta a imagem para preencher o espaço
                    )
                }

                // Exibe a descrição da planta
                Text(
                    text = planta!!.descricao, // Descrição da planta
                    style = MaterialTheme.typography.bodyMedium, // Estilo do texto
                    color = MaterialTheme.colorScheme.onBackground // Cor do texto
                )
                // Exibe a categoria da planta
                Text(
                    text = "Categoria: ${planta!!.categoria}", // Categoria da planta
                    style = MaterialTheme.typography.bodySmall, // Estilo do texto
                    color = MaterialTheme.colorScheme.secondary // Cor do texto
                )

                // Exibe o histórico de regas
                Text(
                    text = "💧 Histórico de Regas", // Título da seção
                    style = MaterialTheme.typography.titleMedium, // Estilo do texto
                    fontWeight = FontWeight.Bold, // Peso da fonte
                    color = MaterialTheme.colorScheme.primary // Cor do texto
                )

                if (historicoList.isEmpty()) {
                    // Mensagem se não houver histórico de regas
                    Text(
                        text = "Nenhuma rega registrada.", // Mensagem padrão
                        style = MaterialTheme.typography.bodyMedium, // Estilo do texto
                        color = MaterialTheme.colorScheme.onBackground // Cor do texto
                    )
                } else {
                    // Itera pelo histórico e exibe cada registro
                    historicoList.forEach { historico ->
                        val formattedDate = historico.dataHora.substring(0, 16).replace("T", " ") // Formata a data
                        Text(
                            text = "🗓️ $formattedDate", // Exibe a data formatada
                            style = MaterialTheme.typography.bodySmall, // Estilo do texto
                            color = MaterialTheme.colorScheme.onBackground // Cor do texto
                        )
                    }
                }
            }

            // Botões flutuantes para ações (editar, regar e excluir)
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Preenche a largura total
                    .align(Alignment.BottomCenter) // Posiciona na parte inferior central
                    .padding(16.dp), // Adiciona espaçamento interno
                horizontalArrangement = Arrangement.SpaceEvenly, // Espaça os botões uniformemente
                verticalAlignment = Alignment.CenterVertically // Alinha os botões verticalmente
            ) {
                // Botão para editar a planta
                FloatingActionButton(
                    onClick = { navController.navigate("editar/$plantaId") }, // Navega para a tela de edição
                    containerColor = MaterialTheme.colorScheme.primary, // Cor do botão
                    contentColor = MaterialTheme.colorScheme.onPrimary // Cor do ícone
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, // Ícone de edição
                        contentDescription = "Editar Planta" // Descrição para acessibilidade
                    )
                }

                // Botão para registrar uma rega
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            val now = LocalDateTime.now().toString() // Obtém a data e hora atual
                            val historico = Historico(
                                plantaId = plantaId, // ID da planta
                                dataHora = now, // Data e hora da rega
                                rega = true // Marca como regada
                            )
                            historicoDao.inserirHistorico(historico) // Insere o histórico no banco de dados
                            historicoList = historicoDao.obterHistoricosPorPlanta(plantaId) // Atualiza a lista
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondary, // Cor do botão
                    contentColor = MaterialTheme.colorScheme.onSecondary // Cor do ícone
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop, // Ícone de rega
                        contentDescription = "Registrar Rega" // Descrição para acessibilidade
                    )
                }

                // Botão para excluir a planta
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            plantaDao.deletarPlantaPorId(plantaId) // Deleta a planta do banco de dados
                            navController.popBackStack() // Retorna à tela anterior
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.error, // Cor do botão
                    contentColor = MaterialTheme.colorScheme.onError // Cor do ícone
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Ícone de exclusão
                        contentDescription = "Excluir Planta" // Descrição para acessibilidade
                    )
                }
            }
        }
    } else {
        // Tela de carregamento enquanto a planta não é carregada
        Box(
            modifier = Modifier
                .fillMaxSize() // Preenche toda a tela
                .background(MaterialTheme.colorScheme.background), // Define o fundo
            contentAlignment = Alignment.Center // Centraliza o conteúdo
        ) {
            CircularProgressIndicator( // Indicador de carregamento
                color = MaterialTheme.colorScheme.primary // Cor do indicador
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