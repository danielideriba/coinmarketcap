package com.test.coinmarketcap.ui.nav

import android.net.Uri

sealed class AppScreen(val route: String) {

    protected fun buildRoute(vararg args: Any): String =
        args.fold(route) { acc, arg ->
            acc.replaceFirst(Regex("\\{[^}]+\\}"), Uri.encode(arg.toString()))
        }

    data object Home : AppScreen("home")

    data object CoinDetail : AppScreen("detail/{id}/{name}/{description}/{logo}/{url}/{makerFee}/{takerFee}/{dateLaunched}") {
        fun createRoute(
            id: Int,
            name: String,
            description: String,
            logo: String,
            url: String,
            makerFee: String,
            takerFee: String,
            dateLaunched: String?
        ) = buildRoute(id, name, description, logo, url, makerFee, takerFee, dateLaunched.orEmpty())
    }
}
