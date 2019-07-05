package com.chyrta.mimi.artists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chyrta.mimi.R
import com.chyrta.mimi.data.local.model.Artist
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ArtistsAdapter : BaseAdapter() {
    private val artistClickSubject = PublishSubject.create<Artist>()
    private var artists: List<Artist>

    init {
        this.artists = emptyList()
    }

    val artistClickObservable: Observable<Artist>
        get() = artistClickSubject

    fun replaceData(artists: List<Artist>) {
        setList(artists)
        notifyDataSetChanged()
    }

    fun setList(artists: List<Artist>) {
        this.artists = artists
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val rowView = view ?: LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_artist, viewGroup, false)

        val artist = getItem(position)

        val artistTitle = rowView.findViewById<TextView>(R.id.tv_artist_name)
        val artistAvatarImage = rowView.findViewById<ImageView>(R.id.iv_artist_avatar)
        var artistBackgroundImage = rowView.findViewById<ImageView>(R.id.iv_background_image)
        val artistGeolocation = rowView.findViewById<TextView>(R.id.tv_artist_geo)
        val artistDescription = rowView.findViewById<TextView>(R.id.tv_artist_description)

        artist.also {
            artistTitle.text = it.username
            artistGeolocation.text = it.geo
            artistDescription.text = it.description
        }

        Glide.with(viewGroup.context).load(artist.backgroundUrl).into(artistBackgroundImage)
        Glide.with(viewGroup.context).load(artist.avatarUrl).into(artistAvatarImage)

        rowView.setOnClickListener { artistClickSubject.onNext(artist) }

        return rowView
    }

    override fun getItem(position: Int): Artist = artists[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = artists.size
}
