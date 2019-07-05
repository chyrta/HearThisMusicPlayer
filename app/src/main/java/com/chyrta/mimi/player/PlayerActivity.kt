package com.chyrta.mimi.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.chyrta.mimi.R
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.AudioStreamingService
import dm.audiostreamer.CurrentSessionCallback
import dm.audiostreamer.MediaMetaData
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity(), CurrentSessionCallback, SeekBar.OnSeekBarChangeListener {

    private val streamingManager: AudioStreamingManager by inject()
    private var currentSong: MediaMetaData? = null
    private var draggingScrubber: Boolean = false

    private lateinit var seekBar: SeekBar
    private lateinit var btnPlay: Button
    private lateinit var btnForward: ImageButton
    private lateinit var ibReplay: ImageButton
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var ivSongCover: ImageView
    private lateinit var tvSongName: TextView
    private lateinit var tvArtistName: TextView

    var selectedMediaMetaData: MediaMetaData? = null
        get() = intent?.extras?.getParcelable(EXTRA_SELECTED_MEDIA_META_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        supportActionBar?.setTitle(R.string.player)

        btnPlay = findViewById(R.id.btn_play)
        seekBar = findViewById(R.id.seekBar)
        ibReplay = findViewById(R.id.ib_replay)
        btnForward = findViewById(R.id.btn_forward)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        tvEndTime = findViewById(R.id.tv_end_time)
        ivSongCover = findViewById(R.id.iv_song_cover)
        tvArtistName = findViewById(R.id.tv_artist_name)
        tvSongName = findViewById(R.id.tv_song_name)

        btnPlay.setOnClickListener { onPlayPauseButton() }
        ibReplay.setOnClickListener { seekBy(-10) }
        btnForward.setOnClickListener { seekBy(30) }
        seekBar.setOnSeekBarChangeListener(this)

        configureAudioStreaming()
        configurePlayer()
    }

    override fun currentSeekBarPosition(progress: Int) {
        if (!draggingScrubber) {
            seekBar.progress = progress
            setCurrentTime(progress)
        }
    }

    override fun playSongComplete() {
        seekBar.progress = 0
    }

    override fun playNext(indexP: Int, currentAudio: MediaMetaData?) {
    }

    override fun updatePlaybackState(state: Int) {
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                btnPlay.isActivated = true
                currentSong?.playState = PlaybackStateCompat.STATE_PLAYING
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                btnPlay.isActivated = false
                currentSong?.playState = PlaybackStateCompat.STATE_PAUSED
            }
            PlaybackStateCompat.STATE_NONE -> {
                currentSong?.playState = PlaybackStateCompat.STATE_NONE
            }
            PlaybackStateCompat.STATE_STOPPED -> {
                btnPlay.isActivated = false
                seekBar.progress = 0
                currentSong?.playState = PlaybackStateCompat.STATE_NONE
            }
            PlaybackStateCompat.STATE_BUFFERING -> {
                currentSong?.playState = PlaybackStateCompat.STATE_NONE
            }
        }
    }

    private fun setCurrentTime(progress: Int) {
        try {
            var currentTime = "00:00"
            currentSong = streamingManager?.currentAudio
            if (currentSong != null && progress != currentSong!!.mediaDuration.toInt()) {
                val currentProgress = TimeUnit.MILLISECONDS.toSeconds(progress.toLong())
                currentTime = DateUtils.formatElapsedTime(currentProgress)
            }
            tvCurrentTime.text = currentTime
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    private fun setEndTime() {
        try {
            val mediaDuration = TimeUnit.MILLISECONDS.toSeconds(currentSong?.mediaDuration!!.toLong())
            val endTimeFormatted = DateUtils.formatElapsedTime(mediaDuration)
            tvEndTime.text = endTimeFormatted
        } catch (exception: NumberFormatException) {
            Timber.e(exception)
        }
    }

    override fun playCurrent(indexP: Int, currentAudio: MediaMetaData?) {}

    override fun playPrevious(indexP: Int, currentAudio: MediaMetaData?) {}

    override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
        val currentTimeProgress = TimeUnit.MILLISECONDS.toSeconds(progress.toLong())
        val currentTimeFormatted = DateUtils.formatElapsedTime(currentTimeProgress)
        tvCurrentTime.text = currentTimeFormatted
    }

    override fun onStartTrackingTouch(seekbar: SeekBar?) {
        draggingScrubber = true
    }

    override fun onStopTrackingTouch(seekbar: SeekBar?) {
        draggingScrubber = false
        if (streamingManager?.mLastPlaybackState != null) {
            streamingManager?.onSeekTo(seekbar!!.progress)
        } else {
            seekbar!!.progress = 0
        }
    }

    private fun onPlayPauseButton() {
        if (streamingManager?.isPlaying!!) {
            streamingManager?.onPause()
        } else {
            streamingManager?.onPlay(currentSong)
        }
    }

    private fun seekBy(seconds: Int) {
        if (streamingManager?.isPlaying!!) {
            val newPosition = seekBar.progress + seconds * 1000
            streamingManager?.onSeekTo(newPosition)
        }
    }

    private fun configureAudioStreaming() {
        streamingManager?.setShowPlayerNotification(true)
        streamingManager?.setPendingIntentAct(getNotificationPendingIntent())
    }

    private fun configurePlayer() {
        if (streamingManager?.isPlaying!! && selectedMediaMetaData == null) {
            currentSong?.playState = streamingManager?.mLastPlaybackState!!
            currentSong = streamingManager?.currentAudio
        } else {
            currentSong = selectedMediaMetaData
            streamingManager?.onPlay(currentSong)
        }

        btnPlay.isActivated = streamingManager?.isPlaying!!
        seekBar.max = currentSong?.mediaDuration!!.toInt()

        tvSongName.text = currentSong!!.mediaTitle
        tvArtistName.text = currentSong!!.mediaArtist

        Glide.with(this).load(currentSong!!.mediaArt).into(ivSongCover)

        setEndTime()
    }

    override fun onStart() {
        super.onStart()
        streamingManager?.subscribesCallBack(this)
    }

    override fun onStop() {
        streamingManager?.unSubscribeCallBack()
        super.onStop()
    }

    override fun onDestroy() {
        streamingManager?.unSubscribeCallBack()
        super.onDestroy()
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.action = "openplayer"
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    companion object {
        const val EXTRA_SELECTED_MEDIA_META_DATA = "SELECTED_MEDIA_META_DATA"
    }
}
