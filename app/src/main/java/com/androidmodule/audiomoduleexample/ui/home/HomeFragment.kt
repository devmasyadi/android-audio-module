package com.androidmodule.audiomoduleexample.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidmodule.audiomodule.data.Resource
import com.androidmodule.audiomodule.model.AudiosItem
import com.androidmodule.audiomodule.model.Playlist
import com.androidmodule.audiomodule.viewmodel.AudioViewModel
import com.androidmodule.audiomoduleexample.Constant
import com.androidmodule.audiomoduleexample.adapter.ListItemAudioAdapter
import com.androidmodule.audiomoduleexample.callback.IDialogAddAudioToPlaylist
import com.androidmodule.audiomoduleexample.callback.IListAudioAdapter
import com.androidmodule.audiomoduleexample.callback.IPopupMenuAudio
import com.androidmodule.audiomoduleexample.databinding.FragmentHomeBinding
import com.androidmodule.audiomoduleexample.ui.detailAudio.DetailAudioActivity
import com.androidmodule.audiomoduleexample.utils.AudioUtils
import com.androidmodule.audiomoduleexample.utils.showDialogAddAudioToPlaylist
import com.androidmodule.audiomoduleexample.utils.showPopUpMenuAudio
import com.androidmodule.audiomoduleexample.viewmodel.AudioPlayerViewModel
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var listItemAudioAdapter: ListItemAudioAdapter
    private val audioViewModel: AudioViewModel by inject()
    private val listPlaylist = ArrayList<Playlist>()
    private val audioPlayerViewModel: AudioPlayerViewModel by inject()

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
                    AudioUtils.playAudio(
                        indexAudio,
                        "home",
                        listItemAudioAdapter.listAudiosItem,
                        audiosItem,
                        requireActivity()
                    )
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
            putExtra(Constant.EXTRA_AUDIO, audiosItem)
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
                TODO("Not yet implemented")
            }

        })
    }
}