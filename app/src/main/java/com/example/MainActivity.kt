package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.McKaysViewModel
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainDashboardScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppScreen {
    LOGIN,
    DASHBOARD
}

class MainActivity : ComponentActivity() {

    private val viewModel: McKaysViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    McKaysApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun McKaysApp(viewModel: McKaysViewModel) {
    var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }

    Crossfade(
        targetState = currentScreen,
        animationSpec = tween(400),
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            AppScreen.LOGIN -> {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { currentScreen = AppScreen.DASHBOARD }
                )
            }
            AppScreen.DASHBOARD -> {
                MainDashboardScreen(
                    viewModel = viewModel,
                    onLogout = { currentScreen = AppScreen.LOGIN }
                )
            }
        }
    }
}
