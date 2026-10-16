package com.listamercado.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

val FondoSuave = Color(0xFFF7FAF2)
val VerdeApp = Color(0xFF5BB373)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercadoTheme {
                val vm: MercadoViewModel = viewModel()
                AppRoot(vm)
            }
        }
    }
}

@Composable
fun MercadoTheme(content: @Composable () -> Unit) {
    // Tema claro y amigable (no cambia con el modo oscuro del teléfono).
    @Suppress("UNUSED_VARIABLE") val oscuro = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = VerdeApp,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFDDF1E2),
            onPrimaryContainer = Color(0xFF1D3B27),
            secondaryContainer = Color(0xFFDDF1E2),
            onSecondaryContainer = Color(0xFF1D3B27),
            background = FondoSuave,
            surface = Color.White,
            surfaceVariant = Color(0xFFE9EEE4)
        ),
        content = content
    )
}
