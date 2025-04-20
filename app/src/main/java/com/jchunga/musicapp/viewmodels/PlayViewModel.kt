package com.jchunga.musicapp.viewmodels

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jchunga.musicapp.datastore.DataStoreManager
import com.jchunga.musicapp.models.MusicModel
import com.jchunga.musicapp.services.MusicService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PlayViewModel(
    application: Application,
    private val dataStoreManager: DataStoreManager
) : AndroidViewModel(application) {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private val _currentPosition = mutableLongStateOf(0L)
    val currentPosition: State<Long> get() = _currentPosition

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> get() = _isPlaying

    private val _isPaused = mutableStateOf(false)

    private val _duration = mutableLongStateOf(0L)

    private val _currentMusic = mutableStateOf<MusicModel?>(null)
    val currentMusic: State<MusicModel?> get() = _currentMusic

    private val _currentIndex = MutableStateFlow<Int>(0)
    val currentIndex: StateFlow<Int> get() = _currentIndex

    private val _navigateToPlayerScreen = MutableStateFlow<MusicModel?>(null)
    val navigateToPlayerScreen: StateFlow<MusicModel?> = _navigateToPlayerScreen

    fun handleIntent(intent: Intent?) {
        val openFromNotification = intent?.getBooleanExtra("OPEN_FROM_NOTIFICATION", false) == true
        if (openFromNotification) {
            val songTitle = intent?.getStringExtra("SONG_TITLE") ?: return
            val songUri = intent.getStringExtra("SONG_URI") ?: return
            val songName = intent.getStringExtra("SONG_NAME") ?: return
            val currentPosition = intent.getIntExtra("CURRENT_POSITION", 0)
            val duration = intent.getLongExtra("DURATION", 0)
            val currentIndex = intent.getIntExtra("CURRENT_INDEX", 0)

            val music  = MusicModel(
                name = songName,
                path = songUri,
                author = null,
                duration = duration
            )
            _navigateToPlayerScreen.value = music
            _currentMusic.value = music
            _currentPosition.longValue = currentPosition.toLong()
            _currentIndex.value = currentIndex
            _isPlaying.value = true
            _isPaused.value = false
        }
    }

    fun clearNavigation() {
        _navigateToPlayerScreen.value = null
    }

    fun setCurrentPosition(currentPosition: Long) {
        _currentPosition.longValue = currentPosition
    }

    fun setDuration(duration: Long) {
        _duration.longValue = duration
    }

    fun setCurrentMusic(currentMusic : MusicModel){
        _currentMusic.value = currentMusic
    }

    init {
        mediaPlayer.setOnCompletionListener {
            _isPlaying.value = false
        }
    }

    private val context = getApplication<Application>()

    fun togglePlayPause(uri: Uri, music: MusicModel) {
        if (_isPlaying.value) {
            // Pausar la música
            val serviceIntent = Intent(context, MusicService::class.java).apply {
                action = MusicService.ACTION_PAUSE
                putExtra("SONG_TITLE", music.name)
                putExtra("SONG_URI", uri.toString())
                putExtra("SONG_NAME", music.name)
            }
            context.startService(serviceIntent)
            _isPlaying.value = false
            _isPaused.value = true

            return

        }

        if(_isPaused.value){
            //Reanudar
            val serviceIntent = Intent(context, MusicService::class.java).apply {
                action = MusicService.ACTION_RESUME
            }
            context.startService(serviceIntent)
            _isPlaying.value = true
            _isPaused.value = false
            return

        }

        val serviceIntent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra("SONG_TITLE", music.name)
            putExtra("SONG_URI", uri.toString())
            putExtra("SONG_NAME", music.name)
        }
        ContextCompat.startForegroundService(context, serviceIntent)
        _isPlaying.value = true
        _isPaused.value = false

    }

    fun nextMusic() {
        val serviceIntent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_NEXT
        }
        context.startService(serviceIntent)
    }

    fun previousMusic() {
        val serviceIntent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PREVIOUS
        }
        context.startService(serviceIntent)
    }

    fun setMusicListAndPlay(list: List<MusicModel>, index: Int) {
        val music = list[index]
        _currentMusic.value = music
        val serviceIntent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra("SONG_TITLE", music.name)
            putExtra("SONG_URI", Uri.parse(music.path).toString())
            putExtra("SONG_NAME", music.name)
            putParcelableArrayListExtra("MUSIC_LIST", arrayListOf(*list.toTypedArray()))
            putExtra("MUSIC_INDEX", index)
            putExtra("DURATION", music.duration)
        }
        _isPlaying.value = true
        _isPaused.value = false
        ContextCompat.startForegroundService(context, serviceIntent)

    }

    fun stopMusic(){
        mediaPlayer.stop()
        mediaPlayer.reset()
        _isPlaying.value = false
        _isPaused.value = false
        _currentPosition.longValue = 0L
        _duration.longValue = 0L
    }

    fun registerReceiver(context: Context){
        val filter = IntentFilter("MUSIC_STATE_UPDATE")
        ContextCompat.registerReceiver(context, object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _currentPosition.value = (intent?.getIntExtra("currentPosition", 0) ?: 0).toLong()
                _duration.value = (intent?.getIntExtra("duration", 0) ?: 0).toLong()
                _isPlaying.value = intent?.getBooleanExtra("isPlaying", false) ?: false
            }
        }, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }




}

class PlayViewModelFactory(
    private val application: Application,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewModel::class.java)) {
            return PlayViewModel(application,  dataStoreManager ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


