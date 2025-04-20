package com.jchunga.musicapp.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jchunga.musicapp.utils.Route
import com.jchunga.musicapp.viewmodels.MusicViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun ListMusicSection(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    navController: NavHostController
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            "Lo-fi Sound",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        ListMusic(
            modifier = modifier.heightIn(
                min = 0.dp,
                max = (listState.layoutInfo.totalItemsCount * 120).dp
            ),
            listState = listState,
            musicViewModel = musicViewModel,
            navController = navController
        )

    }
}

@Composable
fun ListMusic(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    musicViewModel: MusicViewModel,
    navController: NavHostController
) {

    val musicFiles by musicViewModel.musicFiles.collectAsState()

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxWidth()
    ) {
        items(musicFiles.size){ index ->
            val music = musicFiles[index]
            MusicCard(
                music = music,
                onClick = {
                    val musicJson = Json.encodeToString(music)
                    val encodedMusicJson = Uri.encode(musicJson)
                    navController.navigate("${Route.music.route}/${index}/$encodedMusicJson/false")
                }
            )
        }
    }
}