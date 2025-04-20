package com.jchunga.musicapp.presentation.musicplayer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.jchunga.musicapp.models.MusicModel
import com.jchunga.musicapp.viewmodels.PlayViewModel

@SuppressLint("UnspecifiedRegisterReceiverFlag")
@Composable
fun MusicProgressSection(
    music: MusicModel,
    playViewModel: PlayViewModel
) {
    val currentPosition by playViewModel.currentPosition

    Column {
        Slider(
            value = getSliderValue(currentPosition, music.duration),
            onValueChange = {},
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFFF5722),
                activeTrackColor = Color(0xFFFF5722)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(
                formatDuration(currentPosition),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                )
            )

            Text(
                formatDuration(music.duration),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                )
            )
        }
    }

}

fun getSliderValue(currentPosition: Long, totalDuration: Long): Float {
    if (totalDuration == 0L) return 0f
    return currentPosition.toFloat() / totalDuration.toFloat()
}

@SuppressLint("DefaultLocale")
fun formatDuration(durationMs: Long): String {
    val minutes = durationMs / 60000
    val seconds = (durationMs % 60000) / 1000
    return String.format("%02d:%02d", minutes, seconds)
}