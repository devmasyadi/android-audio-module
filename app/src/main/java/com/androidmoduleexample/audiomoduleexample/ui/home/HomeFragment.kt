package com.androidmoduleexample.audiomoduleexample.ui.home

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.audio.data.Resource
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.viewmodel.AudioViewModel
import com.androidmoduleexample.audiomoduleexample.adapter.ListItemAudioAdapter
import com.androidmoduleexample.audiomoduleexample.callback.IDialogAddAudioToPlaylist
import com.androidmoduleexample.audiomoduleexample.callback.IListAudioAdapter
import com.androidmoduleexample.audiomoduleexample.callback.IPopupMenuAudio
import com.androidmoduleexample.audiomoduleexample.databinding.FragmentHomeBinding
import com.androidmoduleexample.audiomoduleexample.ui.detailAudio.DetailAudioActivity
import com.androidmoduleexample.audiomoduleexample.utils.showDialogAddAudioToPlaylist
import com.androidmoduleexample.audiomoduleexample.utils.showPopUpMenuAudio
//import com.androidmoduleexample.audiomoduleserviceexoplayer.AudioUtils
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var listItemAudioAdapter: ListItemAudioAdapter
    private val audioViewModel: AudioViewModel by inject()
    private val listPlaylist = ArrayList<Playlist>()
    private lateinit var downloadReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioViewModel.getAllPlaylist().observe(requireActivity()) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    listPlaylist.clear()
                    listPlaylist.addAll(resource.data)
                }
            }
        }

        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                getWithoutLoading()
            }
        }
        val downloadIntentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(
            requireContext(),
            downloadReceiver,
            downloadIntentFilter,
            RECEIVER_EXPORTED
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listItemAudioAdapter = ListItemAudioAdapter(requireContext(), audioViewModel).apply {
            setIListAudioAdapter(object : IListAudioAdapter {
                override fun onItemClicked(audiosItem: AudiosItem, indexAudio: Int) {
                    /*AudioUtils.playAudio(
                        indexAudio,
                        "home",
                        listItemAudioAdapter.listAudiosItem,
                        audiosItem,
                        requireActivity()
                    )*/
                }

                override fun onOptionClicked(audiosItem: AudiosItem, view: View) {
                    showPopupMenuAudio(audiosItem, view)
                }
            })
        }
        binding.rvAudio.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listItemAudioAdapter
        }

        getData()
    }

    private fun getWithoutLoading() {
        audioViewModel.getData(requireContext().packageName)
            .observe(requireActivity()) { resource ->
                when (resource) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        resource.data.audios?.let { listItemAudioAdapter.setListAudiosItem(it) }
                    }
                }
            }
    }

    private fun getData() {
        audioViewModel.getData(requireContext().packageName)
            .observe(requireActivity()) { resource ->
                when (resource) {
                    is Resource.Error -> {}
                    is Resource.Loading -> setLoading(resource.state)
                    is Resource.Success -> {
                        resource.data.audios?.let { listItemAudioAdapter.setListAudiosItem(it) }
                    }
                }
            }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                rvAudio.visibility = View.GONE
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                rvAudio.visibility = View.VISIBLE
            }
        }
    }

    private fun gotoDetailAudio(audiosItem: AudiosItem) {
        Intent(requireContext(), DetailAudioActivity::class.java).apply {
            putExtra(com.androidmoduleexample.audiomoduleexample.Constant.EXTRA_AUDIO, audiosItem)
            startActivity(this)
        }
    }

    private fun showPopupMenuAudio(audiosItem: AudiosItem, view: View) {
        PopupMenu(requireContext(), view).showPopUpMenuAudio(audiosItem, object : IPopupMenuAudio {
            override fun onAddToFavorite(audiosItem: AudiosItem) {
                audioViewModel.addAudioToFavorite(audiosItem)
            }

            override fun onAddToPlaylist(audiosItem: AudiosItem) {
                requireActivity().showDialogAddAudioToPlaylist(
                    listPlaylist,
                    object : IDialogAddAudioToPlaylist {
                        override fun onAddToPlaylist(playlist: Playlist) {
                            playlist.playlistId?.let {
                                audioViewModel.addAudioToPlaylist(
                                    it,
                                    audiosItem
                                )
                            }
                        }

                        override fun onCreatePlaylist() {

                        }
                    })
            }

            override fun onShareAudio(audiosItem: AudiosItem) {

            }

        })
    }
}