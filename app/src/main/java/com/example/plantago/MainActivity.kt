package com.example.plantago

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.plantago.ui.theme.PlantaGoTheme
import com.example.plantago.view.TelaInicial

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaoTrocaDeTela()
        }
    }
}

@Composable
fun BotaoTrocaDeTela() {
    val context = LocalContext.current

    Button(onClick = {
        // Intent para abrir a SegundaActivity
        val intent = Intent(context, TelaInicial::class.java)
        context.startActivity(intent)
    }) {
        Text("Abrir Segunda Activity")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlantaGoTheme {
        Greeting("Android")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}