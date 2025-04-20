package com.jchunga.musicapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jchunga.musicapp.ui.theme.ColorSecondaryLetter


@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {

        Text(
            "$title,",
            style = MaterialTheme.typography.displayMedium

        )
        Text(
            subTitle,
            style = MaterialTheme.typography.displayMedium.copy(
                color = ColorSecondaryLetter
            ),

        )

    }
}