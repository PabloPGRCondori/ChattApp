import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.chatapp.R 


@Composable
fun ChatApp() {
    var message by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<String>() }
    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondoparaandroid),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Contenedor para los elementos sobre la imagen de fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = "mensajes en tiempo real",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0x80FFFFFF))
                    .padding(8.dp)
            )

            // Campo de entrada de texto con etiqueta
            Text(
                text = "Escribir primer mensaje:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier
                    .background(Color(0x80FFFFFF))
            )
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de mensajes
            Text(
                text = "Mensajes enviados recientemente:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier
                    .background(Color(0x80FFFFFF))
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .weight(1f)
            ) {
                messages.forEach { msg ->
                    Text(
                        text = "- $msg",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .background(Color(0x80FFFFFF))
                            .padding(8.dp)
                    )
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
