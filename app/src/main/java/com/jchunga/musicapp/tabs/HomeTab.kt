package com.jchunga.musicapp.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jchunga.musicapp.components.HeaderSection
import com.jchunga.musicapp.components.ListMusicSection
import com.jchunga.musicapp.components.SpecialForYouSection
import com.jchunga.musicapp.viewmodels.MusicViewModel

@Composable
fun HomeTab(
    musicViewModel: MusicViewModel,
    hasPermission: Boolean,
    uri: String?,
    onSelectionFolderClick: () -> Unit,
    navController: NavHostController
) {

    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)
    ){

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hasPermission) {
                if (uri.isNullOrEmpty()) {
                    Button(
                        onClick = {
                            onSelectionFolderClick()
                        }
                    ) {
                        Text("Seleccionar Carpeta de Música")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item {
                            HeaderSection(
                                title = "Good night",
                                subTitle = "Jean"
                            )
                        }

                        item { SpecialForYouSection() }

                        item {
                            ListMusicSection(
                                musicViewModel = musicViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            } else {
                Text("Permiso requerido para acceder a la música", color = MaterialTheme.colorScheme.error)
            }
        }

//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//        ) {
//
//            item {
//                HeaderSection(
//                    title = "Good night",
//                    subTitle = "Jean"
//                )
//            }
//
//            item { SpecialForYouSection() }
//
//            item { ListMusicSection() }
//
//        }

    }


}