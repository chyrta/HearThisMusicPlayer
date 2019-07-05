package com.chyrta.mimi.artist

import com.chyrta.converter.base.MviAction

sealed class ArtistAction : MviAction {
    data class LoadArtistAndSongsAction(val artistName: String) : ArtistAction()
}
