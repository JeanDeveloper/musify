package com.jchunga.musicapp.presentation.musicplayer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jchunga.musicapp.models.MusicModel

@Composable
fun InfoMusicSection(
    music: MusicModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                music.name,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )
            Text(
                "unknown author", style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.Gray
                )
            )
        }

        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite Icon",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

