package com.example.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.viewmodel.SessionViewModel
import kotlinx.serialization.Serializable

@Serializable
object SessionCheck

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
fun AppNavigation(
    sessionViewModel: SessionViewModel = viewModel()
) {
    val navController = rememberNavController()

    val sessionUiState by sessionViewModel.uiState
        .collectAsStateWithLifecycle()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentDestination =
        currentBackStackEntry?.destination

    val selectedTab = when {
        currentDestination
            ?.hierarchy
            ?.any { destination ->
                destination.hasRoute<CharactersGraph>()
            } == true -> BottomTab.CHARACTERS

        currentDestination
            ?.hierarchy
            ?.any { destination ->
                destination.hasRoute<LocationsGraph>()
            } == true -> BottomTab.LOCATIONS

        currentDestination
            ?.hasRoute<Profile>() == true ->
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
            startDestination = SessionCheck,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SessionCheck> {
                SessionCheckScreen()

                LaunchedEffect(
                    sessionUiState.isChecking,
                    sessionUiState.userName
                ) {
                    if (!sessionUiState.isChecking) {
                        if (sessionUiState.userName.isNullOrBlank()) {
                            navController.navigate(Login) {
                                popUpTo<SessionCheck> {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(CharactersGraph) {
                                popUpTo<SessionCheck> {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                }
            }

            composable<Login> {
                LoginScreen(
                    onLogin = { name ->
                        sessionViewModel.login(
                            name = name,
                            onSuccess = {
                                navController.navigate(CharactersGraph) {
                                    popUpTo<Login> {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                )
            }

            navigation<CharactersGraph>(
                startDestination = Characters
            ) {
                composable<Characters> {
                    CharactersScreen(
                        onCharacterClick = { characterId ->
                            navController.navigate(
                                CharacterDetailsRoute(
                                    characterId = characterId
                                )
                            )
                        }
                    )
                }

                composable<CharacterDetailsRoute> {
                    CharacterDetailsScreen(
                        onBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }

            navigation<LocationsGraph>(
                startDestination = Locations
            ) {
                composable<Locations> {
                    LocationsScreen(
                        onLocationClick = { locationId ->
                            navController.navigate(
                                LocationDetailsRoute(
                                    locationId = locationId
                                )
                            )
                        }
                    )
                }

                composable<LocationDetailsRoute> {
                    LocationDetailsScreen(
                        onBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }

            composable<Profile> {
                ProfileScreen(
                    userName = sessionUiState.userName.orEmpty(),
                    onLogout = {
                        sessionViewModel.logout(
                            onSuccess = {
                                navController.navigate(Login) {
                                    popUpTo<CharactersGraph> {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}
