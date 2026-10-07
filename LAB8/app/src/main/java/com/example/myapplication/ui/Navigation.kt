package com.example.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object CharactersGraph

@Serializable
object Characters

@Serializable
data class CharacterDetailsRoute(
    val characterId: Int
)

@Serializable
object LocationsGraph

@Serializable
object Locations

@Serializable
data class LocationDetailsRoute(
    val locationId: Int
)

@Serializable
object Profile

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val selectedTab = when {
        currentDestination?.hierarchy?.any { it.hasRoute<CharactersGraph>() } == true ->
            BottomTab.CHARACTERS

        currentDestination?.hierarchy?.any { it.hasRoute<LocationsGraph>() } == true ->
            BottomTab.LOCATIONS

        currentDestination?.hasRoute<Profile>() == true ->
            BottomTab.PROFILE

        else -> null
    }

    val showBottomBar =
        currentDestination?.hasRoute<Characters>() == true ||
                currentDestination?.hasRoute<Locations>() == true ||
                currentDestination?.hasRoute<Profile>() == true

    Scaffold(
        bottomBar = {
            if (showBottomBar && selectedTab != null) {
                AppBottomNavigationBar(
                    selectedTab = selectedTab,
                    onCharactersClick = {
                        if (selectedTab != BottomTab.CHARACTERS) {
                            navController.navigate(CharactersGraph) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onLocationsClick = {
                        if (selectedTab != BottomTab.LOCATIONS) {
                            navController.navigate(LocationsGraph) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onProfileClick = {
                        if (selectedTab != BottomTab.PROFILE) {
                            navController.navigate(Profile) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Login> {
                LoginScreen(
                    onStart = {
                        navController.navigate(CharactersGraph) {
                            popUpTo<Login> { inclusive = true }
                        }
                    }
                )
            }

            navigation<CharactersGraph>(startDestination = Characters) {
                composable<Characters> {
                    CharactersScreen(
                        onCharacterClick = { characterId ->
                            navController.navigate(
                                CharacterDetailsRoute(characterId = characterId)
                            )
                        }
                    )
                }

                composable<CharacterDetailsRoute> {
                    CharacterDetailsScreen(
                        onBack = { navController.navigateUp() }
                    )
                }
            }

            navigation<LocationsGraph>(startDestination = Locations) {
                composable<Locations> {
                    LocationsScreen(
                        onLocationClick = { locationId ->
                            navController.navigate(
                                LocationDetailsRoute(locationId = locationId)
                            )
                        }
                    )
                }

                composable<LocationDetailsRoute> {
                    LocationDetailsScreen(
                        onBack = { navController.navigateUp() }
                    )
                }
            }

            composable<Profile> {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
