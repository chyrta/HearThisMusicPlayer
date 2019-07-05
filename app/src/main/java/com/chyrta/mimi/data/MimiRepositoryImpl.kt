package com.chyrta.mimi.data

import com.chyrta.mimi.data.local.model.Artist
import com.chyrta.mimi.data.local.model.Song
import com.chyrta.mimi.data.remote.MimiService
import io.reactivex.Single

class MimiRepositoryImpl constructor(
  private val mimiService: MimiService
) : MimiRepository {

    override fun getArtist(artistName: String): Single<Artist> {
        return mimiService.getArtist(artistName)
    }

    override fun getTopSongs(): Single<List<Song>> {
        return mimiService.getTopSongs()
    }

    override fun getArtistSongs(artistName: String, page: Int): Single<List<Song>> {
        return mimiService.getArtistSongs(artistName = artistName, page = page)
    }
}
