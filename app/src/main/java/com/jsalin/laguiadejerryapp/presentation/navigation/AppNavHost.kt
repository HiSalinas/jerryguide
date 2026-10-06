package com.jsalin.laguiadejerryapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jsalin.laguiadejerryapp.presentation.characterdetail.CharacterDetailScreen
import com.jsalin.laguiadejerryapp.presentation.characterlist.CharacterListScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = CharacterListRoute) {
        composable<CharacterListRoute> {
            CharacterListScreen(
                onCharacterClick = { id -> navController.navigate(CharacterDetailRoute(id)) },
            )
        }
        composable<CharacterDetailRoute> {
            CharacterDetailScreen(onBack = navController::navigateUp)
        }
    }
}