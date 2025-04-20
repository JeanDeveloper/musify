package com.jchunga.musicapp.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.documentfile.provider.DocumentFile
import com.jchunga.musicapp.MainActivity
import com.jchunga.musicapp.R
import com.jchunga.musicapp.datastore.DataStoreManager
import com.jchunga.musicapp.models.MusicModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class MusicService: Service() {

    private val mediaPlayer = MediaPlayer()
    private var musicList: List<MusicModel> = listOf()
    private var currentIndex = 0
    private lateinit var dataStoreManager: DataStoreManager

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_RESUME = "ACTION_RESUME"
    }

    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DataStoreManager(applicationContext)
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val channelId = "music_channel"
        val title = intent?.getStringExtra("SONG_TITLE") ?: "Reproduciendo música"
        val name = intent?.getStringExtra("SONG_NAME") ?: "Sin Nombre"
        val uri = Uri.parse((intent?.getStringExtra("SONG_URI") ?: ""))
        val duration = intent?.getLongExtra("DURATION", 0) ?: 0
        val currentIndexP = intent?.getIntExtra("MUSIC_INDEX", 0) ?: 0
        val notification = createNotification(title, name, uri, duration, currentIndexP)
        createNotificationChannel(channelId)
        startForeground(1, notification)
        when (action) {
            ACTION_PLAY -> {
                val list = intent.getParcelableArrayListExtra<MusicModel>("MUSIC_LIST")
                val index = intent.getIntExtra("MUSIC_INDEX", 0)
                musicList = list ?: listOf()
                currentIndex = index
                playMusic(uri, name)
            }
            ACTION_PAUSE    -> pauseMusic()
            ACTION_RESUME   -> resumeMusic()
            ACTION_NEXT     -> nextMusic()
            ACTION_PREVIOUS -> previousMusic()
        }
        return START_NOT_STICKY
    }

    private fun createNotification(title: String, name: String, uri: Uri, duration: Long, currentIndex: Int): Notification {
        val channelId = "music_channel"
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("OPEN_FROM_NOTIFICATION", true)
            putExtra("SONG_TITLE", title)
            putExtra("CURRENT_POSITION", mediaPlayer.currentPosition)
            putExtra("SONG_URI", uri.toString())
            putExtra("SONG_NAME", name)
            putExtra("DURATION", duration)
            putExtra("CURRENT_INDEX", currentIndex)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText("Reproduciendo Musica ...")
            .setSmallIcon(R.drawable.profile)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal de Música",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Canal para reproducción de música"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun playMusic(uri: Uri, name: String) {
        mediaPlayer.reset()
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        val contentResolver = applicationContext.contentResolver
        val context = applicationContext

        CoroutineScope(Dispatchers.IO).launch{
            val persistedUri = dataStoreManager.getUri().firstOrNull()?.let { Uri.parse(it) }
            val pickedDir    = DocumentFile.fromTreeUri(context, persistedUri!!)
            val targetFile   = pickedDir?.listFiles()?.find {
                it.name == File(uri.path ?: "").name
            }

            targetFile?.let {

                val inputStream = contentResolver.openInputStream(it.uri)
                val tempFile = File.createTempFile("temp_music", ".mp3", context.cacheDir)
                val outputStream = FileOutputStream(tempFile)

                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    mediaPlayer.setDataSource(tempFile.absolutePath)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        mediaPlayer.start()
                        val intent = Intent("MUSIC_DURATION").apply {
                            putExtra("DURATION", mediaPlayer.duration)
                        }
                        sendBroadcast(intent)
                        startTrackingProgress()
                    }
                    mediaPlayer.setOnCompletionListener {
                        stopForeground(true)
                        stopSelf()
                    }
                }

            }
        }

    }

    private fun startTrackingProgress() {
        CoroutineScope(Dispatchers.IO).launch {
            do {
                val intent = Intent("MUSIC_STATE_UPDATE").apply {
                    putExtra("CURRENT_POSITION", mediaPlayer.currentPosition)
                    putExtra("DURATION", mediaPlayer.duration)
                    putExtra("ISPLAYING", mediaPlayer.isPlaying)
                }
                sendBroadcast(intent)
                delay(1000)
            } while (mediaPlayer.isPlaying)
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun resumeMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            startTrackingProgress()
        }
    }

    private fun nextMusic() {
        if (musicList.isNotEmpty()) {
            currentIndex = if (currentIndex < musicList.lastIndex) currentIndex + 1 else 0
            val next = musicList[currentIndex]
            val intent = Intent("MUSIC_CURRENT_MUSIC").apply {
                putExtra("CURRENT_MUSIC", next)
            }
            sendBroadcast(intent)
            playMusic(Uri.parse(next.path), next.name)
        }
    }

    private fun previousMusic() {
        if (musicList.isNotEmpty()) {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else musicList.lastIndex
            val prev = musicList[currentIndex]
            val intent = Intent("MUSIC_CURRENT_MUSIC").apply {
                putExtra("CURRENT_MUSIC", prev)
            }
            sendBroadcast(intent)
            playMusic(Uri.parse(prev.path), prev.name)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}