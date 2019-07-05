package com.chyrta.mimi.artists

import com.chyrta.converter.base.MviViewState
import com.chyrta.mimi.data.local.model.Artist

data class ArtistsViewState(
  val isLoading: Boolean,
  val artists: List<Artist>,
  val error: Throwable?
) : MviViewState {

    companion object {
        fun idle(): ArtistsViewState {
            return ArtistsViewState(
                isLoading = false,
                error = null,
                artists = emptyList()
            )
        }
    }
}
