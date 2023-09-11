package com.androidmoduleexample.audiomoduleexample.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.audio.data.Resource
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.viewmodel.AudioViewModel
import com.androidmoduleexample.audiomoduleexample.adapter.ListItemAudioAdapter
import com.androidmoduleexample.audiomoduleexample.callback.IListAudioAdapter
import com.androidmoduleexample.audiomoduleexample.databinding.FragmentFavoriteBinding
import com.androidmoduleexample.audiomoduleexample.ui.detailAudio.DetailAudioActivity
//import com.androidmoduleexample.audiomoduleserviceexoplayer.AudioUtils
import org.koin.android.ext.android.inject

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var listItemAudioAdapter: ListItemAudioAdapter
    private val audioViewModel: AudioViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listItemAudioAdapter = ListItemAudioAdapter(requireContext(), audioViewModel).apply {
            setIListAudioAdapter(object : IListAudioAdapter {
                override fun onItemClicked(audiosItem: AudiosItem, indexAudio: Int) {
                   /* AudioUtils.playAudio(
                        indexAudio,
                        "favorite",
                        listItemAudioAdapter.listAudiosItem,
                        audiosItem,
                        requireActivity()
                    )*/
                }

                override fun onOptionClicked(audiosItem: AudiosItem, view: View) {
                    TODO("Not yet implemented")
                }
            })
        }

        binding.rvAudio.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listItemAudioAdapter
        }

        audioViewModel.listAudioFavorite().observe(requireActivity()) { resource ->
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
            putExtra(com.androidmoduleexample.audiomoduleexample.Constant.EXTRA_AUDIO, audiosItem)
            startActivity(this)
        }
    }
}