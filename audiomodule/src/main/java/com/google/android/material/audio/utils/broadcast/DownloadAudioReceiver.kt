package com.google.android.material.audio.utils.broadcast

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import com.google.android.material.audio.utils.service.AddDownloadToDbIntentService
import com.google.android.material.audio.utils.toPathDownload
import java.io.File

const val TAG = "DownloadAudioReceiver"
class DownloadAudioReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        moveFileDownload(context)
        val intentDownload = Intent(context, AddDownloadToDbIntentService::class.java)
        val reqDownload = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        intentDownload.putExtra(AddDownloadToDbIntentService.REQ_DOWNLOAD, reqDownload)
        AddDownloadToDbIntentService.enqueueWork(context, intentDownload)
    }

    private fun moveFileDownload(context: Context) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "/${context.packageName}/")
        file.listFiles()?.forEach {
            try {
                it.copyTo(File(it.name.toPathDownload(context)), true)
                it.delete()
            } catch (e: Exception) {
                Log.e(TAG,"moveFileDownload error:${e.message}")
                it.delete()
            }
        }
    }
}