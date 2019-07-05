package com.chyrta.mimi.artists

import androidx.lifecycle.ViewModel
import com.chyrta.converter.base.MviViewModel
import com.chyrta.mimi.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import io.reactivex.functions.BiFunction
import com.chyrta.mimi.artists.ArtistsResult.*
import io.reactivex.disposables.CompositeDisposable

class ArtistsViewModel(
  private val actionProcessorHolder: ArtistsActionProcessorHolder
) : ViewModel(), MviViewModel<ArtistsIntent, ArtistsViewState> {

    private val intentsSubject: PublishSubject<ArtistsIntent> = PublishSubject.create()
    private val statesObservable: Observable<ArtistsViewState> = compose()
    private val disposables = CompositeDisposable()

    private val intentFilter: ObservableTransformer<ArtistsIntent, ArtistsIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(ArtistsIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(ArtistsIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<ArtistsIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<ArtistsViewState> = statesObservable

    private fun compose(): Observable<ArtistsViewState> {
        return intentsSubject
            .compose<ArtistsIntent>(intentFilter)
            .map<ArtistsAction>(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(ArtistsViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ArtistsIntent): ArtistsAction {
        return when (intent) {
            is ArtistsIntent.InitialIntent -> ArtistsAction.LoadArtistsAction
            is ArtistsIntent.RefreshIntent -> ArtistsAction.LoadArtistsAction
        }
    }

    override fun onCleared() {
        disposables.dispose()
    }

    companion object {
        private val reducer = BiFunction { previousState: ArtistsViewState, result: ArtistsResult ->
            when (result) {
                is LoadArtistsResult -> when (result) {
                    is LoadArtistsResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            error = null,
                            artists = result.artists
                        )
                    }
                    is LoadArtistsResult.Failure -> {
                        previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                    is LoadArtistsResult.InFlight -> {
                        previousState.copy(isLoading = true)
                    }
                }
            }
        }
    }
}
