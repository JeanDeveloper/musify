package com.jchunga.musicapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jchunga.musicapp.R

@Composable
fun SpecialCard(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .padding(end = 20.dp)
            .width(400.dp)
            ,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f)
        )

    ) {

        Box(
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(R.drawable.background_player),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(15.dp)
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
                    modifier = modifier.size(40.dp)
                )
            }

        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Sueño profundo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                Text(
                    "Paisaje sonoro",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White
                )
            }

            IconButton(
                modifier = modifier.size(40.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorites", tint = Color.White, modifier = modifier.size(30.dp))
            }

        }

    }

}