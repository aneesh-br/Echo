package com.aneesh.echo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.aneesh.echo.ui.navigation.EchoNavHost
import com.aneesh.echo.ui.theme.EchoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EchoNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}