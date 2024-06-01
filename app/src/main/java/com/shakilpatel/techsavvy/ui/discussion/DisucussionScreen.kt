package com.shakilpatel.techsavvy.ui.discussion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.nav.ChatAppBar
import com.shakilpatel.techsavvy.utils.vibrate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionScreen(viewModel: DiscussionViewModel,navController: NavController) {
    Box(
        modifier= Modifier.fillMaxSize()
    ) {
        var msg by remember{ mutableStateOf("") }

        Column(modifier= Modifier.fillMaxSize()) {
            ChatAppBar(title = "Group Discussion")
        }


        Row(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(Transparent)
            ) {

                val context = LocalContext.current
                val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                recognizerIntent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                val speechTextState = remember { mutableStateOf(TextFieldValue()) }

                val speechRecognitionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted: Boolean ->
                        if (isGranted) {
                            speechRecognizer.startListening(recognizerIntent)
                        }
                    }
                )
                TextField(
                    value = msg, onValueChange = {
                        msg = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusable(true),
                    maxLines = 3,
                    placeholder = { Text("Enter Message") },
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedVisibility(visible = msg.isEmpty()) {
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        Icons.Default.AddCircle,
                                        "",
                                        modifier = Modifier.clickable {
                                            if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                                speechRecognizer.startListening(recognizerIntent)
                                            } else {
                                                speechRecognitionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            }
                                        }
                                    )
                                }
                            }
                            val view = LocalView.current
                            IconButton(
                                onClick = {
//                                    if (msg.isNotEmpty()) {
//                                        viewModel.sendMessage(msg)
//                                        msg = ""
//                                    }
                                    view.vibrate()
                                },
                                enabled = msg.isNotEmpty()
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    "",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }

                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        focusedContainerColor = Color(0x6F454646),
                        unfocusedContainerColor = Color(0x6F454646),
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                speechRecognizer.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}
                    override fun onError(error: Int) {}
                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            speechTextState.value = TextFieldValue(matches[0])
                            msg = speechTextState.value.text
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            speechTextState.value = TextFieldValue(matches[0])
                            msg = speechTextState.value.text
                        }
                    }
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        }

    }
}