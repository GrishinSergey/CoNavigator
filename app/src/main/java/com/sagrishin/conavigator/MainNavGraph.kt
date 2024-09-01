package com.sagrishin.conavigator

import com.sagrishin.conavigator.library.NavGraph
import com.sagrishin.conavigator.library.NavRouteProvider

interface MainNavGraph : NavGraph {
    companion object : NavRouteProvider {
        override val baseRoute: String = "/"
    }
}