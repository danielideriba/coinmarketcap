package com.test.coinmarketcap.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.test.coinmarketcap.ui.features.detail.CoinDetailScreen
import com.test.coinmarketcap.ui.features.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.route
    ) {
        composable(AppScreen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = AppScreen.CoinDetail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("logo") { type = NavType.StringType },
                navArgument("website") { type = NavType.StringType },
                navArgument("makerFee") { type = NavType.FloatType },
                navArgument("takerFee") { type = NavType.FloatType },
                navArgument("dateLaunched") { type = NavType.StringType }

            )
        ) { backStackEntry ->
            CoinDetailScreen(
                id = backStackEntry.arguments?.getInt("id") ?: 0,
                name = backStackEntry.arguments?.getString("name").orEmpty(),
                description = backStackEntry.arguments?.getString("description").orEmpty(),
                logo = backStackEntry.arguments?.getString("logo").orEmpty(),
                website = backStackEntry.arguments?.getString("website").orEmpty(),
                makerFee = backStackEntry.arguments?.getFloat("makerFee")?.toDouble() ?: 0.0,
                takerFee = backStackEntry.arguments?.getFloat("takerFee")?.toDouble() ?: 0.0,
                dateLaunched = backStackEntry.arguments?.getString("dateLaunched").orEmpty(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}