package com.sagrishin.conavigator

import androidx.activity.compose.BackHandler
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
import com.sagrishin.conavigator.library.NavGraph
import com.sagrishin.conavigator.library.NavRouteProvider
import com.sagrishin.conavigator.library.Navigator
import kotlinx.parcelize.Parcelize

/* First destination */

@Parcelize
data class MainScreenArgs constructor(
    val inputString: String
) : NavArguments

@Destination(
    args = MainScreenArgs::class,
    installIn = MainNavGraph::class,
    isStart = true,
)
@Composable
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

/* Second destination */

@Parcelize
data class NextScreenArgs constructor(
    val inputString: String
) : NavArguments

@Composable
@Destination(
    args = NextScreenArgs::class,
    installIn = MainNavGraph::class,
    isStart = false,
)
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


/* Nav-graph */

interface MainNavGraph : NavGraph {
    companion object : NavRouteProvider {
        override val baseRoute: String = "/"
    }
}
