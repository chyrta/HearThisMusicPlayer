package com.chyrta.mimi.artist

import com.chyrta.converter.base.MviResult
import com.chyrta.mimi.data.local.model.Artist
import com.chyrta.mimi.data.local.model.Song

sealed class ArtistResult : MviResult {
    sealed class LoadArtistResult : ArtistResult() {
        data class Success(val artist: Artist) : LoadArtistResult()
        data class Failure(val error: Throwable) : LoadArtistResult()
        object InFlight : LoadArtistResult()
    }

    sealed class LoadArtistSongsResult : ArtistResult() {
        data class Success(val songs: List<Song>) : LoadArtistSongsResult()
        data class Failure(val error: Throwable) : LoadArtistSongsResult()
        object InFlight : LoadArtistSongsResult()
    }
}
