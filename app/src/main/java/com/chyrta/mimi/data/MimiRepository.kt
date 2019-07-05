package com.chyrta.mimi.data

import com.chyrta.mimi.data.local.model.Artist
import com.chyrta.mimi.data.local.model.Song
import com.chyrta.mimi.data.remote.MimiService
import io.reactivex.Single

class MimiRepository constructor(
  private val mimiService: MimiService
) {

    fun getArtist(artistName: String): Single<Artist> {
        return mimiService.getArtist(artistName)
    }

    fun getTopSongs(): Single<List<Song>> {
        return mimiService.getTopSongs()
    }

    fun getArtistSongs(artistName: String, page: Int): Single<List<Song>> {
        return mimiService.getArtistSongs(artistName = artistName, page = page)
    }
}
