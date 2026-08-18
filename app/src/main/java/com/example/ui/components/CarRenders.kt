package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class WebAppInterface(private val mContext: Context) {
    @JavascriptInterface
    fun onComponentTapped(title: String, message: String) {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            Toast.makeText(mContext, "$title: $message", Toast.LENGTH_LONG).show()
        }
    }
}

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
                    addJavascriptInterface(WebAppInterface(context), "Android")
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
                    addJavascriptInterface(WebAppInterface(context), "Android")
                    loadUrl("file:///android_asset/3d_viewer_topdown.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
