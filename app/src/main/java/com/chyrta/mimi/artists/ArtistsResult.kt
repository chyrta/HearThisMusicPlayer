package com.chyrta.mimi.artists

import com.chyrta.converter.base.MviResult
import com.chyrta.mimi.data.local.model.Artist

sealed class ArtistsResult : MviResult {
    sealed class LoadArtistsResult : ArtistsResult() {
        data class Success(val artists: List<Artist>) : LoadArtistsResult()
        data class Failure(val error: Throwable) : LoadArtistsResult()
        object InFlight : LoadArtistsResult()
    }
}
