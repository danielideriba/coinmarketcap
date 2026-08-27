package com.test.coinmarketcap.ui.nav

import android.net.Uri

sealed class AppScreen(val route: String) {

    protected fun buildRoute(vararg args: Any): String =
        args.fold(route) { acc, arg ->
            acc.replaceFirst(Regex("\\{[^}]+\\}"), Uri.encode(arg.toString()))
        }

    data object Home : AppScreen("home")

    data object CoinDetail : AppScreen(
        route = "detail/{id}/{name}/{description}/{logo}/{website}/{makerFee}/{takerFee}/{dateLaunched}"
    ) {
        fun createRoute(
            id: Int,
            name: String,
            description: String,
            logo: String,
            website: String,
            makerFee: Double?,
            takerFee: Double?,
            dateLaunched: String?
        ) = buildRoute(
            id, name, description, logo, website, makerFee ?: 0.0, takerFee  ?: 0.0, dateLaunched.orEmpty()
        )
    }
}
