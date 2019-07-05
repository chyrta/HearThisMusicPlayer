package com.chyrta.mimi.data

import com.chyrta.mimi.data.local.model.Artist
import com.chyrta.mimi.data.local.model.Song
import io.reactivex.Single

interface MimiRepository {
    fun getArtist(artistName: String): Single<Artist>

    fun getTopSongs(): Single<List<Song>>

    fun getArtistSongs(artistName: String, page: Int): Single<List<Song>>
}
