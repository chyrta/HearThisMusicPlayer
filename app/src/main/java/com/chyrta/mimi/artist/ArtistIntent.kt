package com.chyrta.mimi.artist

import com.chyrta.converter.base.MviIntent

sealed class ArtistIntent : MviIntent {
    data class InitialIntent(val artistPermalink: String) : ArtistIntent()
}
