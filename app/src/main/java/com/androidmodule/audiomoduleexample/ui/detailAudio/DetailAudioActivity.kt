package com.androidmodule.audiomoduleexample.ui.detailAudio

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.androidmodule.audiomodule.model.AudiosItem
import com.androidmodule.audiomodule.viewmodel.AudioViewModel
import com.androidmodule.audiomoduleexample.R
import com.androidmodule.audiomoduleexample.databinding.ActivityDetailAudioBinding
import com.androidmodule.audiomoduleexample.service.AudioService
import com.androidmodule.audiomoduleexample.viewmodel.AudioPlayerViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DetailAudioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAudioBinding
    private val audioPlayerViewModel: AudioPlayerViewModel by inject()
    private val audioViewModel: AudioViewModel by inject()
    private lateinit var audioService: AudioService
    private var audioServiceIsBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Current Playing"
            setDisplayHomeAsUpEnabled(true)
        }

        audioPlayerViewModel.currentPlaying.observe(this) {
            setUiAudio(it)
        }

        binding.playerView.showController()
    }

    private fun setUiAudio(audiosItem: AudiosItem) {
        binding.apply {
            tvTitle.text = audiosItem.title
            tvArtist.text = audiosItem.artist
            Glide.with(this@DetailAudioActivity).load(audiosItem.image).dontTransform().into(image)
            handleBtnFavorite(audiosItem)
        }
    }

    private fun handleBtnFavorite(audiosItem: AudiosItem) {
        lifecycleScope.launch {
            audioViewModel.isAudioFavorite(audiosItem.audioId).collect { isFavorite ->
                val imageFavorite =
                    if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                binding.btnFavorite.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@DetailAudioActivity,
                        imageFavorite
                    )
                )
                binding.btnFavorite.setOnClickListener {
                    if (!isFavorite)
                        audioViewModel.addAudioToFavorite(audiosItem)
                    else {
                        audioViewModel.deleteAudioFromFavorite(audiosItem.audioId)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val mBoundServiceIntent = Intent(this, AudioService::class.java)
        startService(mBoundServiceIntent)
        bindService(mBoundServiceIntent, serviceConnection, BIND_AUTO_CREATE)
    }


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.BinderAudioService
            audioService = binder.getService
            binding.playerView.player = audioService.player
            audioServiceIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioServiceIsBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (audioServiceIsBound)
            unbindService(serviceConnection)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}