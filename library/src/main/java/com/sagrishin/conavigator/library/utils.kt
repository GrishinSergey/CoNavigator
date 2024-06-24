package com.sagrishin.conavigator.library

import android.os.BadParcelableException
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sagrishin.conavigator.library.entry as regularEntry

fun NavGraphBuilder.entry(navigator: Navigator, navEntry: NavEntry) {
    when (navEntry) {
        is NavDestination -> destination(navigator, navEntry)
        is NavGraph -> graph(navigator, navEntry)
    }
}


fun NavGraphBuilder.destination(navigator: Navigator, navDestination: NavDestination, args: NamedNavArgument? = null) {
    val arguments = when {
        (args != null) -> args
        "{${ArgsBundleKey}}" in navDestination.baseRoute -> namedNavArgument(ArgsBundleKey, NavType.StringType)
        else -> null
    }

    composable(navDestination.baseRoute, listOfNotNull(arguments)) { navDestination.Composable(it, navigator) }
}


fun NavGraphBuilder.graph(navigator: Navigator, navGraph: NavGraph) {
    navigation(navGraph.startRoute.baseRoute, navGraph.baseRoute) {
        navGraph.entries.forEach { destination ->
            if (destination.baseRoute == navGraph.startRoute.baseRoute) {
                startEntry(navigator, destination, navGraph.startRoute.args)
            } else {
                regularEntry(navigator, destination)
            }
        }
    }
}


private fun NavGraphBuilder.startEntry(navigator: Navigator, navEntry: NavEntry, navArguments: NavArguments?) {
    when (navEntry) {
        is NavDestination -> {
            destination(navigator, navEntry, namedNavArgument(ArgsBundleKey, NavType.StringType, navArguments))
        }
        is NavGraph -> {
            graph(navigator, navEntry)
        }
    }
}


private fun namedNavArgument(name: String, type: NavType<*>, defaultValue: NavArguments? = null): NamedNavArgument {
    return navArgument(name) {
        this.type = type
        this.nullable = defaultValue == null
        this.defaultValue = defaultValue?.encode()
    }
}


inline fun <reified T : NavArguments?> NavBackStackEntry.getNavArguments(serializer: NavArguments.Serializer): T {
    val args = arguments?.getString(ArgsBundleKey)?.let(serializer::deserializeFrom)
    return if (null is T) args as T else requireNotNull(args) as T
}


inline fun <reified T : NavArguments> T.encode(): String {
    return Base64.encodeToString(toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
}


inline fun <reified T : NavArguments> String.decode(): T {
    return Base64.decode(toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP).fromByteArray<T>()
}


@Suppress("NOTHING_TO_INLINE")
inline fun NavArguments.toByteArray(): ByteArray {
    return Parcel.obtain().use {
        writeToParcel(this, 0)
        marshall()
    }
}


inline fun <reified T : NavArguments> ByteArray.fromByteArray(): T {
    return Parcel.obtain().use {
        unmarshall(this@fromByteArray, 0, size)
        setDataPosition(0)
        T::class.java.parcelableCreator.createFromParcel(this)
    }
}


inline fun <reified R> Parcel.use(noinline callback: Parcel.() -> R): R {
    return try {
        callback(this)
    } finally {
        recycle()
    }
}


@Suppress("UNCHECKED_CAST")
inline val <reified T : Any> Class<T>.parcelableCreator
    get() : Parcelable.Creator<T> = try {
        getField("CREATOR").get(null) as Parcelable.Creator<T>
    } catch (e: Exception) {
        throw BadParcelableException(e)
    } catch (t: Throwable) {
        throw BadParcelableException(t.message)
    }


const val ArgsBundleKey = "args"
