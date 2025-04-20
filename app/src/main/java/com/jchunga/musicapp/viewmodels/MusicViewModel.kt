package com.jchunga.musicapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jchunga.musicapp.datastore.DataStoreManager
import com.jchunga.musicapp.models.MusicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(application: Application, private val dataStoreManager: DataStoreManager) :
    AndroidViewModel(application) {

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission = _hasPermission.asStateFlow()

    private val _uri = MutableStateFlow<String?>(null)
    val uri = _uri.asStateFlow()

    private val _musicFiles = MutableStateFlow<List<MusicModel>>(emptyList())
    val musicFiles = _musicFiles.asStateFlow()

    init {
        viewModelScope.launch {
//            dataStoreManager.clearUri()
            dataStoreManager.getUri().collect { storedUri ->
                _uri.value = storedUri
                storedUri?.let { checkForMusicFiles(Uri.parse(it)) }
            }
        }
    }

    fun updatePermissionStatus(context: Context) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.POST_NOTIFICATIONS
        } else {
            ""
        }

        _hasPermission.value = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(notificationPermission) == PackageManager.PERMISSION_GRANTED
        } else {
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun saveMusicFolder(uri: String) {
        viewModelScope.launch {
            dataStoreManager.saveUri(uri)
            _uri.value = uri
        }
    }

    private fun checkForMusicFiles(folderUri: Uri) {
        viewModelScope.launch(Dispatchers.IO){
            val musicList = mutableListOf<MusicModel>()
            val pickedDir = DocumentFile.fromTreeUri(getApplication(), folderUri)

            pickedDir?.listFiles()?.forEach { file ->
                if (file.type == "audio/mpeg") {
                    val fileUri = file.uri
                    val fileName = file.name ?: "Unknown"
                    val author = getAuthorFromUri(fileUri)
                    val duration = getMusicDuration(fileUri)

                    musicList.add(
                        MusicModel(
                            name = fileName.removeSuffix(".mp3"),
                            path = fileUri.toString(),
                            author = author,
                            duration = duration
                        )
                    )
                }
            }
            _musicFiles.value = musicList
        }
    }

    private fun getAuthorFromUri(uri: Uri): String? {
        val retriever = MediaMetadataRetriever()
        return try {
            val afd = getApplication<Application>().contentResolver.openAssetFileDescriptor(uri, "r")
            if(afd != null){
                retriever.setDataSource(afd.fileDescriptor)
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            }
            "DESCONOCIDO"
        } catch (e: Exception){
            "Unknown Author"
        } finally {
            retriever.release()
        }
    }

    private fun getMusicDuration(uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            val afd = getApplication<Application>().contentResolver.openAssetFileDescriptor(uri, "r")
            return if(afd != null){
                retriever.setDataSource(getApplication<Application>(), uri)
                val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                durationStr?.toLongOrNull() ?: 0L
            } else 0L

        } catch (e: Exception) {
            0L
        } finally {
            retriever.release()
        }
    }

}

class MusicViewModelFactory(
    private val application: Application,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            return MusicViewModel(application, dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
