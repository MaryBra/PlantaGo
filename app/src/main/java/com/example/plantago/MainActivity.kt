package com.example.plantago

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
import com.example.plantago.ui.theme.PlantaGoTheme
import com.example.plantago.view.TelaInicial
import com.example.plantago.view.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantaGoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "PlantaGo",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 32.dp)
        )


        // Botão Start Planting
        Button(
            onClick = {
                val intent = Intent(context, TelaInicial::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(0.6f)
                .height(56.dp)
        ) {
            Text(text = "Start Planting", fontSize = 18.sp)
        }

        // Imagem ilustrativa

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PlantaGoTheme {
        MainScreen()
    }
}