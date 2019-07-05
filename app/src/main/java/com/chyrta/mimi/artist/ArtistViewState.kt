package com.chyrta.mimi.artist

import com.chyrta.converter.base.MviViewState
import com.chyrta.mimi.data.local.model.Song

data class ArtistViewState(
  val artistTitle: String,
  val artistLocation: String,
  val artistAvatarUrl: String,
  val artistBackgroundUrl: String,
  val artistSongs: List<Song>,
  val error: Throwable?,
  val isLoading: Boolean
) : MviViewState {
    companion object {
        fun idle(): ArtistViewState {
            return ArtistViewState(
                artistTitle = "",
                artistLocation = "",
                artistSongs = emptyList(),
                artistAvatarUrl = "",
                artistBackgroundUrl = "",
                error = null,
                isLoading = false
            )
        }
    }
}
