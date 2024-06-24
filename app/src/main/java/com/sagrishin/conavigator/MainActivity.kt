package com.sagrishin.conavigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sagrishin.conavigator.library.NavigatorImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navHostController = rememberNavController()
            NavHost(
                modifier = Modifier,
                navHostController = navHostController,
                navigator = NavigatorImpl(navHostController),
                graph = MainNavGraphImpl(
                    startRoute = MainRoute(MainScreenArgs("Hello, world")),
                ),
            )
        }
    }
}
