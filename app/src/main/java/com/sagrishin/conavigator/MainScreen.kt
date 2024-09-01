package com.sagrishin.conavigator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sagrishin.conavigator.generator.annotation.Destination
import com.sagrishin.conavigator.library.NavArguments
import com.sagrishin.conavigator.library.Navigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenArgs constructor(
    val inputString: String
) : NavArguments


@Composable
@Destination(isStart = true, installIn = MainNavGraph::class)
fun MainScreen(args: MainScreenArgs, navigator: Navigator) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.clickable {
                navigator.navigateTo(NextRoute(NextScreenArgs(inputString = "And Hello again")))
            },
            text = args.inputString
        )
    }
}
