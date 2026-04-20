package com.dashagoulyaeva.ritm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dashagoulyaeva.ritm.core.ui.theme.ritmTheme
import com.dashagoulyaeva.ritm.navigation.ritmNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ritmTheme {
                ritmNavGraph()
            }
        }
    }
}
