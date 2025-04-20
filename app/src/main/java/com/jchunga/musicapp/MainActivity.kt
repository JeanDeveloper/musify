package com.jchunga.musicapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.jchunga.musicapp.datastore.DataStoreManager
import com.jchunga.musicapp.ui.theme.MusicAppTheme
import com.jchunga.musicapp.viewmodels.MusicViewModel
import com.jchunga.musicapp.viewmodels.MusicViewModelFactory
import com.jchunga.musicapp.viewmodels.PlayViewModel
import com.jchunga.musicapp.viewmodels.PlayViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MusicViewModel
    private lateinit var playViewModel: PlayViewModel

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        playViewModel.handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                 View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        val dataStoreManager = DataStoreManager(applicationContext)
        val viewModelFactory = MusicViewModelFactory(application, dataStoreManager)
        val playViewModelFactory = PlayViewModelFactory(application, dataStoreManager)
        viewModel = ViewModelProvider(this, viewModelFactory)[MusicViewModel::class.java]
        playViewModel = ViewModelProvider(this, playViewModelFactory)[PlayViewModel::class.java]

        playViewModel.handleIntent(intent)
        setContent {
            MusicAppTheme {
                Navigator(
                    musicViewModel = viewModel,
                    playViewModel = playViewModel
                )
            }
        }

    }

}
