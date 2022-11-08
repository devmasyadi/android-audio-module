package com.androidmodule.audiomoduleexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidmodule.audiomodule.model.AudiosItem
import com.androidmodule.audiomodule.utils.AudioModuleUtils
import com.androidmodule.audiomodule.utils.isPathExistDownload
import com.androidmodule.audiomodule.utils.toPathDownload
import com.androidmodule.audiomodule.utils.validateDownload
import com.androidmodule.audiomodule.viewmodel.AudioViewModel
import com.androidmodule.audiomoduleexample.callback.IListAudioAdapter
import com.androidmodule.audiomoduleexample.databinding.ItemAudioBinding
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
                val pathDownload = audiosItem.audioId.validateDownload().toPathDownload(context)
                val viewBtnDownload =
                    if (pathDownload.isPathExistDownload()) View.GONE else View.VISIBLE
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
