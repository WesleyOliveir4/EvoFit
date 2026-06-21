package com.example.evofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.evofit.navigation.NavNavigation
import com.example.evofit.presentation.ui.theme.EvoFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _root_ide_package_.com.example.evofit.presentation.ui.theme.EvoFitTheme {
                NavNavigation()
            }
        }
    }
}
