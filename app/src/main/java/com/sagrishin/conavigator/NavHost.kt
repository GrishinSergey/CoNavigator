package com.sagrishin.conavigator

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sagrishin.conavigator.library.NavGraph
import com.sagrishin.conavigator.library.Navigator
import com.sagrishin.conavigator.library.entry

typealias EnterAnimation = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
typealias ExitAnimation = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    navigator: Navigator,
    graph: NavGraph,
    enterTransition: EnterAnimation = { slideIntoContainer(Left, tween(250)) },
    exitTransition: ExitAnimation = { slideOutOfContainer(Left, tween(250)) },
    popEnterTransition: EnterAnimation = { slideIntoContainer(Right, tween(250)) },
    popExitTransition: ExitAnimation = { slideOutOfContainer(Right, tween(250)) },
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = graph.baseRoute,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        builder = { entry(navigator, graph) },
    )
}
