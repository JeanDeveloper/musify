package com.jchunga.musicapp.presentation.musicplayer

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.jchunga.musicapp.R
import com.jchunga.musicapp.models.MusicModel
import com.jchunga.musicapp.viewmodels.PlayViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MusicPlayerScreen(
    navController: NavHostController,
    music: MusicModel,
    playViewModel: PlayViewModel,
    listMusic: List<MusicModel>,
    index: Int,
    launchedFromNotification: Boolean
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.background_player),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )

        CustomContent(
            navController = navController,
            playViewModel = playViewModel,
            listMusic = listMusic,
            index = index,
            launchedFromNotification = launchedFromNotification
        )

    }

}
