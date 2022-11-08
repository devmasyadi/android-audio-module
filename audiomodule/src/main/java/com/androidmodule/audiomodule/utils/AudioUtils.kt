package com.androidmodule.audiomodule.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.androidmodule.audiomodule.model.AudiosItem
import java.io.File

object AudioModuleUtils {
    var baseUrl: String? = null
    var maxRecentPlayedAudio = 30

    fun downloadAudio(context: Context, audio: AudiosItem): Long {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(audio.url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("Download ${audio.title}")
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "/${context.packageName}/${audio.audioId.validateDownload()}")
        return downloadManager.enqueue(request)
    }
}

fun String.validateDownload() = this.replace("/", "")
fun String.toPathDownload(context: Context): String {
    return "${context.applicationInfo.dataDir}/${this}"
}
fun String.isPathExistDownload(): Boolean {
    return File(this).exists()
}