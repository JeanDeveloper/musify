package com.jchunga.musicapp.presentation.musicplayer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jchunga.musicapp.models.MusicModel
import com.jchunga.musicapp.viewmodels.PlayViewModel


@SuppressLint("UnspecifiedRegisterReceiverFlag")
@Composable
fun CustomContent(
    navController: NavHostController,
    playViewModel: PlayViewModel,
    listMusic: List<MusicModel>,
    index: Int,
    launchedFromNotification: Boolean
) {
    val isPlaying by playViewModel.isPlaying

    val currentMusic by playViewModel.currentMusic

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if( !launchedFromNotification ){
            playViewModel.setMusicListAndPlay(listMusic, index)
        } else {
            playViewModel.clearNavigation()
        }
    }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?){
                when(intent?.action){
                    "MUSIC_DURATION" -> {
                        val duration = intent.getIntExtra("DURATION", 0)
                        playViewModel.setDuration(duration.toLong())
                    }
                    "MUSIC_STATE_UPDATE" -> {
                        val currentPosition = intent.getIntExtra("CURRENT_POSITION", 0)
                        playViewModel.setCurrentPosition(currentPosition.toLong())
                    }
                    "MUSIC_CURRENT_MUSIC" -> {
                        val currentMusic = intent.getParcelableExtra<MusicModel>("CURRENT_MUSIC")
                        playViewModel.setCurrentMusic(currentMusic!!)
                    }
                }
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction("MUSIC_DURATION")
            addAction("MUSIC_STATE_UPDATE")
            addAction("MUSIC_CURRENT_MUSIC")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                receiver,
                intentFilter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(receiver, intentFilter)
        }

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        currentMusic?.let {
            InfoMusicSection(
                music = it
            )

            MusicProgressSection(
                music = it,
                playViewModel = playViewModel
            )

            PlayerControls(
                onClick = {
                    playViewModel.togglePlayPause(Uri.parse(it.path), it)
                },
                onPreviousClick = {
                    playViewModel.previousMusic()
                },
                onNextClick = {
                    playViewModel.nextMusic()
                },
                icon = if (isPlaying) Icons.Outlined.PauseCircleOutline else Icons.Outlined.PlayCircleOutline
            )
        }

    }

}
