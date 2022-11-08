package com.androidmodule.audiomoduleexample.ui.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.androidmodule.audiomodule.data.Resource
import com.androidmodule.audiomodule.model.Playlist
import com.androidmodule.audiomodule.model.PlaylistWithAudios
import com.androidmodule.audiomodule.viewmodel.AudioViewModel
import com.androidmodule.audiomoduleexample.adapter.IListPlaylistAdapter
import com.androidmodule.audiomoduleexample.adapter.ListPlaylistAdapter
import com.androidmodule.audiomoduleexample.callback.IDialogConfirmationDelete
import com.androidmodule.audiomoduleexample.callback.IDialogCreatePlaylist
import com.androidmodule.audiomoduleexample.callback.IPopupMenuPlaylist
import com.androidmodule.audiomoduleexample.databinding.FragmentPlaylistBinding
import com.androidmodule.audiomoduleexample.utils.*
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var listPlaylistAdapter: ListPlaylistAdapter
    private val audioViewModel: AudioViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listPlaylistAdapter = ListPlaylistAdapter(requireContext(), audioViewModel).apply {
            setIListPlaylistAdapter(object : IListPlaylistAdapter {
                override fun onItemClicked(playlistWithAudios: PlaylistWithAudios) {

                }

                override fun onOptionClicked(playlist: Playlist, view: View) {
                    showMenuPopupPlaylist(playlist, view)
                }
            })
        }

        binding.rvPlaylist.apply {
            setHasFixedSize(true)
            adapter = listPlaylistAdapter
            addItemDecoration(
                Utils.SpacingItemDecoration(
                    2,
                    Utils.convertDpToPixel(8f, requireContext()).toInt(),
                    true
                )
            )
        }

        audioViewModel.getAllPlaylistWithAudios().observe(requireActivity()) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> setLoading(resource.state)
                is Resource.Success -> {
                    setLoading(false)
                    Log.e("HALLO", "playlist: ${resource.data}")
                    listPlaylistAdapter.setListPlaylist(resource.data)
                }
            }
        }

        binding.fabCreatePlaylist.setOnClickListener {
            requireActivity().showDialogFormPlaylist(
                TypeAction.CREATE,
                null,
                object : IDialogCreatePlaylist {
                    override fun onSubmit(data: String) {
                        audioViewModel.createPlaylist(Playlist(null, data, null))
                    }
                })
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                rvPlaylist.visibility = View.GONE
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                rvPlaylist.visibility = View.VISIBLE
            }
        }
    }

    private fun showMenuPopupPlaylist(playlist: Playlist, view: View) {
        PopupMenu(requireContext(), view).showPopUpMenuPlaylist(
            playlist,
            object : IPopupMenuPlaylist {
                override fun onEditPlaylist(playlist: Playlist) {
                    requireActivity().showDialogFormPlaylist(
                        TypeAction.UPDATE,
                        playlist,
                        object :
                            IDialogCreatePlaylist {
                            override fun onSubmit(data: String) {
                                audioViewModel.updatePlaylist(
                                    Playlist(
                                        playlist.playlistId,
                                        data,
                                        playlist.image
                                    )
                                )
                            }
                        })
                }

                override fun onDeletePlaylist(playlist: Playlist) {
                    requireActivity().showDialogDeleteConfirmation(object :
                        IDialogConfirmationDelete {
                        override fun onDeleteConfirmation() {
                            audioViewModel.deletePlaylist(playlist)
                        }
                    })
                }
            })
    }
}