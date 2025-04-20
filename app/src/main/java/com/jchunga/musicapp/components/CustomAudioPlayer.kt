package com.jchunga.musicapp.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jchunga.musicapp.viewmodels.PlayViewModel

@Composable
fun CustomAudioPlayer(
    modifier: Modifier = Modifier,
    playViewModel: PlayViewModel
) {

    val isPlaying by playViewModel.isPlaying
    val currentMusic by playViewModel.currentMusic


    if(isPlaying){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 80.dp, horizontal = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ){
            Row (
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ){
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Star",
                    tint = Color.White
                )
                currentMusic?.let {
                    Text(
                        text = it.name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                IconButton(
                    onClick = {
                        playViewModel.togglePlayPause(Uri.parse(currentMusic?.path), currentMusic!!)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Pause,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        playViewModel.stopMusic()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }


            }

        }
    }



}

//@Preview
//@Composable
//private fun CustomAudioPlayerPreview() {
//    CustomAudioPlayer()
//}