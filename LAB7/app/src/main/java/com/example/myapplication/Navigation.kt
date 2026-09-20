package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Characters

@Serializable
data class CharacterDetails(val characterId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(
                onStart = {
                    navController.navigate(Characters) {
                        popUpTo<Login> { inclusive = true }
                    }
                }
            )
        }
        composable<Characters> {
            CharactersScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(CharacterDetails(characterId = characterId))
                }
            )
        }
        composable<CharacterDetails> { backStackEntry ->
            val details = backStackEntry.toRoute<CharacterDetails>()
            CharacterDetailsScreen(
                characterId = details.characterId,
                onBack = { navController.navigateUp() }
            )
        }
    }
}
