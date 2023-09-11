package com.androidmoduleexample.audiomoduleexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.utils.AudioModuleUtils
import com.google.android.material.audio.utils.isPathExistDownload
import com.google.android.material.audio.utils.toPathDownload
import com.google.android.material.audio.utils.validateDownload
import com.google.android.material.audio.viewmodel.AudioViewModel
import com.androidmoduleexample.audiomoduleexample.callback.IListAudioAdapter
import com.androidmoduleexample.audiomoduleexample.databinding.ItemAudioBinding
import com.bumptech.glide.Glide

class ListItemAudioAdapter(
    private val context: Context,
    private val audioViewModel: AudioViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listAudiosItem = ArrayList<AudiosItem>()
    private var iListAudioAdapter: IListAudioAdapter? = null

    fun setListAudiosItem(lisAudiosItem: List<AudiosItem>) {
        with(this.listAudiosItem) {
            clear()
            addAll(lisAudiosItem)
            notifyDataSetChanged()
        }
    }

    fun setIListAudioAdapter(iListAudioAdapter: IListAudioAdapter) {
        this.iListAudioAdapter = iListAudioAdapter
    }

    inner class ViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(audiosItem: AudiosItem, index: Int) {
            binding.apply {
                tvTitle.text = audiosItem.title
                tvArtist.text = audiosItem.artist
                Glide.with(context).load(audiosItem.image).dontTransform().into(imageView)
                root.setOnClickListener {
                    iListAudioAdapter?.onItemClicked(audiosItem, index)
                }
                btnOption.setOnClickListener {
                    iListAudioAdapter?.onOptionClicked(audiosItem, it)
                }
                val isOnline = audiosItem.url?.startsWith("http")
                val pathDownload = audiosItem.audioId.validateDownload().toPathDownload(context)
                val viewBtnDownload =
                    if (pathDownload.isPathExistDownload() || isOnline == false) View.GONE else View.VISIBLE
                btnDownload.visibility = viewBtnDownload
                btnDownload.setOnClickListener {
                    AudioModuleUtils.downloadAudio(context, audiosItem)
                    audiosItem.url = audiosItem.audioId.validateDownload().toPathDownload(context)
                    audioViewModel.addAudioToDownload(audiosItem)
                    Toast.makeText(context, "Download ${audiosItem.title}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemAudioBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(listAudiosItem[position], position)
    }

    override fun getItemCount(): Int {
        return listAudiosItem.size
    }

}
