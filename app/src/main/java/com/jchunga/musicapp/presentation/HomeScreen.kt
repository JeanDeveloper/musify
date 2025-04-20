package com.jchunga.musicapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jchunga.musicapp.components.CustomAudioPlayer
import com.jchunga.musicapp.models.BottomNavItem
import com.jchunga.musicapp.tabs.DownloadTab
import com.jchunga.musicapp.tabs.FavoritesTab
import com.jchunga.musicapp.tabs.HomeTab
import com.jchunga.musicapp.tabs.ProfileTab
import com.jchunga.musicapp.ui.theme.ColorNavigationBottom
import com.jchunga.musicapp.ui.theme.ColorPrimary
import com.jchunga.musicapp.ui.theme.ColorSecondaryLetter
import com.jchunga.musicapp.viewmodels.MusicViewModel
import com.jchunga.musicapp.viewmodels.PlayViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: MusicViewModel,
    navMainController: NavHostController,
    playViewModel: PlayViewModel
) {
    val context = LocalContext.current
    val hasPermission by viewModel.hasPermission.collectAsState()
    val uri by viewModel.uri.collectAsState()
    val navController = rememberNavController()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            viewModel.updatePermissionStatus(context)
        } else {
            Log.e("HomeScreen", "HomeScreen: Permiso Denegado", )
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permission", "Permiso de notificación concedido")
        } else {
            Log.d("Permission", "Permiso de notificación denegado")
        }
    }

    val folderPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            viewModel.saveMusicFolder(it.toString())
        }
    }

    LaunchedEffect(Unit) {
        viewModel.updatePermissionStatus(context)
        val persistedPermissions = context.contentResolver.persistedUriPermissions

        if (!hasPermission) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            val permission = if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            permissionLauncher.launch(permission)

        }

        if (persistedPermissions.isEmpty()) {
            pickFolder(context, folderPickerLauncher)
        } else {
            Log.d("Permissions", "Ya tienes permisos persistentes en: ${persistedPermissions.map { it.uri }}")
        }

    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController = navController) },
        containerColor = ColorPrimary
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
        ){

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(it)
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeTab(
                        musicViewModel = viewModel,
                        hasPermission = hasPermission,
                        uri = uri,
                        onSelectionFolderClick = { pickFolder(context, folderPickerLauncher)   },
                        navController = navMainController
                    )
                }
                composable(BottomNavItem.Favorite.route) { FavoritesTab() }
                composable(BottomNavItem.Download.route) { DownloadTab() }
                composable(BottomNavItem.Profile.route) { ProfileTab() }
            }
            CustomAudioPlayer(
                modifier = Modifier.align(Alignment.BottomCenter),
                playViewModel = playViewModel
            )

        }

    }

}
fun pickFolder(context: Context, folderLauncher: ActivityResultLauncher<Intent>){
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    intent.addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
                Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
    )
    folderLauncher.launch(intent)
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorite,
        BottomNavItem.Download,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = ColorNavigationBottom
    ) {

        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if(currentRoute == item.route) ColorSecondaryLetter else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ColorSecondaryLetter,
                    selectedTextColor = ColorSecondaryLetter,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }

    }

}
