package com.chyrta.mimi.artists

import com.chyrta.converter.base.MviAction

sealed class ArtistsAction : MviAction {
    object LoadArtistsAction : ArtistsAction()
}
