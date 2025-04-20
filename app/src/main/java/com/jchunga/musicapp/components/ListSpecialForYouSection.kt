package com.jchunga.musicapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ListEspecialForYouSection(modifier: Modifier = Modifier) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
    ) {

        items(10){
            SpecialCard()
        }

    }

}