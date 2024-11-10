package com.example.chatapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        // Mostrar mensajes
        messages.forEach { msg ->
            Text(text = msg, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada de texto
        BasicTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para enviar mensaje
        Button(onClick = {
            if (message.isNotEmpty()) {
                // Agregar el mensaje a Firestore
                val messageData = hashMapOf("message" to message)
                db.collection("messages").add(messageData)
                message = "" // Limpiar el campo de entrada
            }
        }) {
            Text("Enviar")
        }
    }

    // Escuchar cambios en la colección de mensajes en Firestore
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
