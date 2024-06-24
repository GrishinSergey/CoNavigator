package com.sagrishin.conavigator.library

import androidx.navigation.NavController

class NavigatorImpl constructor(
    private val navController: NavController,
) : Navigator {
    override fun navigateTo(route: NavRoute) {
        navController.navigate(route.computeRoute())
    }

    override fun replaceTo(route: NavRoute) {
        val toRoute = route.computeRoute()
        navController.navigate(toRoute) {
            popUpTo(toRoute) { inclusive = true }
        }
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}
