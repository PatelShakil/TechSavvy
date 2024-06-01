package com.shakilpatel.techsavvy.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ComposeToast(msg : String) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}