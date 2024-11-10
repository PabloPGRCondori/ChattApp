package com.example.chatapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun ChatApp() {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<String>() }
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Envia tu primer mensaje SI COFA???",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // Campo de entrada de texto con etiqueta
        Text(
            text = "Escribir primer mensaje:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        BasicTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(40.dp)
                .background(Color.LightGray) // Color de fondo para el campo de texto
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de mensajes
        Text(
            text = "mensajes en tiempo real",
            fontSize = 26.sp, // Tamaño de la fuente
            fontWeight = FontWeight.Bold, // Peso de la fuente (negrita)
            color = Color.Black, // Color del texto
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .weight(1f) // Para llenar el espacio restante
        ) {
            messages.forEach { msg ->
                Text(text = "- $msg", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de envío
        Button(
            onClick = {
                if (message.isNotEmpty()) {
                    val messageData = hashMapOf("message" to message)
                    db.collection("messages").add(messageData)
                    message = "" // Limpiar el campo de entrada
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(200.dp)
                .height(50.dp)
        ) {
            Text("enviar", fontSize = 18.sp, color = Color.White)
        }
    }
        // Escuchar cambios en la colección "messages"
        LaunchedEffect(Unit) {
            db.collection("messages").addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    messages.clear()
                    for (document in it.documents) {
                        val msg = document.getString("message") ?: ""
                        messages.add(msg)
                    }
                }
            }
        }
    }
