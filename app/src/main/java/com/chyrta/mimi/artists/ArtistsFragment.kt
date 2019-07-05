package com.chyrta.mimi.artists

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.chyrta.converter.base.MviView
import com.chyrta.mimi.R
import com.chyrta.mimi.artist.ArtistActivity
import com.chyrta.mimi.data.local.model.Artist
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ArtistsFragment : Fragment(), MviView<ArtistsIntent, ArtistsViewState> {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var lvArtistsList: ListView

    private val refreshListPublisher = PublishSubject.create<ArtistsIntent.RefreshIntent>()
    private val disposables = CompositeDisposable()
    private val viewModel: ArtistsViewModel by viewModel()
    private val adapter: ArtistsAdapter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        lvArtistsList = view.findViewById(R.id.lv_artists_list)
        lvArtistsList.adapter = adapter
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())

        disposables.add(adapter.artistClickObservable.subscribe { artist -> showArtistProfile(artist) })
    }

    private fun showArtistProfile(artist: Artist) {
        val intent = Intent(context, ArtistActivity::class.java)
        intent.putExtra(ArtistActivity.EXTRA_ARTIST_NAME, artist.username)
        intent.putExtra(ArtistActivity.EXTRA_ARTIST_PERMALINK, artist.permalink)
        startActivity(intent)
    }

    override fun intents(): Observable<ArtistsIntent> {
        return Observable.merge(
            initialIntent(),
            refreshIntent()
        )
    }

    override fun render(state: ArtistsViewState) {
        swipeRefreshLayout.isRefreshing = state.isLoading

        if (state.error != null) {
            showMessage(state.error.message!!)
            return
        }

        if (state.artists.isNotEmpty()) {
            adapter.replaceData(state.artists)
        }
    }

    private fun initialIntent(): Observable<ArtistsIntent.InitialIntent> {
        return Observable.just(ArtistsIntent.InitialIntent)
    }

    private fun refreshIntent(): Observable<ArtistsIntent.RefreshIntent> {
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)
            .map { ArtistsIntent.RefreshIntent }
            .mergeWith(refreshListPublisher)
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
