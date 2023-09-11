package com.androidmoduleexample.audiomoduleexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.model.PlaylistWithAudios
import com.google.android.material.audio.viewmodel.AudioViewModel
import com.androidmoduleexample.audiomoduleexample.R
import com.androidmoduleexample.audiomoduleexample.databinding.ItemPlaylistBinding
import com.bumptech.glide.Glide

class ListPlaylistAdapter(
    private val context: Context,
    private val audioViewModel: AudioViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listPlaylist = ArrayList<PlaylistWithAudios>()
    private var iListPlaylistAdapter: IListPlaylistAdapter? = null

    fun setListPlaylist(listPlaylist: List<PlaylistWithAudios>) {
        with(this.listPlaylist) {
            clear()
            addAll(listPlaylist)
            notifyDataSetChanged()
        }
    }

    fun setIListPlaylistAdapter(iListPlaylistAdapter: IListPlaylistAdapter) {
        this.iListPlaylistAdapter = iListPlaylistAdapter
    }

    inner class ViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: PlaylistWithAudios) {
            binding.apply {
                tvPlaylist.text = data.playlist.playlistName
                tvCountAudio.text =
                    context.resources.getString(R.string.format_count_audios, data.audios.size)
                if (data.playlist.image?.isNotEmpty() == true)
                    Glide.with(context).load(data.playlist.image).dontTransform()
                        .into(imgPlaylist)
                else
                    Glide.with(context).load(R.drawable.default_album_art).dontTransform()
                        .into(imgPlaylist)
                root.setOnClickListener {
                    iListPlaylistAdapter?.onItemClicked(data)
                }
                btnOption.setOnClickListener {
                    iListPlaylistAdapter?.onOptionClicked(data.playlist, it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemPlaylistBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(listPlaylist[position])
    }

    override fun getItemCount(): Int {
        return listPlaylist.size
    }
}

interface IListPlaylistAdapter {
    fun onItemClicked(playlistWithAudios: PlaylistWithAudios)
    fun onOptionClicked(playlist: Playlist, view: View)
}