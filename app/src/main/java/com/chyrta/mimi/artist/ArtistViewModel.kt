package com.chyrta.mimi.artist

import androidx.lifecycle.ViewModel
import com.chyrta.converter.base.MviViewModel
import com.chyrta.mimi.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class ArtistViewModel(
  private val actionProcessorHolder: ArtistActionProcessorHolder
) : ViewModel(), MviViewModel<ArtistIntent, ArtistViewState> {

    private val intentsSubject: PublishSubject<ArtistIntent> = PublishSubject.create()
    private val statesObservable: Observable<ArtistViewState> = compose()
    private val disposables = CompositeDisposable()

    private val intentFilter: ObservableTransformer<ArtistIntent, ArtistIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(ArtistIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(ArtistIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<ArtistIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<ArtistViewState> = statesObservable

    private fun compose(): Observable<ArtistViewState> {
        return intentsSubject
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(ArtistViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ArtistIntent): ArtistAction {
        return when (intent) {
            is ArtistIntent.InitialIntent -> ArtistAction.LoadArtistAndSongsAction(intent.artistPermalink)
        }
    }

    override fun onCleared() {
        disposables.dispose()
    }

    companion object {
        private val reducer = BiFunction { previousState: ArtistViewState, result: ArtistResult ->
            when (result) {
                is ArtistResult.LoadArtistResult -> when (result) {
                    is ArtistResult.LoadArtistResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            artistTitle = result.artist.username,
                            artistLocation = result.artist.geo,
                            artistBackgroundUrl = result.artist.backgroundUrl,
                            artistAvatarUrl = result.artist.avatarUrl,
                            error = null
                        )
                    }
                    is ArtistResult.LoadArtistResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is ArtistResult.LoadArtistResult.InFlight -> {
                        previousState.copy(isLoading = true)
                    }
                }
                is ArtistResult.LoadArtistSongsResult -> when (result) {
                    is ArtistResult.LoadArtistSongsResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            artistSongs = result.songs,
                            error = null
                        )
                    }
                    is ArtistResult.LoadArtistSongsResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is ArtistResult.LoadArtistSongsResult.InFlight -> {
                        previousState.copy(isLoading = true)
                    }
                }
            }
        }
    }
}
