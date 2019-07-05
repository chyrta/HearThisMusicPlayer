package com.chyrta.mimi.artist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chyrta.converter.base.MviView
import com.chyrta.mimi.R
import com.chyrta.mimi.artist.ArtistIntent.InitialIntent
import com.chyrta.mimi.data.local.model.Song
import com.chyrta.mimi.data.local.model.convertSongToMediaMetaData
import com.chyrta.mimi.player.PlayerActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ArtistFragment : Fragment(), MviView<ArtistIntent, ArtistViewState> {

    private lateinit var tvArtistName: TextView
    private lateinit var tvArtistGeo: TextView
    private lateinit var ivBackgroundImage: ImageView
    private lateinit var ivArtistAvatar: ImageView
    private lateinit var rvArtistSongs: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    private val disposables = CompositeDisposable()
    private val viewModel: ArtistViewModel by viewModel()
    private val songAdapter: SongAdapter by inject()

    private val argumentArtistPermalink: String
        get() = arguments!!.getString(ARGUMENT_ARTIST_PERMALINK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBackgroundImage = view.findViewById(R.id.iv_background_image)
        ivArtistAvatar = view.findViewById(R.id.iv_artist_avatar)
        tvArtistName = view.findViewById(R.id.tv_artist_name)
        tvArtistGeo = view.findViewById(R.id.tv_artist_geo)
        rvArtistSongs = view.findViewById(R.id.rv_artist_songs)
        rvArtistSongs.layoutManager = layoutManager
        rvArtistSongs.adapter = songAdapter
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())

        disposables.add(songAdapter.songClickSubject.subscribe { song -> playSelectedSong(song) })
    }

    private fun playSelectedSong(song: Song) {
        val intent = Intent(activity, PlayerActivity::class.java)
        val mediaMetaData = convertSongToMediaMetaData(song)
        intent.putExtra(PlayerActivity.EXTRA_SELECTED_MEDIA_META_DATA, mediaMetaData)
        startActivity(intent)
    }

    override fun intents(): Observable<ArtistIntent> = initialIntent()

    override fun render(state: ArtistViewState) {
        if (state.error != null) {
            showMessage(state.error.message!!)
            return
        }

        tvArtistName.text = state.artistTitle
        tvArtistGeo.text = state.artistLocation
        songAdapter.items = state.artistSongs

        if (state.artistBackgroundUrl.isNotEmpty()) {
            Glide.with(this).load(state.artistBackgroundUrl).into(ivBackgroundImage)
        }

        if (state.artistAvatarUrl.isNotEmpty()) {
            Glide.with(this).load(state.artistAvatarUrl).into(ivArtistAvatar)
        }
    }

    private fun initialIntent(): Observable<ArtistIntent> {
        return Observable.just(InitialIntent(argumentArtistPermalink))
    }

    companion object {
        private const val ARGUMENT_ARTIST_PERMALINK = "ARTIST_PERMALINK"

        operator fun invoke(artistName: String): ArtistFragment {
            return ArtistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_ARTIST_PERMALINK, artistName)
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
