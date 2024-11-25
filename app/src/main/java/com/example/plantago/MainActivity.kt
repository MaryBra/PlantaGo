package com.example.plantago

// Importações necessárias para a atividade principal
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.plantago.view.TelaInicial
import com.example.plantago.view.ui.theme.PlantaGoTheme

// Classe principal da aplicação, estende ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o conteúdo da atividade usando Jetpack Compose
        setContent {
            // Aplica o tema personalizado da aplicação
            PlantaGoTheme {
                // Chama a função composable MainScreen para renderizar a interface
                MainScreen()
            }
        }
    }
}

// Função composable que representa a tela inicial da aplicação
@Composable
fun MainScreen() {
    // Obtém o contexto atual para uso em intents
    val context = LocalContext.current

    // Coluna que organiza os elementos verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize() // Preenche todo o espaço disponível
            .padding(16.dp), // Adiciona padding nas bordas
        verticalArrangement = Arrangement.SpaceBetween, // Espaça os elementos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Alinha os elementos horizontalmente ao centro
    ) {
        // Título da aplicação
        Text(
            text = "PlantaGo",
            style = MaterialTheme.typography.headlineLarge, // Estilo de texto definido no tema
            color = MaterialTheme.colorScheme.primary, // Cor primária definida no esquema de cores
            modifier = Modifier.padding(top = 32.dp) // Padding no topo
        )

        // Botão "Start Planting" que inicia a atividade TelaInicial
        Button(
            onClick = {
                // Cria uma intent para navegar para TelaInicial
                val intent = Intent(context, TelaInicial::class.java)
                // Inicia a atividade TelaInicial
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Cor de fundo do botão
                contentColor = Color.White // Cor do texto do botão
            ),
            shape = MaterialTheme.shapes.medium, // Forma do botão definida no tema
            modifier = Modifier
                .padding(top = 32.dp) // Padding no topo
                .fillMaxWidth(0.6f) // O botão ocupa 60% da largura disponível
                .height(56.dp) // Altura do botão
        ) {
            // Texto dentro do botão
            Text(text = "Start Planting", fontSize = 18.sp)
        }

        // Espaço reservado para uma imagem ilustrativa (comentado no código original)
        // Você pode adicionar uma Image aqui se desejar
    }
}

// Função para visualizar a MainScreen no Android Studio Preview
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    // Aplica o tema para o preview
    PlantaGoTheme {
        // Chama a função MainScreen para renderizar no preview
        MainScreen()
    }
}
