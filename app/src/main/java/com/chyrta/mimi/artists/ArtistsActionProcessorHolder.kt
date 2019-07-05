package com.chyrta.mimi.artists

import com.chyrta.mimi.artists.ArtistsAction.LoadArtistsAction
import com.chyrta.mimi.artists.ArtistsResult.LoadArtistsResult
import com.chyrta.mimi.data.MimiRepository
import com.chyrta.mimi.util.flatMapIterable
import com.chyrta.mimi.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ArtistsActionProcessorHolder(
  private val mimiRepository: MimiRepository,
  private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadTopArtistsProcessor =
        ObservableTransformer<LoadArtistsAction, LoadArtistsResult> { actions ->
            actions.flatMap { _ ->
                mimiRepository.getTopSongs()
                    .flatMapIterable()
                    .concatMapSingle { mimiRepository.getArtist(it.user.permalink) }
                    .toList()
                    .toObservable()
                    .map { artists -> LoadArtistsResult.Success(artists) }
                    .cast(LoadArtistsResult::class.java)
                    .onErrorReturn(LoadArtistsResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadArtistsResult.InFlight)
            }
        }

    internal var actionProcessor =
        ObservableTransformer<ArtistsAction, ArtistsResult> { actions ->
            actions.publish { shared ->
                shared.ofType(LoadArtistsAction::class.java).compose(loadTopArtistsProcessor)
                    .cast(ArtistsResult::class.java)
                    .mergeWith(
                        shared.filter { v -> v !is LoadArtistsAction }
                            .flatMap { w ->
                                Observable.error<ArtistsResult>(
                                    IllegalArgumentException("Unknown action type: $w")
                                )
                            }
                    )
            }
        }
}
