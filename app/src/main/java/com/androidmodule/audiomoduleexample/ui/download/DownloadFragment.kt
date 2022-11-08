package com.androidmodule.audiomoduleexample.ui.download

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
import com.androidmodule.audiomodule.viewmodel.AudioViewModel
import com.androidmodule.audiomoduleexample.Constant
import com.androidmodule.audiomoduleexample.adapter.ListItemAudioAdapter
import com.androidmodule.audiomoduleexample.callback.IListAudioAdapter
import com.androidmodule.audiomoduleexample.callback.IPopupMenuDownload
import com.androidmodule.audiomoduleexample.databinding.FragmentDownloadBinding
import com.androidmodule.audiomoduleexample.ui.detailAudio.DetailAudioActivity
import com.androidmodule.audiomoduleexample.utils.showPopUpMenuDownload
import com.androidmodule.audiomoduleserviceexoplayer.AudioUtils
import org.koin.android.ext.android.inject
import java.io.File

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private lateinit var listItemAudioAdapter: ListItemAudioAdapter
    private val audioViewModel: AudioViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDownloadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listItemAudioAdapter = ListItemAudioAdapter(requireContext(), audioViewModel).apply {
            setIListAudioAdapter(object : IListAudioAdapter {
                override fun onItemClicked(audiosItem: AudiosItem, indexAudio: Int) {
                    AudioUtils.playAudio(
                        indexAudio,
                        "download",
                        listItemAudioAdapter.listAudiosItem,
                        audiosItem,
                        requireActivity()
                    )
                }

                override fun onOptionClicked(audiosItem: AudiosItem, view: View) {
                    PopupMenu(requireContext(), view).showPopUpMenuDownload(
                        audiosItem,
                        object : IPopupMenuDownload {
                            override fun onRemoveFromDownload(audioItem: AudiosItem) {
                                audioItem.url?.let {
                                    File(it).delete()
                                    audioViewModel.deleteAudioFromDownload(audioItem.audioId)
                                }
                            }
                        })
                }
            })
        }

        binding.rvAudio.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listItemAudioAdapter
        }

        audioViewModel.listAudioDownload().observe(requireActivity()) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> setLoading(resource.state)
                is Resource.Success -> {
                    setLoading(false)
                    listItemAudioAdapter.setListAudiosItem(resource.data)
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
}