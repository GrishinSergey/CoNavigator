package com.sagrishin.conavigator.library

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

interface Navigator {
    fun navigateTo(route: NavRoute)
    fun replaceTo(route: NavRoute)
    fun navigateUp()
}


sealed interface NavEntry {
    val baseRoute: String
}


interface NavRouteProvider {
    val baseRoute: String
}


interface NavDestination : NavEntry {
    @Composable
    fun Composable(entry: NavBackStackEntry, navigator: Navigator)
}


interface NavGraph : NavEntry {
    val entries: Set<NavEntry>
    val startRoute: NavRoute
}


interface NavRoute {
    val baseRoute: String
    val args: NavArguments?
    fun computeRoute(): String
}


interface NavArguments : Parcelable {
    interface Serializer {
        fun deserializeFrom(string: String): NavArguments?
        fun serializeToString(args: NavArguments): String
    }
}
