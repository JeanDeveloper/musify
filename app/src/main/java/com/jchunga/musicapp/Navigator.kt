package com.jchunga.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jchunga.musicapp.models.MusicModel
import com.jchunga.musicapp.presentation.HomeScreen
import com.jchunga.musicapp.presentation.musicplayer.MusicPlayerScreen
import com.jchunga.musicapp.utils.Route
import com.jchunga.musicapp.viewmodels.MusicViewModel
import com.jchunga.musicapp.viewmodels.PlayViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigator(
    musicViewModel: MusicViewModel,
    playViewModel : PlayViewModel
){

    val navController = rememberNavController()
    val musicToNavigate by playViewModel.navigateToPlayerScreen.collectAsStateWithLifecycle()
    val currentIndex by playViewModel.currentIndex.collectAsStateWithLifecycle()

    LaunchedEffect(musicToNavigate) {
        musicToNavigate?.let { music ->
            val musicJson = Uri.encode(Json.encodeToString(music))
            navController.navigate(
                "${Route.music.route}/${currentIndex}/${musicJson}/true")
            playViewModel.clearNavigation()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        composable(
            route = Route.Home.route
        ){
            HomeScreen(
                viewModel = musicViewModel,
                navMainController = navController,
                playViewModel = playViewModel
            )
        }
        composable(
            route = "${Route.music.route}/{index}/{musicJson}/{launchedFromNotification}",
            arguments = listOf(navArgument("musicJson") {
                type = NavType.StringType
            }, navArgument("index") {
                type = NavType.IntType
            }, navArgument("launchedFromNotification"){
                type = NavType.BoolType
            })
        ){ backStackEntry ->

            val musicJson = backStackEntry.arguments?.getString("musicJson") ?: ""
            val decodedMusicJson = Uri.decode(musicJson)
            val music = Json.decodeFromString<MusicModel>(decodedMusicJson)
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val launchedFromNotification = backStackEntry.arguments?.getBoolean("launchedFromNotification") ?: false

            MusicPlayerScreen(
                navController = navController,
                music = music,
                playViewModel = playViewModel,
                listMusic = musicViewModel.musicFiles.value,
                index = index,
                launchedFromNotification = launchedFromNotification
            )

        }
    }

}