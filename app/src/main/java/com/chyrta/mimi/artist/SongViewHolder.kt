package com.chyrta.mimi.artist

import android.text.format.DateUtils
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chyrta.mimi.R
import com.chyrta.mimi.data.local.model.Song
import io.reactivex.subjects.PublishSubject

class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvSongTitle: TextView = itemView.findViewById(R.id.tv_song_title)
    private val ivSongImage: ImageView = itemView.findViewById(R.id.iv_song_image)
    private val tvSongDescription: TextView = itemView.findViewById(R.id.tv_song_description)
    private val tvSongDuration: TextView = itemView.findViewById(R.id.tv_song_duration)
    private val btnPlay: Button = itemView.findViewById(R.id.btn_play)
    private val btnLikes: Button = itemView.findViewById(R.id.btn_likes)

    fun render(song: Song, clickSubject: PublishSubject<Song>) {
        tvSongTitle.text = song.title
        tvSongDescription.text = song.description
        tvSongDuration.text = DateUtils.formatElapsedTime(song.duration.toLong())
        btnLikes.text = "${song.favoritingsCount} likes"
        btnPlay.setOnClickListener { clickSubject.onNext(song) }

        Glide
            .with(itemView.context)
            .load(song.artworkUrl).into(ivSongImage)
    }
}
