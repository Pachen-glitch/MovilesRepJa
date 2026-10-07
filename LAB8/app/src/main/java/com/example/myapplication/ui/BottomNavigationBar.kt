package com.example.myapplication.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class BottomTab {
    CHARACTERS,
    LOCATIONS,
    PROFILE
}

@Composable
fun AppBottomNavigationBar(
    selectedTab: BottomTab,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == BottomTab.CHARACTERS,
            onClick = onCharactersClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Characters"
                )
            },
            label = { Text("Characters") }
        )

        NavigationBarItem(
            selected = selectedTab == BottomTab.LOCATIONS,
            onClick = onLocationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Locations"
                )
            },
            label = { Text("Locations") }
        )

        NavigationBarItem(
            selected = selectedTab == BottomTab.PROFILE,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") }
        )
    }
}
