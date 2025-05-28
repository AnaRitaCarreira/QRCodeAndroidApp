package com.example.qrcodeapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.util.Patterns
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

import android.os.Handler
import android.os.Looper

@Composable
fun QRResultScreen(result: String, onBack: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Resultado do QR Code:", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (isUrl(result)) {
            Toast.makeText(context, "Abrindo link: $result", Toast.LENGTH_SHORT).show()
            Text(
                text = result,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    try {
                        val uri = Uri.parse(result)
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                            // Precaução extra: diz explicitamente que é para abrir como URL
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }

                        // Tenta abrir o link com um navegador
                        val browserPackage = intent.resolveActivity(context.packageManager)
                        if (browserPackage != null) {
                            context.startActivity(intent)
                        } else {
                            // Alternativa: tenta abrir com um chooser
                            val chooser = Intent.createChooser(intent, "Escolha um navegador")
                            context.startActivity(chooser)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro ao tentar abrir o link", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            )
        } else {
            Text(result, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = { copyToClipboard(context, result) }) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copiar")
            }

            IconButton(onClick = { shareText(context, result) }) {
                Icon(Icons.Default.Share, contentDescription = "Compartilhar")
            }

            IconButton(onClick = {
                enviarQRCodeParaPC(context, result)
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Enviar para o PC")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onBack) {
            Text("Voltar")
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = ContextCompat.getSystemService(context, android.content.ClipboardManager::class.java)
    val clip = android.content.ClipData.newPlainText("QR Code", text)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(context, "Copiado para a área de transferência", Toast.LENGTH_SHORT).show()
}

private fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, "Compartilhar QR Code")
    context.startActivity(chooser)
}

private fun isUrl(text: String): Boolean {
    // Usa o padrão oficial para verificar se é URL válida
    return Patterns.WEB_URL.matcher(text).matches()
}




fun enviarQRCodeParaPC(context: Context, conteudo: String) {
    val client = OkHttpClient()

    val json = """{ "content": "$conteudo" }"""
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://173.249.46.139/qr") // Troca pelo IP correto
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Falha ao enviar QR para o PC", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            Handler(Looper.getMainLooper()).post {
                if (response.isSuccessful) {
                    Toast.makeText(context, "QR enviado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erro na resposta do servidor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}

