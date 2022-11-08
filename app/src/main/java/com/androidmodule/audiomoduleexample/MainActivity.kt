package com.androidmodule.audiomoduleexample

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androidmodule.audiomoduleexample.databinding.ActivityMainBinding
import com.androidmodule.audiomoduleexample.service.AudioService
import com.androidmodule.audiomoduleexample.ui.detailAudio.DetailAudioActivity
import com.androidmodule.audiomoduleexample.ui.download.DownloadFragment
import com.androidmodule.audiomoduleexample.ui.favorite.FavoriteFragment
import com.androidmodule.audiomoduleexample.ui.home.HomeFragment
import com.androidmodule.audiomoduleexample.ui.playlist.PlaylistFragment
import com.androidmodule.audiomoduleexample.ui.recentPlayed.RecentPlayedFragment
import com.androidmodule.audiomoduleexample.viewmodel.AudioPlayerViewModel
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject

private const val TAG = "MainActivity"
private const val REQUEST_PERMISSION_STORAGE_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var favoriteFragment: FavoriteFragment
    private lateinit var downloadFragment: DownloadFragment
    private lateinit var recentPlayedFragment: RecentPlayedFragment
    private lateinit var playlistFragment: PlaylistFragment
    private lateinit var audioService: AudioService
    private var audioServiceIsBound = false
    private val audioPlayerViewModel: AudioPlayerViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()
        loadFragment(homeFragment)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.actionHome -> loadFragment(homeFragment)
                R.id.actionFavorite -> loadFragment(favoriteFragment)
                R.id.actionDownload -> loadFragment(downloadFragment)
                R.id.actionRecentPlayed -> loadFragment(recentPlayedFragment)
                R.id.actionPlaylist -> loadFragment(playlistFragment)
            }
            true
        }
        binding.playerView.apply {
            visibility = View.GONE
            showController()
        }

        val tvTitleMiniPlayer = findViewById<TextView>(R.id.tvTitleMiniPlayer)
        val tvArtistMiniPlayer = findViewById<TextView>(R.id.tvArtistMiniPlayer)

        audioPlayerViewModel.currentPlaying.observe(this) {
            binding.playerView.visibility = View.VISIBLE
            tvTitleMiniPlayer.text = it.title
            tvArtistMiniPlayer.text = it.artist
            Glide.with(this).load(it.image).dontTransform()
                .into(binding.playerView.findViewById(R.id.imgMiniPlayer))
        }

        binding.playerView.setOnClickListener {
            Intent(this, DetailAudioActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun initFragment() {
        homeFragment = HomeFragment()
        favoriteFragment = FavoriteFragment()
        downloadFragment = DownloadFragment()
        recentPlayedFragment = RecentPlayedFragment()
        playlistFragment = PlaylistFragment()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
}