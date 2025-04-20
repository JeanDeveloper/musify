package com.jchunga.musicapp.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jchunga.musicapp.R
import com.jchunga.musicapp.models.MusicModel
import kotlinx.coroutines.delay

@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    music: MusicModel,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .width(400.dp)
            .clickable {
                onClick()
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Box(
                modifier = modifier
                    .size(80.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(R.drawable.background_player),
                    contentDescription = null,
                    modifier = modifier
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        ),
                    contentScale = ContentScale.FillWidth,
                )
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Outlined.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = modifier.size(30.dp)
                    )
                }

            }

            Column(
                modifier = modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                val scrollState = rememberScrollState()

                LaunchedEffect (Unit){
                    while (true){
                        scrollState.animateScrollTo(scrollState.maxValue, animationSpec = tween(5000, easing = LinearEasing))
                        delay(500)
                        scrollState.animateScrollTo(0)
                    }
                }

                Text(
                    music.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    maxLines = 1,
                    modifier = modifier.width(250.dp).horizontalScroll(scrollState, enabled = false)
                )
                Text(
                    "unknown author",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White
                )
            }

            Spacer(modifier = modifier.weight(1f))

            IconButton(
                modifier = modifier.size(40.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorites", tint = Color.White, modifier = modifier.size(30.dp))
            }

        }

    }

}