package com.example

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.MainScreen

class MainActivity : FragmentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Request maximum available refresh rate (120Hz) for ultra-smooth scrolling
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display?.supportedModes?.maxByOrNull { it.refreshRate }?.let { maxMode ->
            window.attributes = window.attributes.apply {
                preferredDisplayModeId = maxMode.modeId
            }
        }
    }

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          MainScreen()
        }
      }
    }
  }
}
