package com.jsalin.laguiadejerryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jsalin.laguiadejerryapp.presentation.navigation.AppNavHost
import com.jsalin.laguiadejerryapp.presentation.theme.JerryGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JerryGuideTheme {
                AppNavHost()
            }
        }
    }
}