package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CarSideProfile(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.allowFileAccess = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.allowUniversalAccessFromFileURLs = true
                    webViewClient = WebViewClient()
                    webChromeClient = android.webkit.WebChromeClient()
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    loadUrl("file:///android_asset/3d_viewer.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CarTopDown(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.allowFileAccess = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.allowUniversalAccessFromFileURLs = true
                    webViewClient = WebViewClient()
                    webChromeClient = android.webkit.WebChromeClient()
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    loadUrl("file:///android_asset/3d_viewer_topdown.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

