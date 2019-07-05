package com.chyrta.mimi

import com.chyrta.mimi.artists.ArtistsActionProcessorHolder
import com.chyrta.mimi.artists.ArtistsIntent
import com.chyrta.mimi.artists.ArtistsViewModel
import com.chyrta.mimi.artists.ArtistsViewState
import com.chyrta.mimi.data.MimiRepository
import com.chyrta.mimi.data.local.model.Artist
import com.chyrta.mimi.data.local.model.Song
import com.chyrta.mimi.util.schedulers.BaseSchedulerProvider
import com.chyrta.mimi.util.schedulers.ImmediateSchedulerProvider
import com.nhaarman.mockito_kotlin.any
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.Exception

class ArtistsViewModelTest {
    @Mock
    private lateinit var mimiRepository: MimiRepository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var testObserver: TestObserver<ArtistsViewState>
    private lateinit var songs: List<Song>
    private lateinit var artist: Artist

    @Before
    fun setupArtistsViewModel() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        artistsViewModel = ArtistsViewModel(ArtistsActionProcessorHolder(mimiRepository, schedulerProvider))

        testObserver = artistsViewModel.states().test()

        artist = Artist(
            "9065988",
            "junglemagik",
            "Jungle Magik",
            "https://api-v2.hearthis.at/junglemagik/",
            "https://hearthis.at/junglemagik/",
            "https://images.hearthis.at/c/r/o/_/uploads/9065988/image_user/w512_h512_q70_m1542396334----cropped_1542396328996.jpg",
            "https://images.hearthis.at/c/r/o/_/uploads/9065988/image_user_bg/w565_h565_c000000_q70_m1561706156----cropped_1561705891326.jpg",
            "Jungle Magik originally established in Edinburgh in 1998 bringing the biggest names in the Jungle and Drum & Bass scene north of the border until 2005. \nFast forward to 2019 and Jungle Magik has made a welcomed return to the capital with a regular event dedicated to the sounds of Jungle and Drum & Bass. \nFollow our Facebook, Instagram and Twitter accounts to keep up to date with event details.",
            "Edinburgh, UK",
            21,
            0,
            0,
            false
        )

        songs = listOf(
            Song(
                2530012,
                "2019-06-30 19:19:50",
                "2000-11-18 01:30:00",
                974507400,
                9065988,
                3596,
                "tee-bee-mc-feelman-live-jungle-magik-the-liquid-room-november-18th-2000",
                "",
                "Edinburgh, United Kingdom",
                "",
                "",
                "91.5",
                "Abm",
                "all",
                "",
                "DJ-Set",
                1,
                "Drum & Bass",
                "drumandbass",
                "Tee Bee & MC Feelman - Live @ Jungle Magik - The Liquid Room - November 18th 2000",
                "https://api-v2.hearthis.at/junglemagik/tee-bee-mc-feelman-live-jungle-magik-the-liquid-room-november-18th-2000/",
                "https://hearthis.at/junglemagik/tee-bee-mc-feelman-live-jungle-magik-the-liquid-room-november-18th-2000/",
                "https://images.hearthis.at/c/r/o/_/uploads/9065988/image_track/2530012/w200_h200_q70_m1541762195----cropped_1541762172123.jpg",
                "https://images.hearthis.at/c/r/o/_/uploads/9065988/image_track/2530012/w500_q70_m1541762195----cropped_1541762172123.jpg",
                "https://images.hearthis.at/c/r/o/_/uploads/9065988/image_track/2530012/w1000_q70_m1541762195----cropped_1541762172123.jpg",
                "https://images.hearthis.at/c/r/o/_/uploads/9065988/image_track/2530012/w565_h565_c000000_q70_m1541762195----cropped_1541762172123.jpg",
                "https://hearthis.at/_/wave_data/9065988/3000_9ac97446bab0aa7d73671bd95bac2f8d.mp3_1561915190.js",
                "https://hearthis.at/_/cache/waveform_mask/2/5/2530012.png",
                artist,
                "https://hearthis.at/junglemagik/tee-bee-mc-feelman-live-jungle-magik-the-liquid-room-november-18th-2000/listen/?s=SR9",
                "https://preview.hearthis.at/files/9ac97446bab0aa7d73671bd95bac2f8d.mp3",
                "https://hearthis.at/junglemagik/tee-bee-mc-feelman-live-jungle-magik-the-liquid-room-november-18th-2000/download/",
                "Tee Bee & MC Feelman - Live @ Jungle Magik - The Liquid Room - November 18th 2000.mp3",
                580,
                6,
                0,
                0,
                0,
                false,
                false,
                false,
                false
            )
        )
    }

    @Test
    fun loadTopArtists() {
        `when`(mimiRepository.getTopSongs()).thenReturn(Single.just(songs))
        `when`(mimiRepository.getArtist(any())).thenReturn(Single.just(artist))

        artistsViewModel.processIntents(Observable.just(ArtistsIntent.InitialIntent))

        testObserver.assertValueAt(0) { !it.isLoading }
        testObserver.assertValueAt(1) { it.isLoading }
        testObserver.assertValueAt(2) { it.artists.first() == artist }
    }

    @Test
    fun loadTopArtistsWithError() {
        `when`(mimiRepository.getTopSongs()).thenReturn(Single.error(Exception()))
        `when`(mimiRepository.getArtist(any())).thenReturn(Single.error(Exception()))

        artistsViewModel.processIntents(Observable.just(ArtistsIntent.InitialIntent))

        testObserver.assertValueAt(0) { !it.isLoading }
        testObserver.assertValueAt(1) { it.isLoading }
        testObserver.assertValueAt(2) { it.error != null }
    }
}
