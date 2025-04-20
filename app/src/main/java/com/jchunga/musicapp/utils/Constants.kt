package com.jchunga.musicapp.utils

import com.jchunga.musicapp.models.Usuario

object Constants {
    val USER_CURRENT = Usuario(
        email = "john.adams@example-pet-store.com",
        name = "Jean",
    )
}

sealed class Route(
    val route: String
){
    data object Home: Route("home")
    data object music: Route("music")
}

