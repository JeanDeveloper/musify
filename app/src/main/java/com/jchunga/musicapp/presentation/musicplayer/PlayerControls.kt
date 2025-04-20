package com.jchunga.musicapp.presentation.musicplayer

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PlayerControls(
    onClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    icon: ImageVector
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                onPreviousClick()
            }
        ) {
            Icon(
                Icons.Outlined.SkipPrevious,
                contentDescription = "Anterior",
                tint = Color.White
            )
        }

        IconButton(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF5722))
        ) {
            Icon(
                icon,
                contentDescription = "Play o Pause",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        IconButton(
            onClick = {
                onNextClick()
            }
        ) {
            Icon(
                Icons.Outlined.SkipNext,
                contentDescription = "Siguiente",
                tint = Color.White
            )
        }
    }
}