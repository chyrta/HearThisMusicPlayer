package com.chyrta.mimi.artist

import com.chyrta.mimi.artist.ArtistResult.LoadArtistResult
import com.chyrta.mimi.artist.ArtistResult.LoadArtistSongsResult
import com.chyrta.mimi.data.MimiRepository
import com.chyrta.mimi.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ArtistActionProcessorHolder(
  private val mimiRepository: MimiRepository,
  private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadArtistProcessor =
        ObservableTransformer<ArtistAction.LoadArtistAndSongsAction, LoadArtistResult> { actions ->
            actions.flatMap { action ->
                mimiRepository.getArtist(action.artistName)
                    .toObservable()
                    .map { LoadArtistResult.Success(it) }
                    .cast(LoadArtistResult::class.java)
                    .onErrorReturn(LoadArtistResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadArtistResult.InFlight)
            }
        }

    private val loadArtistSongsProcessor =
        ObservableTransformer<ArtistAction.LoadArtistAndSongsAction, LoadArtistSongsResult> { actions ->
            actions.flatMap { action ->
                mimiRepository.getArtistSongs(action.artistName, 1)
                    .toObservable()
                    .map { LoadArtistSongsResult.Success(it) }
                    .cast(LoadArtistSongsResult::class.java)
                    .onErrorReturn(LoadArtistSongsResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadArtistSongsResult.InFlight)
            }
        }

    internal var actionProcessor =
        ObservableTransformer<ArtistAction, ArtistResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(ArtistAction.LoadArtistAndSongsAction::class.java).compose(loadArtistProcessor),
                    shared.ofType(ArtistAction.LoadArtistAndSongsAction::class.java).compose(loadArtistSongsProcessor)
                ).mergeWith(
                    shared.filter { v ->
                        v !is ArtistAction.LoadArtistAndSongsAction
                    }
                        .flatMap { w ->
                            Observable.error<ArtistResult>(
                                IllegalArgumentException("Unknown action type: $w")
                            )
                        }
                )
            }
        }
}
