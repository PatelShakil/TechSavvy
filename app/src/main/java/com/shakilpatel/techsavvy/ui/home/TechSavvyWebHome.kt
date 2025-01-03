package com.shakilpatel.techsavvy.ui.home

import android.app.Activity
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TechSavvyWebHome(modifier: Modifier = Modifier) {
    var isLoading by remember{ mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity // Get the activity
    val webView = remember{mutableStateOf(WebView(context))}

    BackHandler (onBack = {
        if(webView.value.canGoBack()){
            webView.value.goBack()
        }else{
            activity?.finish()
        }
    })

    Column {
        Spacer(
            modifier = Modifier
                .background(Color(0xFF000315))
                .height(22.dp)
                .fillMaxWidth(),
        )
        AndroidView(
            modifier = modifier,
            factory = { context -> createWebView(context) }
        ) { webView1 ->
            webView.value = webView1
            webView.value.webViewClient = object : WebViewClient() {
                // ... other overrides ...
                override fun onPageFinished(view: WebView, url: String) {
                    isLoading = false

                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }



}
    fun createWebView(context: Context): WebView {
    return WebView(context).apply {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true // Enable local storage
            databaseEnabled = true // Enable database storage
            setSupportZoom(true) // Allow zooming
            builtInZoomControls = true // Show zoom controls
            displayZoomControls = false // Hide zoom controls UI
            useWideViewPort = true // Use wide viewport
            loadWithOverviewMode = true // Overview mode for better scaling
            // ... other relevant settings ...
        }
        loadUrl("https://tech-savvy-solution.web.app")
    }
}
