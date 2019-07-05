package com.chyrta.mimi.artist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chyrta.mimi.R
import com.chyrta.mimi.data.local.model.Song
import io.reactivex.subjects.PublishSubject

class SongAdapter : RecyclerView.Adapter<SongViewHolder>() {

    val songClickSubject = PublishSubject.create<Song>()

    var items: List<Song> = mutableListOf()
        set(values) {
            field = values
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_song, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        if (song != null) holder.render(song, songClickSubject)
    }

    private fun getItem(position: Int): Song? {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
