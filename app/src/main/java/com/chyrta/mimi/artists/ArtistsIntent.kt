package com.chyrta.mimi.artists

import com.chyrta.converter.base.MviIntent

sealed class ArtistsIntent : MviIntent {
    object InitialIntent : ArtistsIntent()
    object RefreshIntent : ArtistsIntent()
}
