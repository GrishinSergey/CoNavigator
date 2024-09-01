package com.sagrishin.conavigator

import androidx.activity.compose.BackHandler
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
data class NextScreenArgs constructor(
    val inputString: String
) : NavArguments

@Composable
@Destination(installIn = MainNavGraph::class)
fun NextScreen(args: NextScreenArgs, navigator: Navigator) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = args.inputString)
    }

    BackHandler(true) {
        navigator.navigateUp()
    }
}
