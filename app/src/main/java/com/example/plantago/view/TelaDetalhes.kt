package com.example.plantago.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta
import com.example.plantago.ui.theme.PlantaGoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TelaDetalhes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val plantaId = intent.getLongExtra("plantaId", -1L) // Recupera o ID enviado
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "planta_database").build()
        val plantaDao = db.plantaDao()

        setContent {
            PlantaGoTheme {
                TelaDetalhesContent(
                    plantaId = plantaId,
                    plantaDao = plantaDao,
                    onEditarPlanta = { planta ->
                        val intent = Intent(this, TelaEditar::class.java)
                        intent.putExtra("plantaId", planta.id)
                        startActivity(intent) // Navega para a tela de edição
                    },
                    onExcluirPlanta = { planta ->
                        CoroutineScope(Dispatchers.IO).launch {
                            plantaDao.deletar(planta) // Exclui planta no banco
                            finish() // Fecha a tela após excluir
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TelaDetalhesContent(
    plantaId: Long,
    plantaDao: PlantaDao,
    onEditarPlanta: (Planta) -> Unit,
    onExcluirPlanta: (Planta) -> Unit
) {
    var planta by remember { mutableStateOf<Planta?>(null) }

    // Carregar planta com o ID recebido
    LaunchedEffect(plantaId) {
        planta = plantaDao.obterPlantaPorId(plantaId) // Função para buscar uma planta pelo ID
    }

    if (planta == null) {
        Text(
            text = "Erro ao carregar os detalhes da planta.",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Exibição de detalhes da planta
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Detalhes da Planta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nome: ${planta?.nome}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Espécie: ${planta?.especie}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Categoria: ${planta?.categoria}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Botões de ações
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onEditarPlanta(planta!!) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text("Editar")
                }
                Button(
                    onClick = { onExcluirPlanta(planta!!) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}
