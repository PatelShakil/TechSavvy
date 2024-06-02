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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.data.models.MessageGDModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.nav.ChatAppBar
import com.shakilpatel.techsavvy.utils.shimmerEffect
import com.shakilpatel.techsavvy.utils.timeAgo
import com.shakilpatel.techsavvy.utils.vibrate
import kotlin.random.Random

@Composable
fun DiscussionScreen(viewModel: DiscussionViewModel, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        var msg by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            ChatAppBar(
                modifier = Modifier,
                title = "Group Discussion",
                info = "This is group discussion section where you can discuss about any topic you want with all your classmates."
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                viewModel.gdMsg.collectAsState().value.let {
                    when (it) {
                        is Resource.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                repeat(25) {
                                    ChatLoad(Random.nextBoolean())
                                }

                            }
                        }

                        is Resource.Failure -> {}
                        is Resource.Success -> {

                            val scrollState =
                                rememberLazyListState(initialFirstVisibleItemIndex = it.result.size)
                            LaunchedEffect(key1 = it.result.size) {
                                if (it.result.isNotEmpty()) {
                                    scrollState.animateScrollToItem(it.result.size - 1)
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .focusable(true),
                                state = scrollState
                            ) {
                                items(it.result.sortedBy { it.timestamp }) {
                                    MessageGDItem(
                                        message = it,
                                        viewModel.getStudentByKey("phone")!!
                                    )
                                }
                                item {
                                    Spacer(modifier = Modifier.height(500.dp))
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .padding(bottom = 10.dp)
                .imePadding()
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
                                        painterResource(R.drawable.ic_mic),
                                        "",
                                        modifier = Modifier.clickable {
                                            if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                                speechRecognizer.startListening(recognizerIntent)
                                            } else {
                                                speechRecognitionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            }
                                        },
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                            val view = LocalView.current
                            IconButton(
                                onClick = {
                                    if (msg.isNotEmpty()) {
                                        viewModel.sendMessage(msg)
                                        msg = ""
                                    }
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
                        val matches =
                            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            speechTextState.value = TextFieldValue(matches[0])
                            msg = speechTextState.value.text
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches =
                            partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
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

@Composable
fun MessageGDItem(message: MessageGDModel, phone: String) {
    val isMe = message.sender == phone
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (
            !isMe
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(32.dp),
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message.user?.name?.first()?.uppercase().toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 0.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
        Card(
            modifier = Modifier
                .widthIn(max = 200.dp)
                .padding(bottom = if (isMe) 0.dp else 5.dp),
            shape = RoundedCornerShape(
                topStart = 15.dp,
                topEnd = 15.dp,
                bottomStart = if (isMe) 15.dp else 0.dp,
                bottomEnd = if (isMe) 0.dp else 15.dp
            ),
            colors = CardDefaults.cardColors(
                if (isMe) MaterialTheme.colorScheme.primary else Color(
                    0x6F454646
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .padding(5.dp)
            ) {
                Text(
                    text = message.message,
                    color = if (isMe) Black else White,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = timeAgo(message.timestamp, "hh:mma dd MMM yy"),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }

}


@Composable
fun ChatLoad(isSend: Boolean = true) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        val width = Random.nextInt(from = 80, until = 300).dp
        if (isSend) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(width)
                    .height(32.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 15.dp,
                            bottomEnd = 0.dp,
                            topStart = 15.dp,
                            bottomStart = 15.dp
                        )
                    )
                    .shimmerEffect()

            )
        } else {

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(width)
                    .height(32.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 15.dp,
                            bottomEnd = 15.dp,
                            topStart = 0.dp,
                            bottomStart = 15.dp
                        )
                    )
                    .shimmerEffect()

            )
        }
    }
}