package com.example.proyecto_personal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Conversor() }
    }
}
@Composable
fun Conversor() {
    var Monto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    val investigador = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("USD → EUR",
            fontSize = 20.sp)

        Spacer(Modifier.height(16.dp))
        TextField(
            value = Monto,
            onValueChange = { Monto = it },
            label = { Text("Monto") })

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val dinero = Monto.toDoubleOrNull() ?: 0.0
            if (dinero > 0) {
                resultado = "Convirtiendo..."
                investigador.launch(Dispatchers.IO) {
                    try {
                        val json = URL("https://api.frankfurter.app/latest?from=USD&to=EUR")
                            .readText()
                        val diferencia = JSONObject(json).getJSONObject("rates").getDouble("EUR")
                        resultado = "%.2f USD = %.2f EUR".format(dinero, dinero * diferencia)
                    } catch (e: Exception) {
                        resultado = "Error al conectar "
                    }
                }
            } else {
                resultado = "Monto inválido"
            }
        }) {
            Text("Convertir")
        }

        Spacer(Modifier.height(24.dp))
        Text(resultado, fontSize = 20.sp)
    }
}
