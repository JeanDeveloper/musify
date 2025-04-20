package com.jchunga.musicapp.models

import com.jchunga.musicapp.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val title: String
) {
    data object  Home : BottomNavItem(
        route = "Home",
        icon = R.drawable.home,
        title = "Home"
    )

    data object  Favorite : BottomNavItem(
        route = "Favorites",
        icon = R.drawable.lovely,
        title = "Favorites"
    )

    data object  Download : BottomNavItem(
        route = "Download",
        icon = R.drawable.download,
        title = "Download"
    )

    data object  Profile : BottomNavItem(
        route = "Profile",
        icon = R.drawable.profile,
        title = "Profile"
    )
}